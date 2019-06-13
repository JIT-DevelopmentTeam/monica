package com.jeeplus.modules.wxapi.api.core.handler;

import com.jeeplus.modules.wxapi.api.core.exception.WexinReqException;
import com.jeeplus.modules.wxapi.api.core.req.model.WeixinReqParam;

/**
 * 获取微信接口的信息
 * @author liguo
 *
 */
public interface WeiXinReqHandler {
	
	public String doRequest(WeixinReqParam weixinReqParam) throws WexinReqException;
	
}
