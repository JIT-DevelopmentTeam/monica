/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.warehouse.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.warehouse.entity.Stock;

/**
 * 库存查询MAPPER接口
 * @author Vigny
 * @version 2019-05-31
 */
@MyBatisMapper
public interface StockMapper extends BaseMapper<Stock> {
	
}