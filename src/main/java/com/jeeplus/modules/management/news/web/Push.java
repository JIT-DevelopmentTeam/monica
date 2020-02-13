package com.jeeplus.modules.management.news.web;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.JwMessageAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.NewsArticle;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.NewsEntity;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Push {



    /**
     * 新闻公告推送
     * @param newsData 新闻消息对象
     * @param toUser   推送用户Id
     */
    public static void captainSendLoggingData(String ip, String port, News newsData, String toUser) {
        // 获取请求协议
        String messageServerUrl = Global.getConfig("messageServerUrl");
        System.out.println("messageServerUrl : "+messageServerUrl);
        String filePath =  "http://" + messageServerUrl;
        // 获取Token
        AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.monicaSecret);
        com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.News news = new com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.News();
        news.setTouser(toUser);//成员ID列表
        //news.setToparty("");     //部门ID列表
        //news.setTotag("");       //标签ID列表
        news.setMsgtype("news");   //消息类型
        news.setAgentid(JwParamesAPI.monicaAgentid);      //企业应用的id
        NewsArticle newsArticles = new NewsArticle();
        newsArticles.setUrl(filePath+"/f/wechat/news/form?id="+newsData.getId()); //点击后跳转的链接
        newsArticles.setTitle(newsData.getTitle());       //推送标题
        String picUrl = filePath + newsData.getMainpic();  //图片url
        newsArticles.setPicurl(picUrl);         // 新闻封面
        NewsEntity newsEntity=new NewsEntity();
        newsEntity.setArticles(new NewsArticle[] {newsArticles});
        news.setNews(newsEntity);
        JSONObject result = JwMessageAPI.sendNewsMessage(news, accessToken.getAccesstoken());
        System.out.println("推送成功："+result);
    }
}
