/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.stockandentry.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.management.stockandentry.entity.Stock;
import com.jeeplus.modules.management.stockandentry.mapper.StockMapper;
import com.jeeplus.modules.management.stockandentry.entity.Stockentry;
import com.jeeplus.modules.management.stockandentry.mapper.StockentryMapper;

/**
 * 库存查询Service
 * @author Vigny
 * @version 2019-05-30
 */
@Service
@Transactional(readOnly = true)
public class StockService extends CrudService<StockMapper, Stock> {

	@Autowired
	private StockentryMapper stockentryMapper;
	
	public Stock get(String id) {
		Stock stock = super.get(id);
		stock.setStockentryList(stockentryMapper.findList(new Stockentry(stock)));
		return stock;
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
		for (Stockentry stockentry : stock.getStockentryList()){
			if (stockentry.getId() == null){
				continue;
			}
			if (Stockentry.DEL_FLAG_NORMAL.equals(stockentry.getDelFlag())){
				if (StringUtils.isBlank(stockentry.getId())){
					stockentry.setStockid(stock);
					stockentry.preInsert();
					stockentryMapper.insert(stockentry);
				}else{
					stockentry.preUpdate();
					stockentryMapper.update(stockentry);
				}
			}else{
				stockentryMapper.delete(stockentry);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Stock stock) {
		super.delete(stock);
		stockentryMapper.delete(new Stockentry(stock));
	}
	
}