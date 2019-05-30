/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.stockandentry.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.stockandentry.entity.Stockentry;

/**
 * 子库存查询表MAPPER接口
 * @author Vigny
 * @version 2019-05-30
 */
@MyBatisMapper
public interface StockentryMapper extends BaseMapper<Stockentry> {
	
}