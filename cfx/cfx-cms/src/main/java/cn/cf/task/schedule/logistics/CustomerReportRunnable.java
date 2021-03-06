package cn.cf.task.schedule.logistics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

import cn.cf.common.ColAuthConstants;
import cn.cf.common.Constants;
import cn.cf.common.ExportDoJsonParams;
import cn.cf.common.ExportUtil;
import cn.cf.common.utils.BeanUtils;
import cn.cf.common.utils.CommonUtil;
import cn.cf.common.utils.OSSUtils;
import cn.cf.common.utils.ZipUtils;
import cn.cf.dao.LgDeliveryOrderExtDao;
import cn.cf.dao.SysExcelStoreExtDao;
import cn.cf.dto.DataReportDto;
import cn.cf.dto.SysExcelStoreDto;
import cn.cf.dto.SysExcelStoreExtDto;
import cn.cf.entity.ReportFormsDataTypeParams;
import cn.cf.model.SysExcelStore;
import cn.cf.task.schedule.ScheduledFutureMap;
import cn.cf.util.DateUtil;

public class CustomerReportRunnable  implements Runnable {
	
	private String name;

	private String fileName;

	private String accountPk;
	
	private String uuid;
	private SysExcelStoreExtDto storeDtoTemp;
	private SysExcelStoreExtDao storeDao;

	public CustomerReportRunnable() {

	}
	public CustomerReportRunnable(String name,String uuid) {

		this.name = name;
		
		this.uuid = uuid;
	}

	@Override
	public void run() {
        //上传数据
        ScheduledFuture future = null;
        if (CommonUtil.isNotEmpty(this.name)) {
        	future = ScheduledFutureMap.map.get(this.name);
		}
        try {
            upLoadFile();
        } catch (Exception e) {
			ExportDoJsonParams.updateErrorExcelStoreStatus(this.storeDao,this.storeDtoTemp,e.getMessage());
        } finally {
            ScheduledFutureMap.stopFuture(future,this.name);
        }
    }

	private void upLoadFile() {

		LgDeliveryOrderExtDao lgOrderExtDao = (LgDeliveryOrderExtDao) BeanUtils.getBean("lgDeliveryOrderExtDao");
		this.storeDao = (SysExcelStoreExtDao) BeanUtils.getBean("sysExcelStoreExtDao");
		if (storeDao != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("isDeal", Constants.TWO);
			map.put("methodName", "exportCustomerReport_"+StringUtils.defaultIfEmpty(this.uuid, ""));
			// 查询存在要导出的任务
			List<SysExcelStoreExtDto> list = storeDao.getFirstTimeExcelStore(map);
			if (list != null && list.size() > Constants.ZERO) {
				for (SysExcelStoreExtDto storeDto : list) {
					this.storeDtoTemp = storeDto;
					this.fileName = "物流中心-数据报表-客户报表-" + storeDto.getAccountName() + "-"
							+ DateUtil.formatYearMonthDayHMS(new Date());
					if (CommonUtil.isNotEmpty(storeDto.getParams())) {
						ReportFormsDataTypeParams params = JSON.parseObject(storeDto.getParams(),
								ReportFormsDataTypeParams.class);
						this.accountPk = storeDto.getAccountPk();
						if (lgOrderExtDao != null) {
							// 设置查询参数
							Map<String, Object> orderMap = orderParams(params, storeDto);
							// 设置权限
							setColOrderParams(orderMap);
							String ossPath = "";
							int counts = lgOrderExtDao.searchGrossProfitCount(orderMap);
							if (counts > Constants.EXCEL_NUMBER_TENHOUSAND) {
								// 大于10000，分页查询数据
								ossPath = setLimitPages(orderMap, counts, lgOrderExtDao, storeDto);
							} else {
								// 如果小于或等于10000条直接上传
								ossPath = setNotLimitPages(orderMap, lgOrderExtDao, storeDto);
							}
							// 更新订单导出状态
							ExportDoJsonParams.updateExcelStoreStatus(storeDto,storeDao, ossPath);
						}
					}
				}
			}
		}

	}
	private String setNotLimitPages(Map<String, Object> orderMap, LgDeliveryOrderExtDao lgOrderExtDao,
			SysExcelStoreExtDto storeDto) {
		String ossPath = "";
		List<DataReportDto> list = lgOrderExtDao.searchCustomGrossProfit(orderMap);
		if (list != null && list.size() > Constants.ZERO) {
			ExportUtil<DataReportDto> exportUtil = new ExportUtil<DataReportDto>();
			String path = exportUtil.exportDynamicUtil("customerReportForm", this.fileName, list);
			File file = new File(path);
			// 上传到OSS
			ossPath = OSSUtils.ossUploadXls(file, Constants.FIVE);
		}
		return ossPath;
	}

