/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.warehouse.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.warehouse.entity.Warehouse;

/**
 * 仓库管理表MAPPER接口
 * @author Vigny
 * @version 2019-05-31
 */
@MyBatisMapper
public interface WarehouseMapper extends TreeMapper<Warehouse> {
	
}