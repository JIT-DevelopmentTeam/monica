package com.jeeplus.modules.wechat.sobill;
import com.google.common.collect.Lists;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;
import com.jeeplus.modules.management.icitemclass.service.IcitemService;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.ibatis.annotations.Param;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping(value = "${frontPath}/wechat/sobill")
public class SobillWechatController extends BaseController {

    @Autowired
    private SobillService sobillService;

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

    @RequestMapping(value = "checkSobillById")
    @ResponseBody
    public AjaxJson checkSobill(@Param("id") String id){
        AjaxJson aj = new AjaxJson();
        User user = UserUtils.getUser();
        Sobill sobill = sobillService.get(id);
        if (sobill.getCheckStatus() != 1 && sobill.getStatus() != 1){
            // 待审核和未提交状态允许审核
            sobill.setCheckerId(user.getId());
            sobill.setCheckTime(new Date());
            sobill.setCheckStatus(1);
            sobillService.checkOrder(sobill);
            aj.setSuccess(true);
            aj.setMsg("审核成功!");
        } else {
            aj.setSuccess(false);
            aj.setMsg("审核失败!(请检查该订单是否已审核或已提交状态)");
        }
        return aj;
    }

}
