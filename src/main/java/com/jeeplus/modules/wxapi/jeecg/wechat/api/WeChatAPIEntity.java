package com.jeeplus.modules.wxapi.jeecg.wechat.api;

import com.jeeplus.modules.wxapi.jeecg.wechat.api.entity.ConfigEntity;

public class WeChatAPIEntity {

    private static WechatAPI wechatAPI = null;
    static {
        wechatAPI = new WechatAPI(ConfigEntity.appid, ConfigEntity.appsecret);
    }
    public static WechatAPI getWechatAPI(){
        return wechatAPI;
    }
}