	private String setLimitPages(Map<String, Object> orderMap, int counts, LgDeliveryOrderExtDao lgOrderExtDao,
			SysExcelStoreExtDto storeDto) {
		double numbers = Math.floor(counts / Constants.EXCEL_NUMBER_TENHOUSAND);// 获取分页查询次数
		orderMap.put("limit", Constants.EXCEL_NUMBER_TENHOUSAND);
		List<File> fileList = new ArrayList<>();
		for (int i = Constants.ZERO; i < numbers + Constants.ONE; i++) { //
			int start = Constants.ZERO;
			if (i != Constants.ZERO) {
				start = ExportDoJsonParams.indexPageNumbers(i);
			}
			orderMap.put("start", start);
			List<DataReportDto> list = lgOrderExtDao.searchCustomGrossProfit(orderMap);
			orderMap.remove("start");
			if (list != null && list.size() > Constants.ZERO) {
				ExportUtil<DataReportDto> exportUtil = new ExportUtil<DataReportDto>();
				String excelName = "物流中心-数据报表-客户报表-" + storeDto.getAccountName() + "-"
						+ DateUtil.formatYearMonthDayHMS(new Date())+"-"+(i+1);
				String path = exportUtil.exportDynamicUtil("customerReportForm", excelName, list);
				File file = new File(path);
				fileList.add(file);
			}
		}
		// 上传Zip到OSS
		return OSSUtils.ossUploadXls(new File(ZipUtils.fileToZip(fileList, this.fileName)), Constants.FILE);
	}

	private void setColOrderParams(Map<String, Object> orderMap) {
		orderMap.put("colLogisticsName", true);
		orderMap.put("colFromCompanyName", true);
		orderMap.put("colPurName",  CommonUtil.isExistAuthName(accountPk, ColAuthConstants.LG_REPORT_CUT_COL_PURNAME));
		orderMap.put("colToCompanyName",  CommonUtil.isExistAuthName(accountPk, ColAuthConstants.LG_REPORT_CUT_COL_TOCOMNAME));
		orderMap.put("colPurContactsTel",  CommonUtil.isExistAuthName(accountPk, ColAuthConstants.LG_REPORT_CUT_COL_PURCONTACTSTEL));
		orderMap.put("colToAddress",  CommonUtil.isExistAuthName(accountPk, ColAuthConstants.LG_REPORT_CUT_COL_TOADDRESS));
	}

