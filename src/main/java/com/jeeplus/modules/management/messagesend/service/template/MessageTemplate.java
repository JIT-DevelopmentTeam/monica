package com.jeeplus.modules.management.messagesend.service.template;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.JwMessageAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.TextCard;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.TextCardEntity;

public abstract  class MessageTemplate {

    /**
     * 获取用户名称
     * @param fromUser
     * @return
     */
    protected abstract String fromUser(String fromUser);

    /**
     * 描述
     * @param fromUser
     * @param date
     * @param titleCard
     * @return
     */
    protected abstract String description(String fromUser,String date,String titleCard);

    //模板
    public JSONObject send(String fromUser,String titleCard,String toUser,String path,String orderId,String isApproval){
        String fromUserName=fromUser(fromUser);
        String title="审核信息推送";
        String date = DateUtils.get_yyy_MM_dd();  // 推送时间
        String description=description(fromUserName,date,titleCard);
        String url= path + "/wechat/review/applicationDetail?id="+orderId+"&isApproval="+isApproval;   // 详情请求路径--url
        JSONObject jsonObject =sendTextcardMsg(title,description,toUser,url);
        return jsonObject;
    }

    private JSONObject sendTextcardMsg(String title,String description,String toUser,String url){
        AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.monicaSecret);
        TextCard textCard = new TextCard();
        textCard.setTouser(toUser);     // 接收人
        textCard.setMsgtype("textcard");  // 消息类型
        textCard.setAgentid(JwParamesAPI.monicaAgentid);   // 企业微信的应用agentId
        TextCardEntity textCardEntity=new TextCardEntity(); //消息文本对象
        textCardEntity.setTitle(title);
        textCardEntity.setDescription(description);
        textCardEntity.setUrl(url);
        textCardEntity.setBtntxt("详情");
        textCard.setTextcard(textCardEntity);
        textCard.setEnable_id_trans("0");
        JSONObject jsonObject = JwMessageAPI.SendTextcardMessage(textCard, accessToken.getAccesstoken());
        return jsonObject;
    }
}
