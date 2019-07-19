/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.newspush.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 微信推送新闻对象Entity
 * @author commit
 * @version 2019-06-18
 */
public class NewsPush extends DataEntity<NewsPush> {
	
	private static final long serialVersionUID = 1L;
	private String newsId;		// 新闻公告Id
	private String objId;		// 推送对象Id
	
	public NewsPush() {
		super();
	}

	public NewsPush(String id){
		super(id);
	}

	@ExcelField(title="新闻公告Id", align=2, sort=1)
	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	
	@ExcelField(title="推送对象Id", align=2, sort=2)
	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}
	
}