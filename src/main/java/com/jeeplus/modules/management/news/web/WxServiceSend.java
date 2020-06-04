package com.jeeplus.modules.management.news.web;

import com.google.gson.JsonObject;
import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.WechatAPI;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WxServiceSend {

    public static void send(News news, String openId) {
        String appid = "";
        String appsecret = "";
        String templateId = "";
        try {
            appid = PropertiesLoaderUtils.loadAllProperties(("wechat/config.properties")).getProperty("wechat_appid");
            appsecret = PropertiesLoaderUtils.loadAllProperties(("wechat/config.properties")).getProperty("wechat_appsecret");
            templateId = PropertiesLoaderUtils.loadAllProperties(("wechat/config.properties")).getProperty("wechat_template_id");
        } catch (IOException e) {
            e.printStackTrace();
        }
        WechatAPI wxAPI = new WechatAPI(appid, appsecret);
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> first = new HashMap<>();
        first.put("value", news.getTitle());
        data.put("first", first);

        Map<String, Object> keyword1 = new HashMap<>();
        keyword1.put("value", "莫尔卡");
        data.put("keyword1", keyword1);

        Map<String, Object> keyword2 = new HashMap<>();
        keyword2.put("value", news.getDescribe());
        data.put("keyword2", keyword2);

        Map<String, Object> keyword3 = new HashMap<>();
        keyword3.put("value", news.getPush());
        data.put("keyword3", keyword3);

        Map<String, Object> keyword4 = new HashMap<>();
        keyword4.put("value", "");
        data.put("keyword4", keyword4);

        Map<String, Object> keyword5 = new HashMap<>();
        keyword5.put("value", "");
        data.put("keyword5", keyword5);

        Map<String, Object> remark = new HashMap<>();
        remark.put("value", "阅读次数：" + news.getReadCount());
        data.put("remark", remark);
        JsonObject jsonObject = wxAPI.sendTemplate(openId, templateId, "https://www.baidu.com", "", data, new HashMap<>());
        System.out.println("推送服务号成功：" + jsonObject);
    }
}
