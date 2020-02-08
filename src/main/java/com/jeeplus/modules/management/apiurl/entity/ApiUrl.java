/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.apiurl.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 接口管理Entity
 * @author 家成
 * @version 2019-05-30
 */
public class ApiUrl extends DataEntity<ApiUrl> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 接口名称
	private String number;		// 端口编码
	private String domain;		// 接口域名
	private String port;		// 接口端口
	private String url;		// 接口url
	private String protocol;		// 端口协议
	private String describe;		// 接口描述
	private String status;		// 接口状态
	private String isToken;		// 是否需要token
    private String usefulness;  // 接口用处
	
	public ApiUrl() {
		super();
	}

	public ApiUrl(String id){
		super(id);
	}

	@ExcelField(title="接口名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="端口编码", align=2, sort=2)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	@ExcelField(title="接口域名", align=2, sort=3)
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@ExcelField(title="接口端口", align=2, sort=4)
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	@ExcelField(title="接口url", align=2, sort=5)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ExcelField(title="端口协议", align=2, sort=6)
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	@ExcelField(title="接口描述", align=2, sort=7)
	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	@ExcelField(title="接口状态", dictType="", align=2, sort=8)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="是否需要token", dictType="", align=2, sort=9)
	public String getIsToken() {
		return isToken;
	}

	public void setIsToken(String isToken) {
		this.isToken = isToken;
	}

    public String getUsefulness() {
        return usefulness;
    }

    public void setUsefulness(String usefulness) {
        this.usefulness = usefulness;
    }
}