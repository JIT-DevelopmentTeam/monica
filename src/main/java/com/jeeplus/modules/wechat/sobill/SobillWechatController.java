package com.jeeplus.modules.wechat.sobill;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.customer.entity.Customer;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.ibatis.annotations.Param;
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
    public ModelAndView goAdd(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("modules/wechat/sobill/addSobill");
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

}
