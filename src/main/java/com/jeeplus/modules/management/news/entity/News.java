/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.news.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 新闻公告Entity
 * @author Vigny
 * @version 2019-06-10
 */
public class News extends DataEntity<News> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String describe;		// 摘要
	private String content;		// 内容
	private String authorid;		// 发布人
	private String mainpic;		// 封面图片路径
	private Integer isPublic;		// 是否发布
	private Integer headline;		// 是否设置为头条
	private String deptid;		// 发布人部门
	private Date starttime;		// 显示时间开始
	private Date endtime;		// 显示结束时间
	private Integer isPush;		// 是否推送
	private Date push;		// 推送时间
	private String pushrule;		// 推送规则
	private Integer readCount;		// 阅读次数
	
	public News() {
		super();
	}

	public News(String id){
		super(id);
	}

	@ExcelField(title="标题", align=2, sort=1)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="摘要", align=2, sort=2)
	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	@ExcelField(title="内容", align=2, sort=3)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@ExcelField(title="发布人", align=2, sort=4)
	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	
	@ExcelField(title="封面图片路径", align=2, sort=5)
	public String getMainpic() {
		return mainpic;
	}

	public void setMainpic(String mainpic) {
		this.mainpic = mainpic;
	}
	
	@NotNull(message="是否发布不能为空")
	@ExcelField(title="是否发布", dictType="", align=2, sort=6)
	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}
	
	@NotNull(message="是否设置为头条不能为空")
	@ExcelField(title="是否设置为头条", dictType="", align=2, sort=7)
	public Integer getHeadline() {
		return headline;
	}

	public void setHeadline(Integer headline) {
		this.headline = headline;
	}
	
	@ExcelField(title="发布人部门", align=2, sort=8)
	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="显示时间开始", align=2, sort=9)
	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="显示结束时间", align=2, sort=10)
	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	
	@NotNull(message="是否推送不能为空")
	@ExcelField(title="是否推送", dictType="", align=2, sort=11)
	public Integer getIsPush() {
		return isPush;
	}

	public void setIsPush(Integer isPush) {
		this.isPush = isPush;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="推送时间", align=2, sort=12)
	public Date getPush() {
		return push;
	}

	public void setPush(Date push) {
		this.push = push;
	}
	
	@ExcelField(title="推送规则", align=2, sort=13)
	public String getPushrule() {
		return pushrule;
	}

	public void setPushrule(String pushrule) {
		this.pushrule = pushrule;
	}
	
	@ExcelField(title="阅读次数", align=2, sort=14)
	public Integer getReadCount() {
		return readCount;
	}

	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}
	
}