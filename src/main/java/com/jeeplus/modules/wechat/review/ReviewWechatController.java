package com.jeeplus.modules.wechat.review;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.messagesend.entity.Messagesend;
import com.jeeplus.modules.management.messagesend.service.MessagesendService;
import com.jeeplus.modules.management.orderapprove.entity.OrderApprove;
import com.jeeplus.modules.management.orderapprove.service.OrderApproveService;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.JwMessageAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.TextCard;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.TextCardEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 订单审核
 * @author KicoChan
 */
@Controller
@RequestMapping(value = "${frontPath}/wechat/review")
public class ReviewWechatController extends BaseController {

    @Autowired
    private OrderApproveService orderApproveService;

    @Autowired
    private SobillService sobillService;

    // 消息发送
    @Autowired
    private MessagesendService messagesendService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = {"list",""})
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("modules/wechat/review/reviewList");
        mv.addObject("qyUserId",request.getParameter("qyUserId"));
        return mv;
    }

    @RequestMapping(value = "myReviewList")
    @ResponseBody
    public AjaxJson myReviewList(OrderApprove orderApprove,@RequestParam("qyUserId") String qyUserId) {
        AjaxJson aj = new AjaxJson();
        orderApprove.setDelFlag("0");
        User user = userMapper.getByQyUserId(qyUserId);
        if (user != null) {
            orderApprove.setApprovalEmplId(user);
        }
        List<OrderApprove> myReviewList = orderApproveService.findList(orderApprove);
        for (int i = 0; i < myReviewList.size(); i++) {
            if (myReviewList.get(i).getSobillId().getId() != null && !"".equals(myReviewList.get(i).getSobillId().getId())) {
                Sobill sobill = sobillService.get(myReviewList.get(i).getSobillId().getId());
                if (sobill != null) {
                    myReviewList.get(i).setSobillId(sobill);
                }
            }
        }
        if (myReviewList.isEmpty()) {
            aj.setSuccess(false);
        }
        aj.put("myReviewList",myReviewList);
        return aj;
    }

    @RequestMapping(value = "applicationDetail")
    public ModelAndView applicationDetail(@RequestParam("id") String id,@RequestParam("qyUserId") String qyUserId, Integer isApproval) {
        ModelAndView mv = new ModelAndView();
        Sobill sobill = sobillService.get(id);
        if (sobill != null) {
            if (isApproval != null) {
                sobill.setIsApproval(isApproval);
            }
            OrderApprove orderApprove = new OrderApprove();
            orderApprove.setDelFlag("0");
            orderApprove.setSobillId(sobill);
            List<OrderApprove> orderApproveList = orderApproveService.findList(orderApprove);
            mv.addObject("sobill",sobill);
            mv.addObject("orderApproveList",orderApproveList);
        }
        mv.addObject("qyUserId",qyUserId);
        mv.setViewName("modules/wechat/review/applicationDetail");
        return mv;
    }

    /**
     * 审核订单
     * @param sobillId 订单id
     * @param status 审核状态
     * @param remark 审核评议
     * @return
     */
    @RequestMapping(value = "reviewOrder")
    @ResponseBody
    public AjaxJson reviewOrder(@RequestParam("sobillId") String sobillId,@RequestParam("qyUserId") String qyUserId,@RequestParam("status") Integer status, String remark, HttpServletRequest request) {
        String path = request.getContextPath();
        String request_url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        AjaxJson aj = new AjaxJson();
        String userId = null;
        Sobill sobill = sobillService.get(sobillId);
        User loginUser = userMapper.getByQyUserId(qyUserId);
        if (loginUser != null) {
            userId = loginUser.getId();
        }
        if (sobill != null) {
            OrderApprove orderApprove = new OrderApprove();
            orderApprove.setDelFlag("0");
            orderApprove.setSobillId(sobill);
            List<OrderApprove> orderApproveList = orderApproveService.findList(orderApprove);
            boolean allow = false;
            Messagesend messagesend=null;
            for (int i = 0; i < orderApproveList.size(); i++) {
                if (orderApproveList.get(i).getApprovalEmplId().getId().equals(userId) && orderApproveList.get(i).getIsToapp() == 1) {
                    OrderApprove currentApprove = orderApproveList.get(i);
                    currentApprove.setStatus(status);
                    currentApprove.setRemark(remark);
                    currentApprove.setIsToapp(0);
                    orderApproveService.save(currentApprove);
                    String title="订单审核";
                    String toUser=sobill.getEmplId();
                    User user=new User(toUser);
                    String userQyUserId=user.getQyUserId();
                    String getEmplId=orderApproveList.get(i).getApprovalEmplId().getId(); // 发送人Id
                    request_url += Global.getConfig("frontPath");// 跳转详情url
                    if (status == 2) {
                        // 审核不通过
                        sobill.setCheckStatus(3);
                        sobillService.save(sobill);
                        // 消息发送到企业微信
                        messageEend(getEmplId,title,userQyUserId,request_url,sobill.getId(),"2"); // 驳回消息
                    }
                    // 走审核下一个节点
                    if (currentApprove.getIsLast() != 1 && status != 2) {
                        OrderApprove nextApprove = orderApproveList.get(i+1);
                        nextApprove.setIsToapp(1);
                        orderApproveService.save(nextApprove);
                        /**
                         * 提醒申请人
                         */
                        sendMessageTips(getEmplId,title,userQyUserId,request_url,sobill.getId(),"2");// 消息发送到企业微信
                        /**
                         * 发送下一个节点申请人
                         */
                        String nextToUserId=nextApprove.getApprovalEmplId().getQyUserId();// 下一个节点接收人
                        messagesendService.messageEend(getEmplId,title,nextToUserId,request_url,sobill.getId(),"1");// 消息发送到企业微信

                    }
                    // 审核已走到最后节点
                    if (currentApprove.getIsLast() == 1 && status == 1) {
                        // 审核通过
                        sobill.setCheckStatus(1);
                        sobill.setCheckTime(new Date());
                        sobillService.save(sobill);
                        /**
                         * 提醒申请人
                         */
                        sendMessageTips(getEmplId,title,userId,request_url,sobill.getId(),"2");// 消息发送到企业微信
                    }
                    /*Date date=new Date();
                    messagesend =new Messagesend();
                    messagesend.setFromuserId(sobill.getEmplId());
                    messagesend.setTouserId(orderApprove.getApprovalEmplId().getId());
                    messagesend.setSendTime(date);
                    messagesend.setIsRead("0");
                    messagesend.setTitle(title);
                    messagesend.setIsSend("1");
                    messagesend.setIsSendToWX("1");
                    messagesend.setSendTimeWX(date);
                    messagesendService.save(messagesend); // 保存发送消息系统表*/
                    allow = true;
                }
            }
            if (allow) {
                aj.setSuccess(true);
                aj.setMsg("操作成功!");
            } else {
                aj.setSuccess(false);
                aj.setMsg("操作失败!(当前节点不允许操作!)");
            }
        }
        return aj;
    }

    /**
     * 消息驳回
     * @param fromUser
     * @param titleCard
     * @param toUser
     * @param path
     * @param orderId
     * @param isApproval
     */
    public void messageEend(String fromUser,String titleCard,String toUser,String path,String orderId,String isApproval){
        AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.monicaSecret);
        TextCard textCard = new TextCard();
        User user=new User(fromUser);
        String fromUserName=user.getName();
        String title="审核信息推送" ;
        String date = DateUtils.get_yyy_MM_dd();  // 推送时间
        String description=
                "<div class=\"gray\">"+date+"</div>" +
                        "<div class=\"normal\">"+fromUserName+"驳回了你的"+titleCard+"申请</div>" +
                        "<div class=\"highlight\">驳回人:"+fromUserName+"</div>";
        String url= path+"/wechat/review/applicationDetail?id="+orderId+"isApproval="+isApproval;   // 详情请求路径--url
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
        JwMessageAPI.SendTextcardMessage(textCard, accessToken.getAccesstoken());
        return;
    }

    /**
     * 消息提醒
     * @param fromUser
     * @param titleCard
     * @param toUser
     * @param path
     * @param orderId
     * @param isApproval
     */
    public void sendMessageTips(String fromUser,String titleCard,String toUser,String path,String orderId,String isApproval){
        AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.monicaSecret);
        TextCard textCard = new TextCard();
        User user=new User(fromUser);
        String fromUserName=user.getName();
        String title="审核信息推送" ;
        String date = DateUtils.get_yyy_MM_dd();  // 推送时间
        String description=
                "<div class=\"gray\">"+date+"</div>" +
                        "<div class=\"normal\">你的"+titleCard+"申请已通过同意，请知晓</div>" +
                        "<div class=\"highlight\">发送人："+fromUserName+"</div>";
        String url= path+"/wechat/review/applicationDetail?id="+orderId+"isApproval="+isApproval;   // 详情请求路径--url
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
        JwMessageAPI.SendTextcardMessage(textCard, accessToken.getAccesstoken());
        return;
    }
}
