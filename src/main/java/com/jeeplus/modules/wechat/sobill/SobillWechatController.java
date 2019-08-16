package com.jeeplus.modules.wechat.sobill;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.icitemclass.service.IcitemService;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.entity.Sobillentry;
import com.jeeplus.modules.management.sobillandentry.mapper.SobillentryMapper;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;
import com.jeeplus.modules.management.sobillandentry.web.SobillController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
    private SobillentryMapper sobillentryMapper;

    @Autowired
    private IcitemService icitemService;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("modules/wechat/sobill/sobill");
        return mv;
    }

    @RequestMapping(value = "getSobillList")
    @ResponseBody
    public AjaxJson getSobillList(@Param("checkStatus") Integer checkStatus, @Param("isHistory") Integer isHistory, @Param("startPage") Integer startPage,@Param("endPage") Integer endPage, @Param("startTime") String startTime,@Param("endTime") String endTime){
        AjaxJson aj = new AjaxJson();
        Sobill sobill = new Sobill();
        sobill.setDelFlag("0");
        // 待审核
        sobill.setCheckStatus(checkStatus);
        if (isHistory != null){
            // 历史订单
            sobill.setHistory(true);
        }
        sobill.setStartTime(startTime);
        sobill.setEndTime(endTime);
        sobill.setStartPage(startPage);
        sobill.setEndPage(endPage);
        List<Sobill> sobillList = sobillService.findList(sobill);
        aj.put("sobillList",sobillList);
        return aj;
    }

    @RequestMapping(value = "goAdd")
    public ModelAndView goAdd(Sobill sobill){
        ModelAndView mv = new ModelAndView();
        Calendar now = Calendar.getInstance();
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

    @RequestMapping(value = "checkSobillByIds")
    @ResponseBody
    public AjaxJson checkSobill(@RequestParam("idsStr") String idsStr){
        AjaxJson aj = new AjaxJson();
        String[] ids = idsStr.split(",");
        boolean check = true;
        for (String id : ids) {
            Sobill sobill = sobillService.get(id);
            if (sobill != null && sobill.getCheckStatus() != 1 && sobill.getStatus() != 1){
                /* TODO 审核人 */
                sobill.setCheckTime(new Date());
                sobill.setCheckStatus(1);
                sobillService.save(sobill);
            } else {
                check = false;
            }
        }
        aj.setSuccess(true);
        if (check){
            aj.setMsg("审核成功!");
        } else {
            aj.setMsg("操作成功!(部分订单已审核或已提交不允许操作!)");
        }
        return aj;
    }

    @RequestMapping(value = "saveSob",produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public AjaxJson saveSob(@RequestBody Object object){
        AjaxJson aj = new AjaxJson();
        JSONObject jsonObject = JSONObject.fromObject(object);
        if (jsonObject.getString("id") == null || "".equals(jsonObject.getString("id"))){
            Sobill sobill = new Sobill();
            sobill.setBillNo(jsonObject.getString("billNo"));
            sobill.setCustId(jsonObject.getString("custId"));
            sobill.setNeedTime(DateUtils.parseDate(jsonObject.getString("needTime")));
            sobill.setType(Integer.parseInt(jsonObject.get("type").toString()));
            sobill.setSynStatus(Integer.parseInt(jsonObject.get("synStatus").toString()));
            sobill.setStatus(Integer.parseInt(jsonObject.get("status").toString()));
            sobill.setCancellation(Integer.parseInt(jsonObject.get("cancellation").toString()));
            sobill.setCheckStatus(Integer.parseInt(jsonObject.get("checkStatus").toString()));
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
        } else {
            Sobill sobill = sobillService.get(jsonObject.getString("id"));
            if (sobill != null){
                sobill.setCustId(jsonObject.getString("custId"));
                sobill.setNeedTime(DateUtils.parseDate(jsonObject.getString("needTime")));
                sobill.setType(Integer.parseInt(jsonObject.get("type").toString()));
                sobill.setStatus(Integer.parseInt(jsonObject.get("status").toString()));
                sobill.setNeedTime(DateUtils.parseDate(jsonObject.get("needTime")));
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
            }
            sobillService.save(sobill);
        }
        aj.setSuccess(true);
        aj.setMsg("保存成功!");
        return aj;
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
    public AjaxJson submittedList(Sobill sobill){
        AjaxJson aj = new AjaxJson();
        sobill.setDelFlag("0");
        /* TODO 后续获取微信登录用户 */
        sobill.setCreateBy(UserUtils.getUser());
        List<Sobill> submittedList = sobillService.findSubmittedList(sobill);
        aj.put("submittedList",submittedList);
        return aj;
    }

}