	private Map<String, Object> orderParams(ReportFormsDataTypeParams params, SysExcelStoreExtDto dto) {
		String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getInsertTime());
		String str = "4,5,6,7,8,9";// 进行中状态
		String finishStr = "2,3,10";// 完成
		String tempStr = "-1";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderName", "insertTime");
		map.put("orderType", "desc");
		if (CommonUtil.isNotEmpty(params.getPurchaserName())) {
			map.put("purchaserName", params.getPurchaserName());
		}
		if (CommonUtil.isNotEmpty(params.getPurchasersContacts())) {
			map.put("purchasersContacts", params.getPurchasersContacts());
		}
		if (CommonUtil.isNotEmpty(params.getOrderPk())) {
			map.put("orderPk", params.getOrderPk());
		}
		if (CommonUtil.isNotEmpty(params.getInsertStartTime())) {
			map.put("insertStartTime", params.getInsertStartTime());
		} 
		if (CommonUtil.isNotEmpty(params.getInsertEndTime())) {
			map.put("insertEndTime", params.getInsertEndTime());
		} 
		if (CommonUtil.isNotEmpty(params.getIsSettleFreight())) {
			map.put("isSettleFreight", params.getIsSettleFreight());
		} 
		// 如果没有选择下单时间条件，则判断查询当前导出列表的添加时间作为结束时间
			map.put("insertTime", now);
		// temp : 0:初始化 ； 1 全部 ；2 进行中；3 已完成；4 异常
		int temp = params.getReportStatus() == null||params.getReportStatus().equals("") ? 0 :Integer.parseInt(params.getReportStatus());
		int orderTemp =params.getOrderStatus() == null ||params.getOrderStatus() .equals("") ? 0 : Integer.parseInt(params.getOrderStatus());
	
		
		if (temp == 0 || temp == 1 || temp == 4) {// 初始化；全部；异常
			if (temp == 4) {// 异常：isAbnormal=2
				map.put("isAbnormal", Constants.ORDER_ISABNORMAL_002);
			}
			if (orderTemp == 0) {
				map.put("orderStatus", null);
			} else {
				map.put("orderStatus", String.valueOf(orderTemp).split(","));
			}
		}
		if (temp == 2) {// 进行中
			map.put("isAbnormal", Constants.ORDER_ISABNORMAL_001);
			if (orderTemp == 0) {
				map.put("orderStatus", str.split(","));
			} else {
				if (orderTemp != Constants.ORDER_STATUS_002 && orderTemp != Constants.ORDER_STATUS_003&&orderTemp != Constants.ORDER_STATUS_010) {
					map.put("orderStatus", String.valueOf(orderTemp).split(","));
				} else {
					map.put("orderStatus", tempStr.split(","));
				}
			}
		}
		if (temp == 3) {// 已完成
			map.put("isAbnormal", Constants.ORDER_ISABNORMAL_001);
			if (orderTemp == 0) {
				map.put("orderStatus", finishStr.split(","));
			} else {
				if (orderTemp == Constants.ORDER_STATUS_002 || orderTemp == Constants.ORDER_STATUS_003 ||orderTemp == Constants.ORDER_STATUS_010) {
					map.put("orderStatus", String.valueOf(orderTemp).split(","));
				} else {
					map.put("orderStatus", tempStr.split(","));
				}
			}
		}
		
		
		return map;
	}

	public static String customerReportParam(ReportFormsDataTypeParams params, SysExcelStore dto) {
		String str = "4,5,6,7,8,9";// 进行中状态
		String finishStr = "2,3,10";// 完成
		String tempStr = "-1";
		String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getInsertTime());
		String paramStr = "";
		if (CommonUtil.isNotEmpty(params.getPurchaserName())) {
			paramStr = paramStr +  "客户名称：" + params.getPurchaserName() + ";";
		}
		
		if (CommonUtil.isNotEmpty(params.getPurchasersContacts())) {
			paramStr = paramStr + "联系电话：" + params.getPurchasersContacts() + ";";
		}
		
		if (CommonUtil.isNotEmpty(params.getOrderPk())) {
			paramStr = paramStr + "订单编号：" + params.getOrderPk() + ";";
		}
		
		paramStr = CommonUtil.getDateShow(paramStr,  params.getInsertStartTime(), params.getInsertEndTime(),  "订单时间：");
		// 订单状态
		// temp : 0:初始化 ； 1 全部 ；2 进行中；3 已完成；4 异常
		int temp = params.getReportStatus() == null||params.getReportStatus().equals("") ? 0 :Integer.parseInt(params.getReportStatus());
		int orderTemp =params.getOrderStatus() == null ||params.getOrderStatus() .equals("") ? 0 : Integer.parseInt(params.getOrderStatus());
	
				
				if (temp == 0 || temp == 1 || temp == 4) {// 初始化；全部；异常
					if (temp == 4) {// 异常：isAbnormal=2
						paramStr = paramStr + "检索条件：" + "异常;";
					}
					if (orderTemp == 0) {
					} else {
						paramStr = paramStr + "订单状态：" + getStatusName(orderTemp) + ";";
					}
				}
				if (temp == 2) {// 进行中
					paramStr = paramStr + "检索条件：" + "进行中;";
					if (orderTemp == 0) {
					} else {
						if (orderTemp != Constants.ORDER_STATUS_002 && orderTemp != Constants.ORDER_STATUS_003&&orderTemp != Constants.ORDER_STATUS_010) {
							paramStr = paramStr + "订单状态：" + getStatusName(orderTemp) + ";";
						} 
					}
				}
				if (temp == 3) {// 已完成
					paramStr = paramStr + "检索条件：" + "已完成;";
					if (orderTemp == 0) {
					} else {
						if (orderTemp == Constants.ORDER_STATUS_002 || orderTemp == Constants.ORDER_STATUS_003 ||orderTemp == Constants.ORDER_STATUS_010) {
							paramStr = paramStr + "订单状态：" + getStatusName(orderTemp) + ";";
						} 
					}
				}
				if (CommonUtil.isNotEmpty(params.getIsSettleFreight())) {
					paramStr = paramStr + "结算状态：" + getIsSettleFreightName(params.getIsSettleFreight()) + ";";
				} 	
		return paramStr;
	}

	
	private static String getIsSettleFreightName(String isSettleFreight) {
		String result = "";
		if (isSettleFreight.equals("1")) {
			result = "未结算";
		} else if (isSettleFreight.equals("2")) {
			result = "已结算";
		}
		return result;
	}

	private static String getStatusName(int orderTemp) {
		String result = "";
		if (orderTemp == Constants.ORDER_STATUS_002) {
			result = "已取消";
		} else if (orderTemp == Constants.ORDER_STATUS_003) {
			result = "已签收";
		} else if (orderTemp == Constants.ORDER_STATUS_004) {
			result = "配送中";
		} else if (orderTemp == Constants.ORDER_STATUS_005) {
			result = "提货中";
		} else if (orderTemp == Constants.ORDER_STATUS_006) {
			result = "财务已确认，待指派车辆";
		} else if (orderTemp == Constants.ORDER_STATUS_008) {
			result = "待确认";
		} else if (orderTemp == Constants.ORDER_STATUS_009) {
			result = "待付款";
		} else if (orderTemp == Constants.ORDER_STATUS_010) {
			result = "已关闭";
		}
		return result;
	}

	

}
