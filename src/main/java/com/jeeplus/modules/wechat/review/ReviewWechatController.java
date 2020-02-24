package com.jeeplus.modules.wechat.review;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.messagesend.entity.Messagesend;
import com.jeeplus.modules.management.messagesend.service.MessagesendService;
import com.jeeplus.modules.management.messagesend.service.template.MessageTemplate;
import com.jeeplus.modules.management.messagesend.service.template.impl.MsgPass;
import com.jeeplus.modules.management.messagesend.service.template.impl.MsgReject;
import com.jeeplus.modules.management.messagesend.service.template.impl.MsgRemind;
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

    /**
     * 消息提醒
     */
    @Autowired
    private MsgRemind msgRemind;

    /**
     * 消息驳回
     */
    @Autowired
    private MsgReject msgReject;

    /**
     * 消息通过
     */
    @Autowired
    private MsgPass msgPass;

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
                        MessageTemplate msgRejectTemplate = msgReject;
                        msgRejectTemplate.send(getEmplId,title,userQyUserId,request_url,sobill.getId(),"2"); // 驳回消息
                    }
                    // 走审核下一个节点
                    if (currentApprove.getIsLast() != 1 && status != 2) {
                        OrderApprove nextApprove = orderApproveList.get(i+1);
                        nextApprove.setIsToapp(1);
                        orderApproveService.save(nextApprove);
                        /**
                         * 提醒申请人
                         */
                        MessageTemplate msgRemindTemplate=msgRemind;
                        msgRemindTemplate.send(getEmplId,title,userQyUserId,request_url,sobill.getId(),"2");// 消息发送到企业微信
                        /**
                         * 发送下一个节点申请人
                         */
                        String nextToUserId=nextApprove.getApprovalEmplId().getQyUserId();// 下一个节点接收人
                        MessageTemplate msgPassTemplate = msgPass;
                        msgPassTemplate.send(getEmplId,title,nextToUserId,request_url,sobill.getId(),"1");// 消息发送到企业微信(下一个节点审批)

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
                        MessageTemplate msgRemindTemplate=msgRemind;
                        msgRemindTemplate.send(getEmplId,title,userId,request_url,sobill.getId(),"2");// 消息发送到企业微信(提醒申请人)
                    }
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

    @RequestMapping(value = "send")
    @ResponseBody
    public void test(){
        MessageTemplate msgRemindTemplate=msgRemind;
        msgRemindTemplate.send("5b874fb83d504d598fa6809074d444c8","审批订单","LiShenWen","http://120.77.40.245:8080/monica/f/"
                ,"974b95221f2f4b37b4f7fdfec40355f9","1");

        MessageTemplate msgPassTemplate = msgPass;
        msgPassTemplate.send("5b874fb83d504d598fa6809074d444c8","审批订单","LiShenWen","http://120.77.40.245:8080/monica/f/"
                ,"974b95221f2f4b37b4f7fdfec40355f9","1");

        MessageTemplate msgRejectTemplate = msgReject;
        msgRejectTemplate.send("5b874fb83d504d598fa6809074d444c8","审批订单","LiShenWen","http://120.77.40.245:8080/monica/f/"
                ,"974b95221f2f4b37b4f7fdfec40355f9","1");
    }
}
