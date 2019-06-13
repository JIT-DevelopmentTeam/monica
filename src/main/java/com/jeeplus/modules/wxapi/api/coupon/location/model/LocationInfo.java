package com.jeeplus.modules.wxapi.api.coupon.location.model;

import com.jeeplus.modules.wxapi.api.core.annotation.ReqType;
import com.jeeplus.modules.wxapi.api.core.req.model.WeixinReqParam;

@ReqType("getLocationInfo")
public class LocationInfo extends WeixinReqParam {
	// 图片地址
	private String filePathName;

	public String getFilePathName() {
		return filePathName;
	}

	public void setFilePathName(String filePathName) {
		this.filePathName = filePathName;
	}


	
}
