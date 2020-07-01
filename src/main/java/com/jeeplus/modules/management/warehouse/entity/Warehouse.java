/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.warehouse.entity;

import com.google.common.collect.Lists;
import com.jeeplus.core.persistence.TreeEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 库存管理Entity
 * @author Vigny
 * @version 2019-05-31
 */
public class Warehouse extends TreeEntity<Warehouse> {
	
	private static final long serialVersionUID = 1L;
	private String erpid;		// erpid
	private String number;		// 编码
	private Long modifytime;		// 同步时间戳
	private Integer status;		// 状态
	
	private List<Stock> stockList = Lists.newArrayList();		// 子表列表
	
	public Warehouse() {
		super();
	}

	public Warehouse(String id){
		super(id);
	}

	public String getErpid() {
		return erpid;
	}

	public void setErpid(String erpid) {
		this.erpid = erpid;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public Long getModifytime() {
		return modifytime;
	}

	public void setModifytime(Long modifytime) {
		this.modifytime = modifytime;
	}
	
	@NotNull(message="状态不能为空")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public  Warehouse getParent() {
			return parent;
	}
	
	@Override
	public void setParent(Warehouse parent) {
		this.parent = parent;
		
	}
	
	public List<Stock> getStockList() {
		return stockList;
	}

	public void setStockList(List<Stock> stockList) {
		this.stockList = stockList;
	}
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}