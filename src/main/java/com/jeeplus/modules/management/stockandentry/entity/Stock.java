/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.stockandentry.entity;

import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 库存查询Entity
 * @author Vigny
 * @version 2019-05-30
 */
public class Stock extends DataEntity<Stock> {
	
	private static final long serialVersionUID = 1L;
	private String warehouseId;		// 仓库id
	private String warehousePosition;		// 仓位
	private String commodityNumber;		// 商品代码
	private String commodityName;		// 商品名称
	private String specification;		// 规格型号
	private String unit;		// 单位
	private Double total;		// 库存总数
	private List<Stockentry> stockentryList = Lists.newArrayList();		// 子表列表
	
	public Stock() {
		super();
	}

	public Stock(String id){
		super(id);
	}

	@ExcelField(title="仓库id", align=2, sort=1)
	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	@ExcelField(title="仓位", align=2, sort=2)
	public String getWarehousePosition() {
		return warehousePosition;
	}

	public void setWarehousePosition(String warehousePosition) {
		this.warehousePosition = warehousePosition;
	}
	
	@ExcelField(title="商品代码", align=2, sort=3)
	public String getCommodityNumber() {
		return commodityNumber;
	}

	public void setCommodityNumber(String commodityNumber) {
		this.commodityNumber = commodityNumber;
	}
	
	@ExcelField(title="商品名称", align=2, sort=4)
	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	
	@ExcelField(title="规格型号", align=2, sort=5)
	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}
	
	@ExcelField(title="单位", align=2, sort=6)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@ExcelField(title="库存总数", align=2, sort=7)
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	public List<Stockentry> getStockentryList() {
		return stockentryList;
	}

	public void setStockentryList(List<Stockentry> stockentryList) {
		this.stockentryList = stockentryList;
	}
}