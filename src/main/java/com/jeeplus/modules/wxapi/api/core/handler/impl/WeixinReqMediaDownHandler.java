package com.jeeplus.modules.wxapi.api.core.handler.impl;

import org.apache.log4j.Logger;
import com.jeeplus.modules.wxapi.api.core.annotation.ReqType;
import com.jeeplus.modules.wxapi.api.core.exception.WexinReqException;
import com.jeeplus.modules.wxapi.api.core.handler.WeiXinReqHandler;
import com.jeeplus.modules.wxapi.api.core.req.model.DownloadMedia;
import com.jeeplus.modules.wxapi.api.core.req.model.WeixinReqConfig;
import com.jeeplus.modules.wxapi.api.core.req.model.WeixinReqParam;
import com.jeeplus.modules.wxapi.api.core.util.HttpRequestProxy;
import com.jeeplus.modules.wxapi.api.core.util.WeiXinReqUtil;

import java.util.Map;

public class WeixinReqMediaDownHandler implements WeiXinReqHandler {

	private static Logger logger = Logger.getLogger(WeixinReqMediaDownHandler.class);
	
	@SuppressWarnings("rawtypes")
	public String doRequest(WeixinReqParam weixinReqParam) throws WexinReqException {
		// TODO Auto-generated method stub
		String strReturnInfo = "";
		if(weixinReqParam.getClass().isAnnotationPresent(ReqType.class)){
			DownloadMedia downloadMedia = (DownloadMedia) weixinReqParam;
			ReqType reqType = weixinReqParam.getClass().getAnnotation(ReqType.class);
			WeixinReqConfig objConfig = WeiXinReqUtil.getWeixinReqConfig(reqType.value());
			if(objConfig != null){
				String reqUrl = objConfig.getUrl();
				String filePath = downloadMedia.getFilePath();
				Map parameters = WeiXinReqUtil.getWeixinReqParam(weixinReqParam);
				parameters.remove("filePathName");
				strReturnInfo = HttpRequestProxy.downMadGet(reqUrl, parameters, "UTF-8",filePath,downloadMedia.getMedia_id());
			}
		}else{
			logger.info("没有找到对应的配置信息");
		}
		return strReturnInfo;
	}

}
