/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.token.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * Token表Entity
 * @author KicoChan
 * @version 2019-08-24
 */
public class Token extends DataEntity<Token> {
	
	private static final long serialVersionUID = 1L;
	private String token;		// token值
	private Integer times;		// token有效期限
	private String emplId;		// token所属
	private String clientId;		// 终端id
	private Integer clientType;		// token生成终端
	private Integer rule;		// 生成规则
	private Integer status;		// 是否已经禁用
	private Integer timeOut;		// 是否过期
	private Date startTime;		// 生成时间
	
	public Token() {
		super();
	}

	public Token(String id){
		super(id);
	}

	@ExcelField(title="token值", align=2, sort=7)
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@ExcelField(title="token有效期限", align=2, sort=8)
	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}
	
	@ExcelField(title="token所属", align=2, sort=9)
	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}
	
	@ExcelField(title="终端id", align=2, sort=10)
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	@ExcelField(title="token生成终端", dictType="token_clientType", align=2, sort=11)
	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}
	
	@ExcelField(title="生成规则", dictType="token_Rule", align=2, sort=12)
	public Integer getRule() {
		return rule;
	}

	public void setRule(Integer rule) {
		this.rule = rule;
	}
	
	@ExcelField(title="是否已经禁用", dictType="token_status", align=2, sort=13)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ExcelField(title="是否过期", dictType="token_TimeOut", align=2, sort=14)
	public Integer getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="生成时间", align=2, sort=15)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
}