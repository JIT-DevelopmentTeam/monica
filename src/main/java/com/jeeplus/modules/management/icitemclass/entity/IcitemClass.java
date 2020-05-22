/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 商品分类管理Entity
 * @author JiaChe
 * @version 2019-05-29
 */
public class IcitemClass extends TreeEntity<IcitemClass> {
	
	private static final long serialVersionUID = 1L;
	private String erpId;		// erp分类id
	private String number;		// 编码
	private Long modifyTime;		// 同步时间戳
	private String erpNote;		// erp备注

	private List<Icitem> icitemList = Lists.newArrayList();		// 子表列表

	public IcitemClass() {
		super();
	}

	public IcitemClass(String id){
		super(id);
	}

	public String getErpId() {
		return erpId;
	}

	public void setErpId(String erpId) {
		this.erpId = erpId;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public Long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public String getErpNote() {
		return erpNote;
	}

	public void setErpNote(String erpNote) {
		this.erpNote = erpNote;
	}
	
	public  IcitemClass getParent() {
			return parent;
	}

	@Override
	public void setParent(IcitemClass parent) {
		this.parent = parent;
		
	}
	
	public List<Icitem> getIcitemList() {
		return icitemList;
	}

	public void setIcitemList(List<Icitem> icitemList) {
		this.icitemList = icitemList;
	}
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}