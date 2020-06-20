package com.jeeplus.modules.management.news.web;

import com.google.gson.JsonObject;
import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.WechatAPI;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
        first.put("value", "您好，你收到一条新闻公告");
        data.put("first", first);

        Map<String, Object> keyword1 = new HashMap<>();
        keyword1.put("value", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(news.getPush()));
        data.put("keyword1", keyword1);

        Map<String, Object> keyword2 = new HashMap<>();
        keyword2.put("value", "新闻公告");
        data.put("keyword2", keyword2);

        Map<String, Object> keyword3 = new HashMap<>();
        keyword3.put("value", news.getUser().getName());
        data.put("keyword3", keyword3);

        Map<String, Object> keyword4 = new HashMap<>();
        keyword4.put("value", news.getDescribe());
        data.put("keyword4", keyword4);

        Map<String, Object> keyword5 = new HashMap<>();
        keyword5.put("value", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(news.getCreateDate()));
        data.put("keyword5", keyword5);

        Map<String, Object> remark = new HashMap<>();
        remark.put("value", news.getRemarks());
        data.put("remark", remark);
        JsonObject jsonObject = wxAPI.sendTemplate(openId, templateId, "http://218.13.165.158:8069/monica/f/wechat/news/form?id=" + news.getId(), "", data, new HashMap<>());
        System.out.println("推送服务号成功：" + jsonObject);
    }
}
