package com.jeeplus.modules.wechat.review;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.orderapprove.entity.OrderApprove;
import com.jeeplus.modules.management.orderapprove.service.OrderApproveService;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping(value = {"list",""})
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("modules/wechat/review/reviewList");
        return mv;
    }

    @RequestMapping(value = "myReviewList")
    @ResponseBody
    public AjaxJson myReviewList(OrderApprove orderApprove) {
        AjaxJson aj = new AjaxJson();
        orderApprove.setDelFlag("0");
        /* TODO 后续获取微信登录用户信息 */
        orderApprove.setApprovalEmplId(UserUtils.getUser());
        List<OrderApprove> myReviewList = orderApproveService.findList(orderApprove);
        for (int i = 0; i < myReviewList.size(); i++) {
            if (myReviewList.get(i).getSobillId().getId() != null && !"".equals(myReviewList.get(i).getSobillId().getId())) {
                Sobill sobill = sobillService.get(myReviewList.get(i).getSobillId().getId());
                if (sobill != null) {
                    myReviewList.get(i).setSobillId(sobill);
                }
            }
        }
        aj.put("myReviewList",myReviewList);
        return aj;
    }

    @RequestMapping(value = "applicationDetail")
    public ModelAndView applicationDetail(@RequestParam("id") String id, Integer isApproval) {
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
    public AjaxJson reviewOrder(@RequestParam("sobillId") String sobillId,@RequestParam("status") Integer status, String remark) {
        AjaxJson aj = new AjaxJson();
        Sobill sobill = sobillService.get(sobillId);
        if (sobill != null) {
            OrderApprove orderApprove = new OrderApprove();
            orderApprove.setDelFlag("0");
            orderApprove.setSobillId(sobill);
            List<OrderApprove> orderApproveList = orderApproveService.findList(orderApprove);
            boolean allow = false;
            for (int i = 0; i < orderApproveList.size(); i++) {
                /* TODO 微信登录用户 */
                if (orderApproveList.get(i).getApprovalEmplId().getId().equals(UserUtils.getUser().getId()) && orderApproveList.get(i).getIsToapp() == 1) {
                    OrderApprove currentApprove = orderApproveList.get(i);
                    currentApprove.setStatus(status);
                    currentApprove.setRemark(remark);
                    currentApprove.setIsToapp(0);
                    orderApproveService.save(currentApprove);
                    if (status == 2) {
                        // 审核不通过
                        sobill.setCheckStatus(3);
                        sobillService.save(sobill);
                    }
                    if (currentApprove.getIsLast() != 1 && status != 2) {
                        OrderApprove nextApprove = orderApproveList.get(i+1);
                        nextApprove.setIsToapp(1);
                        orderApproveService.save(nextApprove);
                    }
                    if (currentApprove.getIsLast() == 1 && status == 1) {
                        // 审核通过
                        sobill.setCheckStatus(1);
                        sobill.setCheckTime(new Date());
                        sobillService.save(sobill);
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

}
