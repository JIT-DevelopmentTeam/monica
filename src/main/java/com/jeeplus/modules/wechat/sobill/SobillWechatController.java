package com.jeeplus.modules.wechat.sobill;

import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "${adminPath}/wechat/sobill")
public class SobillWechatController extends BaseController {

    @Autowired
    private SobillService sobillService;

    @RequestMapping(value = "list")
    public ModelAndView list(Sobill sobill) {
        ModelAndView mv = new ModelAndView();
        sobill.setDelFlag("0");
        sobill.setCheckStatus(0);
        List<Sobill> toAuditList = sobillService.findList(sobill);
        sobill.setDelFlag("0");
        sobill.setCheckStatus(1);
        List<Sobill> historyList = sobillService.findList(sobill);
        mv.addObject("toAuditList",toAuditList);
        mv.addObject("historyList",historyList);
        mv.setViewName("modules/wechat/sobill/sobill");
        return mv;
    }

    @RequestMapping(value = "goAdd")
    public ModelAndView goAdd(Sobill sobill){
        ModelAndView mv = new ModelAndView();
        mv.addObject("sobill",sobill);
        mv.setViewName("modules/wechat/sobill/sobillForm");
        return mv;
    }

}
