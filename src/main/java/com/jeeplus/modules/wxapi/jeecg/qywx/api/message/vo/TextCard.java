package com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo;

public class TextCard {

    private String touser; //成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
    private String toparty;//部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
    private String totag;  //标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数
    private String msgtype;//消息类型，此时固定为：text （支持消息型应用跟主页型应用）
    private int agentid;   //企业应用的id，整型。可在应用的设置页面查看
    private TextCardEntity textcard;// 消息卡片内容实体
    private String enable_id_trans;// 表示是否开启id转译，0表示否，1表示是，默认0

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }

    public TextCardEntity getTextcard() {
        return textcard;
    }

    public void setTextcard(TextCardEntity textcard) {
        this.textcard = textcard;
    }

    public String getEnable_id_trans() {
        return enable_id_trans;
    }

    public void setEnable_id_trans(String enable_id_trans) {
        this.enable_id_trans = enable_id_trans;
    }
}
