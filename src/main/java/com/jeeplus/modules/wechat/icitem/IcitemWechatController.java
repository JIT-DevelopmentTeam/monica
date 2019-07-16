package com.jeeplus.modules.wechat.icitem;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;
import com.jeeplus.modules.management.icitemclass.entity.IcitemClass;
import com.jeeplus.modules.management.icitemclass.service.IcitemClassService;
import com.jeeplus.modules.management.icitemclass.service.IcitemService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 企业微信商品资料Controller
 * @author JiaChe
 * @version 2019-05-29
 */
@Controller
@RequestMapping(value = "${frontPath}/wechat/icitem")
public class IcitemWechatController {

    @Autowired
    private IcitemClassService icitemClassService;

    @Autowired
    private IcitemService icitemService;

    @RequestMapping(value = "getItemsListByClassId")
    @ResponseBody
    public AjaxJson getItemsListByClassId(Icitem icitem){
        AjaxJson aj = new AjaxJson();
        icitem.setDelFlag("0");
        List<Icitem> icitemList = icitemService.findList(icitem);
        aj.put("icitemList",icitemList);
        return aj;
    }

    @RequestMapping(value = "getItemClass")
    @ResponseBody
    public AjaxJson getItemClass(IcitemClass icitemClass){
        AjaxJson aj = new AjaxJson();
        icitemClass.setDelFlag("0");
        List<IcitemClass> icitemClassList = icitemClassService.findListForWechat(icitemClass);
        aj.put("icitemClassList",icitemClassList);
        return aj;
    }

    @RequestMapping(value = "getListByName")
    @ResponseBody
    public AjaxJson getListByName(@Param("name") String name){
        AjaxJson aj = new AjaxJson();
        Icitem icitem = new Icitem();
        icitem.setDelFlag("0");
        icitem.setName(name.trim());
        List<Icitem> icitemList = icitemService.findList(icitem);
        aj.put("icitemList",icitemList);
        return aj;
    }

    @RequestMapping(value = "findItemListByIds")
    @ResponseBody
    public AjaxJson findItemListByIds(@RequestParam(value = "idsStr") String idsStr){
        LinkedHashMap<String, Object> body = new LinkedHashMap<String, Object>();
        Icitem icitem = new Icitem();
        AjaxJson aj = new AjaxJson();
        String[] ids = idsStr.split(",");
        StringBuffer idsSql = new StringBuffer();
        for (String id : ids) {
            idsSql.append("'"+id+"',");
        }
        idsSql = idsSql.deleteCharAt(idsSql.length()-1);
        icitem.setIds(idsSql.toString());
        List<Icitem> icitemList = icitemService.findList(icitem);
        body.put("icitemList",icitemList);
        aj.setBody(body);
        return aj;
    }

}
