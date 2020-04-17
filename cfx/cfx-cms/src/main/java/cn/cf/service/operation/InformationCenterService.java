package cn.cf.service.operation;

import java.util.List;

import cn.cf.dto.SysRegionsDto;

public interface InformationCenterService {
	
	/**
	 * 把保存在数据库里的资讯信息导入mongo
	 * @return
	 */
	String importNewsToMongo();
	

	
	
	/**
	 * 根据parentPk查询地区
	 * @param parentPk
	 * @return
	 */
	List<SysRegionsDto> searchSysRegionsList(String parentPk);
}
