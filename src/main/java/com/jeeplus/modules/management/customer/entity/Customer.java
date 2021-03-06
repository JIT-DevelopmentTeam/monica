/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.customer.entity;


import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

import java.sql.Timestamp;

/**
 * 客户管理Entity
 * @author commit
 * @version 2019-05-30
 */
public class Customer extends DataEntity<Customer> {
	
	private static final long serialVersionUID = 1L;
	private String erpId;		// ERPId
	private String number;		// 客户编码
	private String name;		// 客户名称
	private String deptId;		// 部门归属id
	private String emplId;		// 员工所属id
	private String currencyId;		// 结算币种
	private String erpNote;		// ERP备注
	private Integer status;		// 状态
	private String crediTamt;		// 信用证
	private Long modifyTime;
	private String info;	// 客户信息（用途：条件搜索）
	private Office office;
	private User user;
	
	public Customer() {
		super();
	}

	public Customer(String id){
		super(id);
	}

	@ExcelField(title="ERPId", align=2, sort=1)
	public String getErpId() {
		return erpId;
	}

	public void setErpId(String erpId) {
		this.erpId = erpId;
	}
	
	@ExcelField(title="客户编码", align=2, sort=2)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	@ExcelField(title="客户名称", align=2, sort=3)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="部门归属id", align=2, sort=4)
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	@ExcelField(title="员工所属id", align=2, sort=5)
	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}
	
	@ExcelField(title="结算币别id", dictType="", align=2, sort=6)
	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	
	@ExcelField(title="ERP备注", align=2, sort=7)
	public String getErpNote() {
		return erpNote;
	}

	public void setErpNote(String erpNote) {
		this.erpNote = erpNote;
	}
	
	@ExcelField(title="状态", align=2, sort=8)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ExcelField(title="信用证", align=2, sort=9)
	public String getCrediTamt() {
		return crediTamt;
	}

	public void setCrediTamt(String crediTamt) {
		this.crediTamt = crediTamt;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}