package com.jeeplus.modules.wechat.sobill;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.approvenode.entity.Approvenode;
import com.jeeplus.modules.management.approvenode.service.ApprovenodeService;
import com.jeeplus.modules.management.icitemclass.service.IcitemService;
import com.jeeplus.modules.management.messagesend.entity.Messagesend;
import com.jeeplus.modules.management.messagesend.service.MessagesendService;
import com.jeeplus.modules.management.orderapprove.entity.OrderApprove;
import com.jeeplus.modules.management.orderapprove.service.OrderApproveService;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.entity.Sobillentry;
import com.jeeplus.modules.management.sobillandentry.mapper.SobillentryMapper;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;
import com.jeeplus.modules.management.sobillandentry.web.SobillController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "${frontPath}/wechat/sobill")
public class SobillWechatController extends BaseController {

    @Autowired
    private SobillService sobillService;

    @Autowired
    private ApprovenodeService approvenodeService;

    @Autowired
    private OrderApproveService orderApproveService;

    // 消息发送
    @Autowired
    private MessagesendService messagesendService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "list")
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("qyUserId",request.getParameter("qyUserId"));
        mv.setViewName("modules/wechat/sobill/sobill");
        return mv;
    }

    @RequestMapping(value = "getSobillList")
    @ResponseBody
    public AjaxJson getSobillList(@Param("checkStatus") Integer checkStatus, @Param("isHistory") Integer isHistory, @Param("startPage") Integer startPage, @Param("endPage") Integer endPage, @Param("startTime") String startTime, @Param("endTime") String endTime, @RequestParam("qyUserId") String qyUserId){
        AjaxJson aj = new AjaxJson();
        Sobill sobill = new Sobill();
        sobill.setDelFlag("0");
        // 待审核
        sobill.setCheckStatus(checkStatus);
        if (isHistory != null){
            // 历史订单
            sobill.setHistory(true);
        }
        User user = userMapper.getByQyUserId(qyUserId);
        if (user != null) {
            sobill.setEmplId(user.getId());
        }
        sobill.setStartTime(startTime);
        sobill.setEndTime(endTime);
        sobill.setStartPage(startPage);
        sobill.setEndPage(endPage);
        List<Sobill> sobillList = sobillService.findList(sobill);
        if (sobillList.isEmpty()) {
            aj.setSuccess(false);
        }
        aj.put("sobillList",sobillList);
        return aj;
    }

    @RequestMapping(value = "goAdd")
    public ModelAndView goAdd(@RequestParam("qyUserId") String qyUserId){
        ModelAndView mv = new ModelAndView();
        Calendar now = Calendar.getInstance();
        Sobill sobill = new Sobill();
        int year = now.get(Calendar.YEAR);
        String month = (now.get(Calendar.MONTH) + 1) + "";
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        int secound = now.get(Calendar.SECOND);
        String time = year + month + day + hour + min + secound;
        sobill.setBillNo("SOB"+time);
        sobill.setCreateDate(new Date());
        sobill.setSynStatus(0);
        User user = userMapper.getByQyUserId(qyUserId);
        if (user != null) {
            sobill.setEmplId(user.getId());
        }
        mv.setViewName("modules/wechat/sobill/addSobill");
        mv.addObject("sobill",sobill);
        return mv;
    }

    @RequestMapping(value = "goEdit")
    public ModelAndView goEdit(@RequestParam("id") String id){
        ModelAndView mv = new ModelAndView();
        Sobill sobill = sobillService.get(id);
        StringBuffer itemIdsStr = new StringBuffer();
        for (Sobillentry sobillentry : sobill.getSobillentryList()) {
            itemIdsStr.append(sobillentry.getItemId()+",");
        }
        if (!"".equals(itemIdsStr.toString())){
            itemIdsStr = itemIdsStr.deleteCharAt(itemIdsStr.length()-1);
            mv.addObject("itemIdsStr",itemIdsStr.toString());
        }
        mv.addObject("sobill",sobill);
        mv.setViewName("modules/wechat/sobill/editSobill");
        return mv;
    }

    @RequestMapping(value = "selectItems")
    public ModelAndView selectItems(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("modules/wechat/sobill/selectItems");
        return mv;
    }

    @RequestMapping(value = "delectByIds")
    @ResponseBody
    public AjaxJson delectById(@RequestParam("idsStr") String idsStr){
        AjaxJson aj = new AjaxJson();
        String[] ids = idsStr.split(",");
        boolean delect = true;
        for (String id : ids) {
            Sobill sobill = sobillService.get(id);
            if (sobill != null && sobill.getCheckStatus() != 1 && sobill.getStatus() != 1){
                sobillService.delete(sobill);
            } else {
                delect = false;
            }
        }
        aj.setSuccess(true);
        if (!delect){
            aj.setMsg("操作成功!(部分订单已审核或已提交不允许删除!)");
        } else {
            aj.setMsg("删除订单成功!");
        }
        return aj;
    }

    @RequestMapping(value = "saveSob",produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public AjaxJson saveSob(@RequestBody Object object,HttpServletRequest request){
        String path = request.getContextPath();
        String request_url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        AjaxJson aj = new AjaxJson();
        JSONObject jsonObject = JSONObject.fromObject(object);
        if (jsonObject.get("id") == null || "".equals(jsonObject.getString("id"))){
            Sobill sobill = new Sobill();
            sobill.setBillNo(jsonObject.getString("billNo"));
            sobill.setCustId(jsonObject.getString("custId"));
            sobill.setFollowerId(jsonObject.getString("followerId"));
            sobill.setNeedTime(DateUtils.parseDate(jsonObject.getString("needTime")));
            sobill.setType(jsonObject.get("type").toString());
            sobill.setSynStatus(Integer.parseInt(jsonObject.get("synStatus").toString()));
            sobill.setStatus(Integer.parseInt(jsonObject.get("status").toString()));
            sobill.setCancellation(Integer.parseInt(jsonObject.get("cancellation").toString()));
            sobill.setEmplId(jsonObject.getString("emplId"));
            if (sobill.getStatus() == 1) {
                sobill.setCheckStatus(0);
            } else {
                sobill.setCheckStatus(2);
            }
            sobill.setRemarks(jsonObject.getString("remarks"));
            sobill.setRemark01(jsonObject.getString("remark01"));
            sobill.setRemark02(jsonObject.getString("remark02"));
            sobill.setRemark03(jsonObject.getString("remark03"));
            sobill.setRemark04(jsonObject.getString("remark04"));
            sobill.setRemark05(jsonObject.getString("remark05"));
            sobill.setRemark06(jsonObject.getString("remark06"));
            sobill.setRemark07(jsonObject.getString("remark07"));
            sobill.setRemark08(jsonObject.getString("remark08"));
            sobill.setRemark09(jsonObject.getString("remark09"));
            sobill.setRemark10(jsonObject.getString("remark10"));
            sobill.setCreateDate(DateUtils.parseDate(jsonObject.get("createDate")));
            sobill.setId(IdGen.uuid());
            sobill.setIsNewRecord(true);
            List<Sobillentry> sobillentryList = new ArrayList<>();
            JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("sobillentryList"));
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject sobillEntryObject = jsonArray.getJSONObject(i);
                Sobillentry sobillentry = new Sobillentry();
                sobillentry.setSobillId(sobill);
                sobillentry.setItemId(sobillEntryObject.getString("itemId"));
                sobillentry.setAuxqty(Double.parseDouble(sobillEntryObject.get("auxqty").toString()));
                sobillentry.setDelFlag("0");
                // id设置为空字符串才会插入数据
                sobillentry.setId("");
                sobillentryList.add(sobillentry);
            }
            sobill.setSobillentryList(sobillentryList);
            sobillService.save(sobill);
            generateApproval(sobill,request_url);
        } else {
            Sobill sobill = sobillService.get(jsonObject.getString("id"));
            if (sobill != null){
                sobill.setCustId(jsonObject.getString("custId"));
                sobill.setFollowerId(jsonObject.getString("followerId"));
                sobill.setNeedTime(DateUtils.parseDate(jsonObject.getString("needTime")));
                sobill.setType(jsonObject.get("type").toString());
                sobill.setStatus(Integer.parseInt(jsonObject.get("status").toString()));
                if (sobill.getStatus() == 1) {
                    sobill.setCheckStatus(0);
                }
                sobill.setNeedTime(DateUtils.parseDate(jsonObject.get("needTime")));
                sobill.setRemarks(jsonObject.getString("remarks"));
                sobill.setRemark01(jsonObject.getString("remark01"));
                sobill.setRemark02(jsonObject.getString("remark02"));
                sobill.setRemark03(jsonObject.getString("remark03"));
                sobill.setRemark04(jsonObject.getString("remark04"));
                sobill.setRemark05(jsonObject.getString("remark05"));
                sobill.setRemark06(jsonObject.getString("remark06"));
                sobill.setRemark07(jsonObject.getString("remark07"));
                sobill.setRemark08(jsonObject.getString("remark08"));
                sobill.setRemark09(jsonObject.getString("remark09"));
                sobill.setRemark10(jsonObject.getString("remark10"));
                List<Sobillentry> sobillentryList = sobill.getSobillentryList();
                JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("sobillentryList"));
                SobillController.checkDelect(sobillentryList, jsonArray);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject sobillEntryObject = jsonArray.getJSONObject(i);
                    boolean exist = false;
                    for (int j = 0; j < sobillentryList.size(); j++) {
                        if (sobillentryList.get(j).getItemId().equals(sobillEntryObject.getString("itemId"))){
                            exist = true;
                            sobillentryList.get(j).setAuxqty(Double.parseDouble(sobillEntryObject.get("auxqty").toString()));
                        }
                    }
                    if (!exist){
                        Sobillentry sobillentry = new Sobillentry();
                        sobillentry.setSobillId(sobill);
                        sobillentry.setItemId(sobillEntryObject.getString("itemId"));
                        sobillentry.setAuxqty(Double.parseDouble(sobillEntryObject.get("auxqty").toString()));
                        sobillentry.setDelFlag("0");
                        // id设置为空字符串才会插入数据
                        sobillentry.setId("");
                        sobillentryList.add(sobillentry);
                    }
                }
                sobillService.save(sobill);
                generateApproval(sobill,request_url);
            }
        }
        aj.setSuccess(true);
        aj.setMsg("保存成功!");
        return aj;
    }


    /**
     * 生成审批
     * @param sobill
     * @param request_url  请求路径url
     */
    private void generateApproval(Sobill sobill,String request_url){
        if (sobill.getStatus() != null && sobill.getStatus() == 1) {
            Approvenode approvenode = new Approvenode();
            approvenode.setDelFlag("0");
            approvenode.setStatus(1);
            // 订单类型
            approvenode.setType(1);
            List<Approvenode> approvenodeList = approvenodeService.findList(approvenode);
            Messagesend messagesend=null;
            for (int i = 0; i < approvenodeList.size(); i++) {
                OrderApprove orderApprove = new OrderApprove();
                orderApprove.setType(approvenodeList.get(i).getType());
                User user = new User();
                user.setId(approvenodeList.get(i).getApprovalEmplid());
                orderApprove.setApprovalEmplId(user);
                orderApprove.setIndex(approvenodeList.get(i).getIndex());
                orderApprove.setName(approvenodeList.get(i).getName());
                orderApprove.setStatus(0);
                if (i == 0) {
                    // 第一位审核人
                    orderApprove.setIsToapp(1);
                } else {
                    orderApprove.setIsToapp(0);
                }
                if (i == approvenodeList.size() - 1) {
                    // 最后一位审批人
                    orderApprove.setIsLast(1);
                } else {
                    orderApprove.setIsLast(0);
                }
                orderApprove.setSobillId(sobill);
                orderApproveService.save(orderApprove);
                if(i == 0 && orderApprove.getIsToapp()==1){
                    String title="订单审核";
                    // 获取发送给微信用户Id
                    String toUser=orderApprove.getApprovalEmplId().getQyUserId();
                    // 跳转详情url
                    request_url += Global.getConfig("frontPath");
                    // 消息发送到企业微信
                    messagesendService.messageEend(sobill.getEmplId(),title,toUser,request_url,sobill.getId(),"1");
                    Date date=new Date();
                    messagesend =new Messagesend();
                    messagesend.setFromuserId(sobill.getEmplId());
                    messagesend.setTouserId(orderApprove.getApprovalEmplId().getId());
                    messagesend.setSendTime(date);
                    messagesend.setIsRead("0");
                    messagesend.setTitle(title);
                    messagesend.setIsSend("1");
                    messagesend.setIsSendToWX("1");
                    messagesend.setSendTimeWX(date);
                    messagesendService.save(messagesend); // 保存发送消息系统表
                }
            }
        }
    }

    @RequestMapping(value = "getById")
    @ResponseBody
    public AjaxJson getById(@RequestParam("id") String id){
        AjaxJson aj = new AjaxJson();
        Sobill sobill = sobillService.get(id);
        aj.put("sobill",sobill);
        return aj;
    }

    @RequestMapping(value = "submittedList")
    @ResponseBody
    public AjaxJson submittedList(Sobill sobill,@RequestParam("qyUserId") String qyUserId){
        AjaxJson aj = new AjaxJson();
        sobill.setDelFlag("0");
        User user = userMapper.getByQyUserId(qyUserId);
        if (user != null) {
            sobill.setEmplId(user.getId());
        }
        List<Sobill> submittedList = sobillService.findSubmittedList(sobill);
        if (submittedList.isEmpty()) {
            aj.setSuccess(false);
        }
        aj.put("submittedList",submittedList);
        return aj;
    }

}
