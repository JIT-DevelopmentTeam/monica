package com.jeeplus.modules.wxapi.jeecg.wechat.test.wechatapi;

import com.jeeplus.modules.wxapi.jeecg.wechat.api.WechatAPI;
import com.google.gson.JsonObject;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.WeChatAPIEntity;
import org.junit.Assert;
import org.junit.Test;

public class APIUserTest {

    private WechatAPI wechatAPI = WeChatAPIEntity.getWechatAPI();

    @Test
    public void getUserTest(){
        JsonObject userInfo = wechatAPI.getUser("oOAKn1EqJnrnOWP_pU4Z4DtaZ3Zk");

        System.out.println(userInfo);

        Assert.assertNotNull(userInfo);
        Assert.assertTrue(userInfo.has("nickname"));
        Assert.assertTrue(userInfo.has("sex"));
        Assert.assertTrue(userInfo.has("openid"));

    }
}
