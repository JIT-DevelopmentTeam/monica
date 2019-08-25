/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.usedrecord.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * token范围Entity
 * @author KicoChan
 * @version 2019-08-24
 */
public class UsedRecord extends DataEntity<UsedRecord> {
	
	private static final long serialVersionUID = 1L;
	private String tokenId;		// tokenid
	private String url;		// url
	private String purpose;		// 用途说明
	
	public UsedRecord() {
		super();
	}

	public UsedRecord(String id){
		super(id);
	}

	@ExcelField(title="tokenid", align=2, sort=7)
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	@ExcelField(title="url", align=2, sort=8)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ExcelField(title="用途说明", align=2, sort=9)
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
}