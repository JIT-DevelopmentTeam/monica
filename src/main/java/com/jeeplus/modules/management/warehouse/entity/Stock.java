/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.warehouse.entity;


import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 库存查询Entity
 * @author Vigny
 * @version 2019-05-31
 */
public class Stock extends DataEntity<Stock> {
	
	private static final long serialVersionUID = 1L;
	private Warehouse warehouseId;		// 仓库id 父类
	private String commodityNumber;		// 商品代码
	private String commodityName;		// 商品名称
	private String specification;		// 规格型号
	private String unit;		// 单位
	private String warehouse;	// 仓库
	private String warehousePosition;		// 仓位
	private String batchNumber;		// 批号
	private String level;		// 等级
	private String colorNumber;		// 色号
	private String direction;		// 方向
	private Double total;		// 库存总数

	public Warehouse getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Warehouse warehouseId) {
		this.warehouseId = warehouseId;
	}

	@ExcelField(title="产品代码", align=2, sort=1)
	public String getCommodityNumber() {
		return commodityNumber;
	}

	public void setCommodityNumber(String commodityNumber) {
		this.commodityNumber = commodityNumber;
	}

	@ExcelField(title="产品名称", align=2, sort=2)
	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	@ExcelField(title="规格型号", align=2, sort=3)
	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	@ExcelField(title="单位", align=2, sort=4)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@ExcelField(title="仓库", align=2, sort=5)
	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	@ExcelField(title="仓位", align=2, sort=6)
	public String getWarehousePosition() {
		return warehousePosition;
	}

	public void setWarehousePosition(String warehousePosition) {
		this.warehousePosition = warehousePosition;
	}

	@ExcelField(title="批号", align=2, sort=7)
	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	@ExcelField(title="等级", align=2, sort=8)
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@ExcelField(title="色号", align=2, sort=9)
	public String getColorNumber() {
		return colorNumber;
	}

	public void setColorNumber(String colorNumber) {
		this.colorNumber = colorNumber;
	}

	@ExcelField(title="方向", align=2, sort=10)
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@ExcelField(title="库存总数", align=2, sort=11)
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}