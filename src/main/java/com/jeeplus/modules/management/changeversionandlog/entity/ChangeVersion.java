/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.changeversionandlog.entity;

import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 变更版本和记录Entity
 * @author KicoChan
 * @version 2020-03-09
 */
public class ChangeVersion extends DataEntity<ChangeVersion> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 变更人
	private Date date;		// 变更日期
	private Integer version;		// 变更版本
	private Sobill sobill;		// 所属订单
	private List<ChangeLog> changeLogList = Lists.newArrayList();		// 子表列表
	
	public ChangeVersion() {
		super();
	}

	public ChangeVersion(String id){
		super(id);
	}

	@ExcelField(title="变更人", align=2, sort=7)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="变更日期", align=2, sort=8)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@ExcelField(title="变更版本", align=2, sort=9)
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@ExcelField(title="所属订单", align=2, sort=10)
	public Sobill getSobill() {
		return sobill;
	}

	public void setSobill(Sobill sobill) {
		this.sobill = sobill;
	}
	
	public List<ChangeLog> getChangeLogList() {
		return changeLogList;
	}

	public void setChangeLogList(List<ChangeLog> changeLogList) {
		this.changeLogList = changeLogList;
	}
}