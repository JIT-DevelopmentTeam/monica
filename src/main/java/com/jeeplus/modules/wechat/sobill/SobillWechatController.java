package com.jeeplus.modules.wechat.sobill;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "${frontPath}/wechat/sobill")
public class SobillWechatController extends BaseController {

    @Autowired
    private SobillService sobillService;

    @RequestMapping(value = "list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView();
        Sobill toAuditSobill = new Sobill();
        toAuditSobill.setDelFlag("0");
        toAuditSobill.setCheckStatus(0);
        toAuditSobill.setStartPage(0);
        toAuditSobill.setEndPage(2);
        List<Sobill> toAuditList = sobillService.findList(toAuditSobill);
        Sobill historySobill = new Sobill();
        historySobill.setDelFlag("0");
        historySobill.setStartPage(0);
        historySobill.setEndPage(2);
        historySobill.setCheckStatus(1);
        List<Sobill> historyList = sobillService.findList(historySobill);
        mv.addObject("toAuditList",toAuditList);
        mv.addObject("historyList",historyList);
        mv.addObject("toAuditSobill",toAuditSobill);
        mv.addObject("historySobill",historySobill);
        mv.setViewName("modules/wechat/sobill/sobill");
        return mv;
    }

    @RequestMapping(value = "getSobillListForPage")
    @ResponseBody
    public AjaxJson getSobillListForPage(Sobill sobill){
        AjaxJson aj = new AjaxJson();
        sobill.setDelFlag("0");
        List<Sobill> sobillList = sobillService.findList(sobill);
        aj.put("sobillList",sobillList);
        return aj;
    }

    @RequestMapping(value = "goAdd")
    public ModelAndView goAdd(Sobill sobill){
        ModelAndView mv = new ModelAndView();
        mv.addObject("sobill",sobill);
        mv.setViewName("modules/wechat/sobill/sobillForm");
        return mv;
    }

}
