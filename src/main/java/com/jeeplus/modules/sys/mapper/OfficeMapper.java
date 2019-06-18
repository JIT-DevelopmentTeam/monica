/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.Office;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机构MAPPER接口
 * @author jeeplus
 * @version 2017-05-16
 */
@MyBatisMapper
public interface OfficeMapper extends TreeMapper<Office> {
	
	public Office getByCode(String code);

	Office getByName(String name);

	void deleteByParentId(String id);

	List<Office> findByQyDeptParentId(@Param("DEL_FLAG_NORMAL") int DEL_FLAG_NORMAL, @Param("qyDeptParentId") int qyDeptParentId);

	List<Office> findByParentIds(@Param("DEL_FLAG_NORMAL") int DEL_FLAG_NORMAL, @Param("parentIds") String parentIds);

	/**
	 * 逻辑删除（通过id）
	 * @param office
	 */
	void deleteLogical(Office office);
}
