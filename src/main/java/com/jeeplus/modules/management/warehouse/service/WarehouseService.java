/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.warehouse.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.management.warehouse.entity.Warehouse;
import com.jeeplus.modules.management.warehouse.mapper.WarehouseMapper;

/**
 * 库存管理Service
 * @author Vigny
 * @version 2019-05-31
 */
@Service
@Transactional(readOnly = true)
public class WarehouseService extends TreeService<WarehouseMapper, Warehouse> {

	public Warehouse get(String id) {
		return super.get(id);
	}
	
	public List<Warehouse> findList(Warehouse warehouse) {
		if (StringUtils.isNotBlank(warehouse.getParentIds())){
			warehouse.setParentIds(","+warehouse.getParentIds()+",");
		}
		return super.findList(warehouse);
	}
	
	@Transactional(readOnly = false)
	public void save(Warehouse warehouse) {
		super.save(warehouse);
	}
	
	@Transactional(readOnly = false)
	public void delete(Warehouse warehouse) {
		super.delete(warehouse);
	}
	
}