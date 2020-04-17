package cn.cf.service.platform;

import java.util.List;
import java.util.Map;

import cn.cf.dto.B2bLoanNumberDto;
import cn.cf.dto.B2bLoanNumberDtoEx;

public interface B2bLoanNumberService {
	
	B2bLoanNumberDto getB2bLoanNumberDto(String orderNumber);
	
	List<B2bLoanNumberDto> searchB2bLoanNumberDtoList();
	
	List<B2bLoanNumberDtoEx> searchB2bRepaymentDtoList(Map<String,String> map);
	
//	void update(B2bLoanNumber loanNumber);
	
	void updateBackCancalOrder(String orderNumber);
	/**
	 * 查询剩余未还金额
	 * @param purchaserPk
	 * @param goodsType
	 * @return
	 */
	Double searchSumLoan(String purchaserPk,int goodsType);
}
