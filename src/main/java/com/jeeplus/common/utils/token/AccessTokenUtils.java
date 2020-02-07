package com.jeeplus.common.utils.token;


import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;


/**
 * AccessToken工具类
 * @author KicoChan
 */
public class AccessTokenUtils {

    /**
     * 更新Token
     */
    public static void updateAgentToken() {
        if (CacheUtils.get("addressBookAccessToken") == null) {
            AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId,JwParamesAPI.contactSecret);
            if (accessToken != null && StringUtils.isNotBlank(accessToken.getAccesstoken())) {
                CacheUtils.put("addressBookAccessToken",accessToken.getAccesstoken());
            }
        } else {
            String addressBookAccessToken = CacheUtils.get("addressBookAccessToken").toString();
            AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId,JwParamesAPI.contactSecret);
            if (!accessToken.getAccesstoken().equals(addressBookAccessToken)) {
                CacheUtils.remove("addressBookAccessToken");
                CacheUtils.put("addressBookAccessToken",accessToken.getAccesstoken());
            }
        }
    }
}
