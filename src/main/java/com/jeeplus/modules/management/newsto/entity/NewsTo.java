/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.newsto.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 公告推送人Entity
 * @author KicoChan
 * @version 2019-08-24
 */
public class NewsTo extends DataEntity<NewsTo> {
	
	private static final long serialVersionUID = 1L;
	private String newsId;		// 新闻公告id
	private String emplId;		// 推送到
	private Integer isPush;		// 是否已经推送
	private Integer isSuccee;		// 是否已经推送成功
	private String error;		// 推送失败原因
	private Integer isRead;		// 标记是否已读
	
	public NewsTo() {
		super();
	}

	public NewsTo(String id){
		super(id);
	}

	@ExcelField(title="新闻公告id", align=2, sort=7)
	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	
	@ExcelField(title="推送到", align=2, sort=8)
	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}
	
	@NotNull(message="是否已经推送不能为空")
	@ExcelField(title="是否已经推送", dictType="newsTo_isPush", align=2, sort=9)
	public Integer getIsPush() {
		return isPush;
	}

	public void setIsPush(Integer isPush) {
		this.isPush = isPush;
	}
	
	@NotNull(message="是否已经推送成功不能为空")
	@ExcelField(title="是否已经推送成功", dictType="newsTo_isSuccee", align=2, sort=10)
	public Integer getIsSuccee() {
		return isSuccee;
	}

	public void setIsSuccee(Integer isSuccee) {
		this.isSuccee = isSuccee;
	}
	
	@ExcelField(title="推送失败原因", align=2, sort=11)
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@NotNull(message="标记是否已读不能为空")
	@ExcelField(title="标记是否已读", dictType="newsTo_isRead", align=2, sort=12)
	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	
}