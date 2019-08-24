package com.jeeplus.modules.management.messagesend.web;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.TextCard;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.TextCardEntity;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.JwMessageAPI;
import org.junit.Test;

public class TestMsg {


    @Test
    public void msgtype(){
        AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.monicaSecret);
        TextCard textCard = new TextCard();
        textCard.setTouser("LiWeiHongKong");
        // text.setToparty("678910");
        // text.setTotag("112233");
        textCard.setMsgtype("textcard");
        textCard.setAgentid(JwParamesAPI.monicaAgentid);
        TextCardEntity textCardEntity=new TextCardEntity();
        textCardEntity.setTitle("领奖通知");
        textCardEntity.setDescription("<div class=\"gray\">2019年08月16日</div> <div class=\"normal\">恭喜你抽中华为 P30一台，领奖码：wHS9t0</div><div class=\"highlight\">请于2019年08月18日前领取</div>");
        textCardEntity.setUrl("https://sale.vmall.com/huawei.html?cid=99115");
        textCardEntity.setBtntxt("详情");
        textCard.setTextcard(textCardEntity);
        textCard.setEnable_id_trans("0");
        JSONObject  jsonObject=JwMessageAPI.SendTextcardMessage(textCard, accessToken.getAccesstoken());
        System.out.println("----> "+jsonObject);
    }
}
