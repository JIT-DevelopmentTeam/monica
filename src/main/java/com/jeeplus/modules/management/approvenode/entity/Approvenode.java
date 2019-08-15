/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.approvenode.entity;

import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.User;

import javax.validation.constraints.NotNull;

/**
 * 流程节点Entity
 * @author Vigny
 * @version 2019-08-14
 */
public class Approvenode extends DataEntity<Approvenode> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 流程类型
	private String approvalEmplid;		// 流程节点用户id
	private Integer index;		// 排序
	private String name;		// 节点名称
	private Integer status;		// 使用状态
	private User user;	// 用户实体
	
	public Approvenode() {
		super();
	}

	public Approvenode(String id){
		super(id);
	}

	@ExcelField(title="流程类型", dictType="", align=2, sort=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="流程节点用户id", fieldType=String.class, value="", align=2, sort=2)
	public String getApprovalEmplid() {
		return approvalEmplid;
	}

	public void setApprovalEmplid(String approvalEmplid) {
		this.approvalEmplid = approvalEmplid;
	}
	
	@NotNull(message="排序不能为空")
	@ExcelField(title="排序", align=2, sort=3)
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	@ExcelField(title="节点名称", align=2, sort=4)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="使用状态不能为空")
	@ExcelField(title="使用状态", dictType="", align=2, sort=5)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}