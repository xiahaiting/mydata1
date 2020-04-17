package cn.cf.common.creditpay.suzhou;

import java.util.Date;
import java.util.Map;

import cn.cf.property.PropertyConfig;
import cn.cf.util.DateUtil;
import cn.cf.util.KeyUtils;

import com.bsz.becp.core.client.BSZBECPClient;

public class PostUtils {

	public static String post(Map<String,Object> map){
		BSZBECPClient becpClient = new BSZBECPClient();
		becpClient.initialize(PropertyConfig.getProperty("sz_bank_url"));
		map.put("clientFlowNo", DateUtil.formatYearMonthDayHms(new Date())+KeyUtils.getRandom(18));
		return becpClient.sendAndReceive(map);
	}
}
