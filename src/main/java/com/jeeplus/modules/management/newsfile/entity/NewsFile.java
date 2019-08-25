/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.newsfile.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 新闻公告附件Entity
 * @author KicoChan
 * @version 2019-08-24
 */
public class NewsFile extends DataEntity<NewsFile> {
	
	private static final long serialVersionUID = 1L;
	private String originalName;		// 文件原名称
	private String name;		// 上传编码名称
	private Double size;		// 文件大小
	private Integer type;		// 文件类型
	private Integer isDown;		// 文件是否允许下载
	private String url;		// 文件路径
	private String smallUrl;		// 文件预览图路径
	private String server;		// 文件服务器地址
	private String newsId;		// 新闻id
	private Integer downCount;		// 下载次数
	
	public NewsFile() {
		super();
	}

	public NewsFile(String id){
		super(id);
	}

	@ExcelField(title="文件原名称", align=2, sort=7)
	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	@ExcelField(title="上传编码名称", align=2, sort=8)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="文件大小", align=2, sort=9)
	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}
	
	@ExcelField(title="文件类型", align=2, sort=10)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@ExcelField(title="文件是否允许下载", align=2, sort=11)
	public Integer getIsDown() {
		return isDown;
	}

	public void setIsDown(Integer isDown) {
		this.isDown = isDown;
	}
	
	@ExcelField(title="文件路径", align=2, sort=12)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ExcelField(title="文件预览图路径", align=2, sort=13)
	public String getSmallUrl() {
		return smallUrl;
	}

	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}
	
	@ExcelField(title="文件服务器地址", align=2, sort=14)
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
	
	@ExcelField(title="新闻id", align=2, sort=15)
	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	
	@ExcelField(title="下载次数", align=2, sort=16)
	public Integer getDownCount() {
		return downCount;
	}

	public void setDownCount(Integer downCount) {
		this.downCount = downCount;
	}
	
}