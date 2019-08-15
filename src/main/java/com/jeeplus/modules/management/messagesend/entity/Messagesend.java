/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.messagesend.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 消息发送Entity
 * @author MrLISH
 * @version 2019-08-14
 */
public class Messagesend extends DataEntity<Messagesend> {
	
	private static final long serialVersionUID = 1L;
	private String fromuserId;		// 发送人
	private String touserId;		// 发送给（接收人）
	private Date sendTime;		// 发送时间
	private String isRead;		// 是否已读
	private Date readTime;		// 阅读时间
	private String sendTextId;		// 发送文本id
	private String title;		// 信息标题
	private String describe;		// 消息描述
	private String isSend;		// 是否已经发送
	private String isSendToWX;		// 是否需要发送到微信
	private Date sendTimeWX;		// 发送到微信的时间
	
	public Messagesend() {
		super();
	}

	public Messagesend(String id){
		super(id);
	}

	@ExcelField(title="发送人", align=2, sort=1)
	public String getFromuserId() {
		return fromuserId;
	}

	public void setFromuserId(String fromuserId) {
		this.fromuserId = fromuserId;
	}
	
	@ExcelField(title="发送给（接收人）", align=2, sort=2)
	public String getTouserId() {
		return touserId;
	}

	public void setTouserId(String touserId) {
		this.touserId = touserId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="发送时间", align=2, sort=3)
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	@ExcelField(title="是否已读", dictType="", align=2, sort=4)
	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="阅读时间", align=2, sort=5)
	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	
	@ExcelField(title="发送文本id", align=2, sort=6)
	public String getSendTextId() {
		return sendTextId;
	}

	public void setSendTextId(String sendTextId) {
		this.sendTextId = sendTextId;
	}
	
	@ExcelField(title="信息标题", align=2, sort=7)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="消息描述", align=2, sort=8)
	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	@ExcelField(title="是否已经发送", align=2, sort=9)
	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}
	
	@ExcelField(title="是否需要发送到微信", align=2, sort=10)
	public String getIsSendToWX() {
		return isSendToWX;
	}

	public void setIsSendToWX(String isSendToWX) {
		this.isSendToWX = isSendToWX;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="发送到微信的时间", align=2, sort=11)
	public Date getSendTimeWX() {
		return sendTimeWX;
	}

	public void setSendTimeWX(Date sendTimeWX) {
		this.sendTimeWX = sendTimeWX;
	}
	
}