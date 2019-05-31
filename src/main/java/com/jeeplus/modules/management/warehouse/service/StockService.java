/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.warehouse.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.warehouse.entity.Stock;
import com.jeeplus.modules.management.warehouse.mapper.StockMapper;

/**
 * 库存查询Service
 * @author Vigny
 * @version 2019-05-31
 */
@Service
@Transactional(readOnly = true)
public class StockService extends CrudService<StockMapper, Stock> {

	public Stock get(String id) {
		return super.get(id);
	}
	
	public List<Stock> findList(Stock stock) {
		return super.findList(stock);
	}
	
	public Page<Stock> findPage(Page<Stock> page, Stock stock) {
		return super.findPage(page, stock);
	}
	
	@Transactional(readOnly = false)
	public void save(Stock stock) {
		super.save(stock);
	}
	
	@Transactional(readOnly = false)
	public void delete(Stock stock) {
		super.delete(stock);
	}
	
}