/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.jurisdiction.entity;

import com.jeeplus.modules.management.customer.entity.Customer;
import com.jeeplus.modules.management.warehouse.entity.Warehouse;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 库存数据权限Entity
 * @author Vigny
 * @version 2020-05-27
 */
public class Jurisdiction extends DataEntity<Jurisdiction> {
	
	private static final long serialVersionUID = 1L;
	private Customer client;		// 客户id
	private Warehouse warehouse;		// 仓库id
	private Icitem item;		// 商品id
	
	public Jurisdiction() {
		super();
	}

	public Jurisdiction(String id){
		super(id);
	}

	@ExcelField(title="客户id", fieldType=Customer.class, value="client.name", align=2, sort=7)
	public Customer getClient() {
		return client;
	}

	public void setClient(Customer client) {
		this.client = client;
	}
	
	@ExcelField(title="仓库id", fieldType=Warehouse.class, value="warehouse.name", align=2, sort=8)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
	@ExcelField(title="商品id", fieldType=Icitem.class, value="item.name", align=2, sort=9)
	public Icitem getItem() {
		return item;
	}

	public void setItem(Icitem item) {
		this.item = item;
	}
	
}