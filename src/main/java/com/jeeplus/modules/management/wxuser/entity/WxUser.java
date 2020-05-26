/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.wxuser.entity;

import com.jeeplus.modules.management.customer.entity.Customer;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import java.sql.Timestamp;

/**
 * 微信用户Entity
 * @author Vigny
 * @version 2020-05-25
 */
public class WxUser extends DataEntity<WxUser> {
	
	private static final long serialVersionUID = 1L;
	private String openId;		// 微信用户标记
	private String nickName;		// 昵称
	private String sex;		// 性别
	private String city;		// 城市
	private String country;		// 国家
	private String province;		// 省份
	private Customer client;		// 所属客户
	private String accessToken;		// 授权token
	private Timestamp accessTokenExpireId;		// 过期时间
	private String refreshToken;		// 用户刷新token
	private Timestamp refreshTokenExpireId;		// 用户刷新Token过期时间
	
	public WxUser() {
		super();
	}

	public WxUser(String id){
		super(id);
	}

	@ExcelField(title="微信用户标记", align=2, sort=7)
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	@ExcelField(title="昵称", align=2, sort=8)
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	@ExcelField(title="性别", dictType="sex", align=2, sort=9)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@ExcelField(title="城市", align=2, sort=10)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@ExcelField(title="国家", align=2, sort=11)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@ExcelField(title="省份", align=2, sort=12)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@ExcelField(title="所属客户", fieldType=Customer.class, value="client.name", align=2, sort=13)
	public Customer getClient() {
		return client;
	}

	public void setClient(Customer client) {
		this.client = client;
	}
	
	@ExcelField(title="授权token", align=2, sort=14)
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	@ExcelField(title="过期时间", align=2, sort=15)
	public Timestamp getAccessTokenExpireId() {
		return accessTokenExpireId;
	}

	public void setAccessTokenExpireId(Timestamp accessTokenExpireId) {
		this.accessTokenExpireId = accessTokenExpireId;
	}
	
	@ExcelField(title="用户刷新token", align=2, sort=16)
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	@ExcelField(title="用户刷新Token过期时间", align=2, sort=17)
	public Timestamp getRefreshTokenExpireId() {
		return refreshTokenExpireId;
	}

	public void setRefreshTokenExpireId(Timestamp refreshTokenExpireId) {
		this.refreshTokenExpireId = refreshTokenExpireId;
	}
	
}