package cn.cf.service;

import java.util.List;
import java.util.Map;

import cn.cf.PageModel;
import cn.cf.dto.B2bCompanyDto;
import cn.cf.dto.B2bStoreDto;
import cn.cf.dto.B2bTrancsationDto;

public interface B2bTrancsationService {
	/**
	 * 分页查询交易记录
	 * @param map
	 * @param company
	 * @return
	 */
	PageModel<B2bTrancsationDto> searchPageTrancsationList(
            Map<String, Object> map,B2bStoreDto store, B2bCompanyDto company);
	
	/**
	 * 所有交易记录
	 * @param map
	 * @param company
	 * @return
	 */
	List<B2bTrancsationDto> searchTrancsationList(Map<String, Object> map, B2bStoreDto store, B2bCompanyDto company);
}
