package com.jeeplus.modules.wechat.review;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.ListUtil;
import com.jeeplus.common.utils.collection.MapUtil;
import com.jeeplus.common.utils.k3.K3SOUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.customer.entity.Customer;
import com.jeeplus.modules.management.customer.service.CustomerService;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;
import com.jeeplus.modules.management.icitemclass.service.IcitemService;
import com.jeeplus.modules.management.messagesend.entity.Messagesend;
import com.jeeplus.modules.management.messagesend.service.MessagesendService;
import com.jeeplus.modules.management.messagesend.service.template.MessageTemplate;
import com.jeeplus.modules.management.messagesend.service.template.impl.MsgPass;
import com.jeeplus.modules.management.messagesend.service.template.impl.MsgReject;
import com.jeeplus.modules.management.messagesend.service.template.impl.MsgRemind;
import com.jeeplus.modules.management.orderapprove.entity.OrderApprove;
import com.jeeplus.modules.management.orderapprove.service.OrderApproveService;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.entity.Sobillentry;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;
import com.jeeplus.modules.sys.entity.DictType;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.service.DictTypeService;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    private DictTypeService dictTypeService;

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

    @Autowired
    private IcitemService icitemService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OfficeService officeService;

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
        DictType sobillType = new DictType();
        sobillType.setType("sobill_type");
        List<DictType> sobillTypeDictType = dictTypeService.findList(sobillType);
        List<DictValue> sobillTypeList = dictTypeService.get(sobillTypeDictType.get(0).getId()).getDictValueList();
        DictType sobillRemark01 = new DictType();
        sobillRemark01.setType("sobill_remark01");
        List<DictType> sobillRemark01DictType = dictTypeService.findList(sobillRemark01);
        List<DictValue> sobillRemark01List = dictTypeService.get(sobillRemark01DictType.get(0).getId()).getDictValueList();
        DictType sobillRemark02 = new DictType();
        sobillRemark02.setType("sobill_remark02");
        List<DictType> sobillRemark02DictType = dictTypeService.findList(sobillRemark02);
        List<DictValue> sobillRemark02List = dictTypeService.get(sobillRemark02DictType.get(0).getId()).getDictValueList();
        DictType sobillRemark03 = new DictType();
        sobillRemark03.setType("sobill_remark03");
        List<DictType> sobillRemark03DictType = dictTypeService.findList(sobillRemark03);
        List<DictValue> sobillRemark03List = dictTypeService.get(sobillRemark03DictType.get(0).getId()).getDictValueList();
        DictType sobillRemark04 = new DictType();
        sobillRemark04.setType("sobill_remark04");
        List<DictType> sobillRemark04DictType = dictTypeService.findList(sobillRemark04);
        List<DictValue> sobillRemark04List = dictTypeService.get(sobillRemark04DictType.get(0).getId()).getDictValueList();
        DictType sobillRemark05 = new DictType();
        sobillRemark05.setType("sobill_remark05");
        List<DictType> sobillRemark05DictType = dictTypeService.findList(sobillRemark05);
        List<DictValue> sobillRemark05List = dictTypeService.get(sobillRemark05DictType.get(0).getId()).getDictValueList();
        DictType sobillRemark06 = new DictType();
        sobillRemark06.setType("sobill_remark06");
        List<DictType> sobillRemark06DictType = dictTypeService.findList(sobillRemark06);
        List<DictValue> sobillRemark06List = dictTypeService.get(sobillRemark06DictType.get(0).getId()).getDictValueList();
        DictType sobillRemark07 = new DictType();
        sobillRemark07.setType("sobill_remark07");
        List<DictType> sobillRemark07DictType = dictTypeService.findList(sobillRemark07);
        List<DictValue> sobillRemark07List = dictTypeService.get(sobillRemark07DictType.get(0).getId()).getDictValueList();
        DictType sobillRemark08 = new DictType();
        sobillRemark08.setType("sobill_remark08");
        List<DictType> sobillRemark08DictType = dictTypeService.findList(sobillRemark08);
        List<DictValue> sobillRemark08List = dictTypeService.get(sobillRemark08DictType.get(0).getId()).getDictValueList();
        mv.addObject("qyUserId",qyUserId);
        mv.addObject("sobillTypeList",sobillTypeList);
        mv.addObject("sobillRemark01List",sobillRemark01List);
        mv.addObject("sobillRemark02List",sobillRemark02List);
        mv.addObject("sobillRemark03List",sobillRemark03List);
        mv.addObject("sobillRemark04List",sobillRemark04List);
        mv.addObject("sobillRemark05List",sobillRemark05List);
        mv.addObject("sobillRemark06List",sobillRemark06List);
        mv.addObject("sobillRemark07List",sobillRemark07List);
        mv.addObject("sobillRemark08List",sobillRemark08List);
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
        String request_url = request.getScheme()+"://"+request.getServerName()+path+"/";
        AjaxJson aj = new AjaxJson();
        String userId = null;
        Sobill sobill = sobillService.get(sobillId);
        User loginUser = userMapper.getByQyUserId(qyUserId);
        if (loginUser != null) {
            userId = loginUser.getId();
        }
        if (sobill != null) {
            OrderApprove orderApprove = new OrderApprove();
            orderApprove.setSobillId(sobill);
            List<OrderApprove> orderApproveList = orderApproveService.findList(orderApprove);
            boolean allow = false;
            Messagesend messagesend=null;
            if (orderApproveList.size() == 1) {
                if (orderApproveList.get(0).getStatus() == 0) {
                    OrderApprove currentApprove = orderApproveList.get(0);
                    currentApprove.setStatus(status);
                    currentApprove.setRemark(remark);
                    currentApprove.setIsToapp(0);
                    orderApproveService.save(currentApprove);
                    allow = true;
                    switch (status) {
                        case 1:
                            JSONObject jsonObject = syncOrder(sobill);
                            if (!ObjectUtils.isEmpty(jsonObject.get("StatusCode")) && "200".equals(jsonObject.getString("StatusCode"))) {
                                JSONObject dataJson = jsonObject.getJSONObject("Data");
                                // 审核通过
                                sobill.setCheckStatus(1);
                                sobill.setCheckerId(userId);
                                sobill.setCheckTime(new Date());
                                sobill.setErpCode(dataJson.get("BillNo").toString());
                                sobill.setSynStatus(1);
                                sobill.setSynTime(new Date());
                                sobillService.save(sobill);
                            } else {
                                allow = false;
                            }
                            break;
                        case 2:
                            // 审核不通过
                            sobill.setCheckStatus(3);
                            sobillService.save(sobill);
                            break;
                    }
                }
            } else {
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
                            break;
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
                            allow = true;
                            // 同步成功后才允许审核
                            JSONObject jsonObject = syncOrder(sobill);
                            if (!ObjectUtils.isEmpty(jsonObject.get("StatusCode")) && "200".equals(jsonObject.getString("StatusCode"))) {
                                JSONObject dataJson = jsonObject.getJSONObject("Data");
                                // 审核通过
                                sobill.setCheckStatus(1);
                                sobill.setCheckerId(userId);
                                sobill.setCheckTime(new Date());
                                sobill.setErpCode(dataJson.get("BillNo").toString());
                                sobill.setSynStatus(1);
                                sobill.setSynTime(new Date());
                                sobillService.save(sobill);
                                /**
                                 * 提醒申请人
                                 */
                                MessageTemplate msgRemindTemplate=msgRemind;
                                msgRemindTemplate.send(getEmplId,title,userQyUserId,request_url,sobill.getId(),"2");// 消息发送到企业微信(提醒申请人)
                            } else {
                                allow = false;
                            }
                        }
                    }
                }
            }
            if (allow) {
                aj.setSuccess(true);
                aj.setMsg("操作成功!");
            } else {
                aj.setSuccess(false);
                aj.setMsg("操作失败!");
            }
        }
        return aj;
    }

    @RequestMapping(value = "send")
    @ResponseBody
    public void test(){
        MessageTemplate msgRemindTemplate=msgRemind;
        msgRemindTemplate.send("5b874fb83d504d598fa6809074d444c8","审批订单","LiWeiHongKong","http://monica.justinit.cn/monica/f/"
                ,"974b95221f2f4b37b4f7fdfec40355f9","1");

        MessageTemplate msgPassTemplate = msgPass;
        msgPassTemplate.send("5b874fb83d504d598fa6809074d444c8","审批订单","LiWeiHongKong","http://monica.justinit.cn/monica/f/"
                ,"974b95221f2f4b37b4f7fdfec40355f9","1");

        MessageTemplate msgRejectTemplate = msgReject;
        msgRejectTemplate.send("5b874fb83d504d598fa6809074d444c8","审批订单","LiWeiHongKong","http://monica.justinit.cn/monica/f/"
                ,"974b95221f2f4b37b4f7fdfec40355f9","1");
    }

    /**
     * 同步订单
     * @param sobill
     */
    private JSONObject syncOrder(Sobill sobill) {
        Map<String,Object> allDataMap = MapUtil.newHashMap();
        Map<String,Object> dataMap = MapUtil.newHashMap();
        List<Map<String,Object>> page1List = ListUtil.newArrayList();
        Map<String,Object> page1Map = MapUtil.newHashMap();
        List<Map<String,Object>> page2List = ListUtil.newArrayList();
        page1Map.put("FCancellation","0");
        page1Map.put("Fdate",DateUtils.formatDateTime(sobill.getCreateDate()));
        Customer customer = customerService.get(sobill.getCustId());
        if (!ObjectUtils.isEmpty(customer)) {
            Map<String,String> custMap = MapUtil.newHashMap();
            custMap.put("FNumber",customer.getId());
            custMap.put("FName",customer.getName());
            page1Map.put("FCustID",custMap);
        }
        User user = userMapper.get(sobill.getEmplId());
        Office office = officeService.get(user.getOffice().getId());
        Map<String,Object> deptIDMap = new HashMap<>();
        deptIDMap.put("FNumber",office.getErpDeptNumber());
        deptIDMap.put("FName",office.getErpDeptName());
        page1Map.put("FDeptID",deptIDMap);
        Map<String,Object> empIDMap = new HashMap<>();
        empIDMap.put("FNumber",user.getErpUserNumber());
        empIDMap.put("FName",user.getErpUserName());
        page1Map.put("FEmpID",empIDMap);
        if (StringUtils.isNotBlank(sobill.getErpCode())) {
            page1Map.put("FBillNo",sobill.getErpCode());
        }
        page1List.add(page1Map);
        for (Sobillentry sobillentry : sobill.getSobillentryList()) {
            Map<String,Object> page2Map = MapUtil.newHashMap();
            Map<String,Object> unitIDMap = MapUtil.newHashMap();
            unitIDMap.put("FNumber","004");
            unitIDMap.put("FName","个");
            page2Map.put("FUnitID",unitIDMap);
            page2Map.put("FDate1",DateUtils.formatDateTime(sobill.getNeedTime()));
            Icitem item = icitemService.get(sobillentry.getItemId());
            if (!ObjectUtils.isEmpty(item)) {
                Map<String,Object> itemIDMap = MapUtil.newHashMap();
                itemIDMap.put("FNumber",item.getNumber());
                itemIDMap.put("FName",item.getName());
                page2Map.put("FItemID",itemIDMap);
                page2Map.put("FItemName",item.getName());
                page2Map.put("FItemModel",item.getModel());
                page2Map.put("FBaseUnit",item.getUnit());
            }
            page2Map.put("Fauxqty",sobillentry.getAuxqty().toString());
            if (sobillentry.getPrice() != null) {
                page2Map.put("Fauxprice",sobillentry.getPrice().toString());
            }
            if (sobillentry.getAmount() != null) {
                page2Map.put("Famount",sobillentry.getAmount().toString());
            }
            if (StringUtils.isNotBlank(sobillentry.getRemarks())) {
                page2Map.put("Fnote",sobillentry.getRemarks());
            }
            page2List.add(page2Map);
        }
        dataMap.put("Page1",page1List);
        dataMap.put("Page2",page2List);
        if (StringUtils.isNotBlank(sobill.getErpCode())) {
            allDataMap.put("FBillNo",sobill.getErpCode());
        }
        allDataMap.put("Data",dataMap);
        JSONObject jsonObject = K3SOUtils.saveSO(allDataMap);
        return jsonObject;
    }

}
