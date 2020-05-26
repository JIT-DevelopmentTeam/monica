package com.jeeplus.modules.wechat.wxuser;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.modules.management.customer.entity.Customer;
import com.jeeplus.modules.management.customer.service.CustomerService;
import com.jeeplus.modules.management.wxuser.entity.WxUser;
import com.jeeplus.modules.management.wxuser.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${frontPath}/wechat/wxuser")
public class WxUserWeChatController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private WxUserService wxUserService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public AjaxJson registerWxUser(String clientName, String openId){
        AjaxJson aj = new AjaxJson();
        Customer cust = customerService.getByName(clientName);
        WxUser wxuser = wxUserService.getByOpenId(openId);
        wxuser.setClient(cust);
        wxUserService.save(wxuser);
        aj.setSuccess(true);
        aj.put("user", wxuser);
        return aj;
    }
}
