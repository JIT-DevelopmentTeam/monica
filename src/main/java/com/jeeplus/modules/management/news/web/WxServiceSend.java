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
        data.put("first", news.getTitle());
        data.put("keyword1", "莫尔卡");
        data.put("keyword2", news.getDescribe());
        data.put("keyword3", news.getPush());
        data.put("keyword4", "");
        data.put("keyword5", "");
        data.put("remark", "阅读次数：" + news.getReadCount());
        JsonObject jsonObject = wxAPI.sendTemplate(openId, templateId, "https://www.baidu.com", "", data, new HashMap<>());
        System.out.println("推送服务号成功：" + jsonObject);
    }
}
