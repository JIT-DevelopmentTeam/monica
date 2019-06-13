package com.jeeplus.modules.wxapi.api.third.model;

import com.jeeplus.modules.wxapi.api.core.req.model.WeixinReqParam;


/**
 * 获取预授权码
 * @author zhangdaihao
 *
 */
public class GetPreAuthCodeParam  extends WeixinReqParam{

	private String component_appid;
	public String getComponent_appid() {
		return component_appid;
	}
	public void setComponent_appid(String component_appid) {
		this.component_appid = component_appid;
	}
	
}
