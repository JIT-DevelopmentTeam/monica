package com.jeeplus.modules.wxapi.jeecg.wechat.test.wechatapi;

import com.jeeplus.modules.wxapi.jeecg.wechat.api.WechatAPI;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.WeChatAPIEntity;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.entity.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.entity.ConfigEntity;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.entity.Ticket;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class APICommonsTest {

    private WechatAPI wechatAPI = WeChatAPIEntity.getWechatAPI();

    @Test
    public void getAppidTest(){
        String appid = wechatAPI.getAppid();
//        System.out.println(appid);
        Assert.assertEquals(appid, ConfigEntity.appid);
    }
    @Test
    public void getAppsecretTest(){
        String appsecret = wechatAPI.getAppsecret();

        Assert.assertEquals(appsecret, ConfigEntity.appsecret);
    }

    @Test
    public void getAccessTokenTest(){

        AccessToken accessToken = wechatAPI.getAccessToken();

        Assert.assertNotEquals(accessToken.getAccessToken(), null);
        Assert.assertNotEquals(accessToken.getExpireTime(), null);

    }

    @Test
    public void getIpTest(){

        List<String> ips = wechatAPI.getIp();

        Assert.assertTrue(ips.size() > 0);

    }

    @Test
    public void getLatestTicketTest(){

        Ticket ticket = wechatAPI.getLatestTicket();

        Assert.assertNotEquals(ticket.getTicket(), null);
        System.out.println(ticket.getTicket());

    }


    @Test
    public void getToken(){
        WechatAPI wechatAPI=new WechatAPI(ConfigEntity.appid,ConfigEntity.appsecret);
        AccessToken accessToken = wechatAPI.getAccessToken();
        System.out.println(accessToken);
    }

}
