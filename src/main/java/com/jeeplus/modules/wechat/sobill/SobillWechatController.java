package com.jeeplus.modules.wechat.sobill;
import com.jeeplus.common.utils.IdGen;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.icitemclass.service.IcitemService;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.entity.Sobillentry;
import com.jeeplus.modules.management.sobillandentry.mapper.SobillentryMapper;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;
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

    @RequestMapping(value = "getSobillListByCheckStatus")
    @ResponseBody
    public AjaxJson getSobillListByCheckStatus(@Param("checkStatus") Integer checkStatus,@Param("startPage") Integer startPage,@Param("endPage") Integer endPage){
        AjaxJson aj = new AjaxJson();
        Sobill sobill = new Sobill();
        sobill.setDelFlag("0");
        sobill.setCheckStatus(checkStatus);
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

    @RequestMapping(value = "delectById")
    @ResponseBody
    public AjaxJson delectById(@Param("id") String id){
        AjaxJson aj = new AjaxJson();
        Sobill sobill = new Sobill();
        sobill.setId(id);
        sobill = sobillService.get(sobill);
        if (sobill.getStatus() == 1 || sobill.getCheckStatus() == 1){
            aj.setSuccess(false);
            aj.setMsg("删除失败!(已提交或已审核订单不允许删除)");
        } else {
            sobillService.delete(sobill);
            aj.setSuccess(true);
            aj.setMsg("删除数据成功!");
        }
        return aj;
    }

    @RequestMapping(value = "checkSobill")
    @ResponseBody
    public AjaxJson checkSobill(@RequestParam("id") String id){
        AjaxJson aj = new AjaxJson();
        Sobill sobill = sobillService.get(id);
        if (sobill.getCheckStatus() != 1){
            // 待审核和未提交状态允许审核
            /* TODO 审核人 */
            sobill.setCheckTime(new Date());
            sobill.setCheckStatus(1);
            sobillService.save(sobill);
            aj.setSuccess(true);
            aj.setMsg("审核成功!");
        } else {
            aj.setSuccess(false);
            aj.setMsg("审核失败!(请检查该订单是否已审核!)");
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
            sobill.setSynStatus(Integer.parseInt(jsonObject.get("synStatus").toString()));
            sobill.setStatus(Integer.parseInt(jsonObject.get("status").toString()));
            sobill.setCancellation(Integer.parseInt(jsonObject.get("cancellation").toString()));
            sobill.setCheckStatus(Integer.parseInt(jsonObject.get("checkStatus").toString()));
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
                sobill.setSynStatus(Integer.parseInt(jsonObject.get("synStatus").toString()));
                sobill.setStatus(Integer.parseInt(jsonObject.get("status").toString()));
                sobill.setCancellation(Integer.parseInt(jsonObject.get("cancellation").toString()));
                sobill.setCheckStatus(Integer.parseInt(jsonObject.get("checkStatus").toString()));
                List<Sobillentry> sobillentryList = sobill.getSobillentryList();
                JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("sobillentryList"));
                for (int i = 0; i < sobillentryList.size(); i++) {
                    boolean delect = true;
                    for (int j = 0; j < jsonArray.size(); j++) {
                        JSONObject sobillEntryObject = jsonArray.getJSONObject(j);
                        if (sobillEntryObject.getString("itemId").equals(sobillentryList.get(i).getItemId())){
                            delect = false;
                        }
                    }
                    if (delect) {
                        sobillentryList.get(i).setDelFlag("1");
                    }
                }
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

}
