package com.jeeplus.modules.wxapi.jeecg.qywx.api.base;


import com.alibaba.fastjson.JSONObject;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.JsapiTicket;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 企业微信--access token信息
 * 
 * @author zhoujf
 * 
 */
public class JwAccessTokenAPI {
	
	private static final Logger logger = LoggerFactory.getLogger(JwAccessTokenAPI.class);

	//获取access_token的接口地址（GET）   
	public final static String access_token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=CorpID&corpsecret=SECRET";

	// 获取JSAPI_TICKET的接口地址（GET）
	public final static String JSAPI_TICKET = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";
	
	/** 
	 * 获取access_token 
	 *  
	 * @param CorpID 企业Id 
	 * @param SECRET 管理组的凭证密钥，每个secret代表了对应用、通讯录、接口的不同权限；不同的管理组拥有不同的secret 
	 * @return 
	 */  
	public static AccessToken getAccessToken(String corpID, String secret) {  
	    AccessToken accessToken = null;  
	    String requestUrl = access_token_url.replace("CorpID", corpID).replace("SECRET", secret);  
	    JSONObject jsonObject = HttpUtil.sendGet(requestUrl);
	    // 如果请求成功  
	    if (null != jsonObject) {  
	        try {  
	            accessToken = new AccessToken();  
	            accessToken.setAccesstoken(jsonObject.getString("access_token"));  
	            accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
	            logger.info("[ACCESSTOKEN]", "获取ACCESSTOKEN成功:{}", new Object[]{accessToken});
	        } catch (Exception e) {  
	            accessToken = null;  
	            // 获取token失败  
	            int errcode = jsonObject.getIntValue("errcode");
	            String errmsg = jsonObject.getString("errmsg");
	            logger.info("[ACCESSTOKEN]", "获取ACCESSTOKEN失败 errcode:{} errmsg:{}", new Object[]{errcode,errmsg});
	        }  
	    }  
	    return accessToken;  
	}


	/**
	 * 获取JsapiTicket
	 * @param accessToken
	 * @return
	 */
	public static JsapiTicket getJsapiTicket(String accessToken) {
		JsapiTicket jsapiTicket = null;
		String requestUrl = JSAPI_TICKET.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = HttpUtil.sendGet(requestUrl);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				jsapiTicket = new JsapiTicket();
				jsapiTicket.setTicket(jsonObject.getString("ticket"));
				jsapiTicket.setExpiresIn(jsonObject.getString("expires_in"));
				logger.info("[JSAPITICKET]", "获取JSAPITICKET成功:{}", new Object[]{jsapiTicket});
			} catch (Exception e) {
				jsapiTicket = null;
				// 获取ticket失败
				int errcode = jsonObject.getIntValue("errcode");
				String errmsg = jsonObject.getString("errmsg");
				logger.info("[JSAPITICKET]", "获取JSAPITICKET失败 errcode:{} errmsg:{}", new Object[]{errcode,errmsg});
			}
		}
		return jsapiTicket;
	}
	 
	
	public static void main(String[] args){
		try {
			AccessToken s = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId,JwParamesAPI.monicaSecret);
			JsapiTicket j = JwAccessTokenAPI.getJsapiTicket(s.getAccesstoken());
			System.out.println("token: " + s);
			System.out.println("ticket: " + j);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
