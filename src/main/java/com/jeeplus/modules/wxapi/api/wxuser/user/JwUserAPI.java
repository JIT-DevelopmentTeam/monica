package com.jeeplus.modules.wxapi.api.wxuser.user;

import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.token.AccessTokenUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.jeeplus.modules.wxapi.api.core.common.WxstoreUtils;
import com.jeeplus.modules.wxapi.api.core.exception.WexinReqException;
import com.jeeplus.modules.wxapi.api.core.req.WeiXinReqService;
import com.jeeplus.modules.wxapi.api.core.req.model.user.UserInfoListGet;
import com.jeeplus.modules.wxapi.api.wxuser.user.model.Wxuser;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信--用户
 * 
 * @author lizr
 * 
 */
public class JwUserAPI {
	private static Logger logger = LoggerFactory.getLogger(JwUserAPI.class);
	//获取用户基本信息（包括UnionID机制）
    private static String GET_USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //获取访问用户身份 , 使用应用的token ,得到UserId(企业用户)   或  OpenId (非企业用户)
    public static String GET_USER_INFO_BY_CODE_URL	= "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";

    
	/**
	 * 根据user_openid 获取关注用户的基本信息
	 * 
	 * @param shelf_id
	 * @return
	 * @throws WexinReqException
	 */
	public static Wxuser  getWxuser(String accesstoken,String user_openid) throws WexinReqException {
		if (accesstoken != null) {
			 String requestUrl = GET_USER_URL.replace("ACCESS_TOKEN", accesstoken).replace("OPENID", user_openid);
			 JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", null);
			logger.info(result.toString());
			// 正常返回
			Wxuser wxuser = null;
			Object error = result.get("errcode");
			wxuser = (Wxuser) JSONObject.toBean(result, Wxuser.class);
			return wxuser;
		}
		return null;
	}

    /**
     * 根据user_openid 获取关注用户的基本信息
     *
     * @param shelf_id
     * @return
     * @throws WexinReqException
     */
    public static Map<String,Object> getWxuserInfo(String code) throws WexinReqException {
        Map<String,Object> userMap = new HashMap<>();
        String accesstoken;
        if (CacheUtils.get("monicaAccessToken") != null) {
            accesstoken = CacheUtils.get("monicaAccessToken").toString();
        } else {
            AccessTokenUtils.updateAgentToken();
            accesstoken = CacheUtils.get("monicaAccessToken").toString();
        }
        if (accesstoken != null) {
            String requestUrl = GET_USER_INFO_BY_CODE_URL.replace("ACCESS_TOKEN", accesstoken).replace("CODE", code);
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", null);
            userMap.put("UserId",result.getString("UserId"));
            logger.info(result.toString());
            // 正常返回
            Object error = result.get("errcode");
            return userMap;
        }
        return null;
    }

	/**
	 * 获取所有关注用户信息信息
	 * 
	 * @return
	 * @throws WexinReqException 
	 */
	public static List<Wxuser> getAllWxuser(String accesstoken,String next_openid) throws WexinReqException {
		if (accesstoken != null) {
			UserInfoListGet userInfoListGet = new UserInfoListGet();
			userInfoListGet.setAccess_token(accesstoken);
			userInfoListGet.setNext_openid(next_openid);
			JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(userInfoListGet);
			Object error = result.get("errcode");
			List<Wxuser> lstUser = null;
			Wxuser mxuser = null;
			int total = result.getInt("total");
			int count = result.getInt("count");
			String strNextOpenId = result.getString("next_openid");
			JSONObject data = result.getJSONObject("data");
			lstUser = new ArrayList<Wxuser>(total);
			if (count > 0) {
				JSONArray lstOpenid = data.getJSONArray("openid");
				int iSize = lstOpenid.size();
				for (int i = 0; i < iSize; i++) {
					String openId = lstOpenid.getString(i);
					mxuser = getWxuser(accesstoken, openId);
					lstUser.add(mxuser);
				}
				if (strNextOpenId != null) {
					lstUser.addAll(getAllWxuser(accesstoken, strNextOpenId));
				}
			}
			return lstUser;
		}
		return null;
	}
	
}
