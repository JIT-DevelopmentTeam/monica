/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.plaurl.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 平台链接管理Entity
 * @author 家成
 * @version 2019-06-06
 */
public class PlaUrl extends DataEntity<PlaUrl> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 模块名称
	private String url;		// 模块url
	private Integer status;		// 模块状态
	private String app;		// 模块所属
	private Integer sort;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public PlaUrl() {
		super();
	}

	public PlaUrl(String id){
		super(id);
	}

	@ExcelField(title="模块名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="模块url", align=2, sort=2)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ExcelField(title="模块状态", dictType="", align=2, sort=3)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ExcelField(title="模块所属", dictType="", align=2, sort=4)
	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}
	
}