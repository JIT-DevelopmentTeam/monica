/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.stockandentry.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 子库存查询表Entity
 * @author Vigny
 * @version 2019-05-30
 */
public class Stockentry extends DataEntity<Stockentry> {
	
	private static final long serialVersionUID = 1L;
	private Stock stockid;		// 父库存查询表id 父类
	private String batchNumber;		// 批号
	private Integer level;		// 等级
	private String colorNumber;		// 色号
	private Double number;		// 库存数量
	
	public Stockentry() {
		super();
	}

	public Stockentry(String id){
		super(id);
	}

	public Stockentry(Stock stockid){
		this.stockid = stockid;
	}

	public Stock getStockid() {
		return stockid;
	}

	public void setStockid(Stock stockid) {
		this.stockid = stockid;
	}
	
	@ExcelField(title="批号", align=2, sort=2)
	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	
	@ExcelField(title="等级", align=2, sort=3)
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	@ExcelField(title="色号", align=2, sort=4)
	public String getColorNumber() {
		return colorNumber;
	}

	public void setColorNumber(String colorNumber) {
		this.colorNumber = colorNumber;
	}
	
	@ExcelField(title="库存数量", align=2, sort=5)
	public Double getNumber() {
		return number;
	}

	public void setNumber(Double number) {
		this.number = number;
	}
	
}