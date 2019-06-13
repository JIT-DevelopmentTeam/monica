package com.jeeplus.modules.wxapi.jeecg.weibo.api;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.modules.wxapi.jeecg.weibo.dto.WeiboSendDto;
import com.jeeplus.modules.wxapi.jeecg.weibo.exception.BusinessException;
import com.jeeplus.modules.wxapi.jeecg.weibo.util.HttpUtil;
import com.jeeplus.modules.wxapi.jeecg.weibo.util.WeiboSendUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeiboSendApi {
	
	private static final Logger logger = LoggerFactory.getLogger(WeiboSendApi.class);
	//指定一个图片URL地址抓取后上传并同时发布一条新微博
	private static final String upload_url_text_url="https://api.weibo.com/2/statuses/upload_url_text.json?1=1";
	//发布一条新微博
	private static String update_url = "https://api.weibo.com/2/statuses/update.json?1=1";
	//删除一条微博
	private static String delete_url = "https://api.weibo.com/2/statuses/destroy.json?1=1";
	
	
	public static JSONObject sendWeibo(WeiboSendDto send){
		JSONObject j=null;
		try {
			logger.info("发布新微博的参数为:"+send.toString());
			//验证请求参数
			WeiboSendUtil.getSendParmValidate(send);
			if(StringUtils.isEmpty(send.getUrl())){
				String sendUrl = WeiboSendUtil.getSendUrl(update_url, send);
				logger.info("发布新微博的路径为："+sendUrl);
				j = HttpUtil.httpRequest(sendUrl, "POST", "");
			}else{
				String sendUrl = WeiboSendUtil.getSendUrl(upload_url_text_url, send);
				logger.info("发布新微博的路径为："+sendUrl);
				j = HttpUtil.httpRequest(sendUrl, "POST", "");
			}
			if(j!=null){
				logger.info("发布新微博的结果为:"+j.toString());
			}else{
				logger.info("发布新微博的结果为:null");
			}
		}catch(BusinessException e)	{
			logger.info(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	
	public static JSONObject delWeibo(WeiboSendDto send){
		JSONObject j=null;
		try {
			logger.info("删除微博的参数为:"+send.toString());
			//验证请求参数
			WeiboSendUtil.delParmValidate(send);
			String delUrl = WeiboSendUtil.getDelUrl(delete_url, send);
			logger.info("删除微博的路径为："+delUrl);
			j = HttpUtil.httpRequest(delUrl, "POST", "");
			if(j!=null){
				logger.info("删除微博的结果为:"+j.toString());
			}else{
				logger.info("删除微博的结果为:null");
			}
		}catch(BusinessException e)	{
			logger.info(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
}
