package com.jeeplus.modules.wxapi.api.coupon.qrcode.model;

import com.jeeplus.modules.wxapi.api.core.annotation.ReqType;
import com.jeeplus.modules.wxapi.api.core.req.model.WeixinReqParam;

@ReqType("getGrcodeInfo")
public class QrcodeInfo  extends WeixinReqParam{
	// 名称
	private String action_name;
	// 卡卷对象
	private ActionInfo action_info;
	
	public String getAction_name() {
		return action_name;
	}
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
	public ActionInfo getAction_info() {
		return action_info;
	}
	public void setAction_info(ActionInfo action_info) {
		this.action_info = action_info;
	}
	
	
}
