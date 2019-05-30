/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.warehouse.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 仓库管理表Entity
 * @author Vigny
 * @version 2019-05-30
 */
public class Warehouse extends DataEntity<Warehouse> {
	
	private static final long serialVersionUID = 1L;
	private String erpid;		// erpid
	private String number;		// 编码
	private String name;		// 名称
	private Date modifytime;		// 同步时间戳
	private Integer status;		// 状态
	
	public Warehouse() {
		super();
	}

	public Warehouse(String id){
		super(id);
	}

	@ExcelField(title="erpid", align=2, sort=1)
	public String getErpid() {
		return erpid;
	}

	public void setErpid(String erpid) {
		this.erpid = erpid;
	}
	
	@ExcelField(title="编码", align=2, sort=2)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	@ExcelField(title="名称", align=2, sort=3)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="同步时间戳", align=2, sort=4)
	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}
	
	@ExcelField(title="状态", align=2, sort=5)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}