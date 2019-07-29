package com.jeeplus.modules.wechat.customer;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.customer.entity.Customer;
import com.jeeplus.modules.management.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping(value = "${frontPath}/wechat/customer")
public class CustomerWechatController extends BaseController {

    @Autowired
    private CustomerService customerService;

    /**
     * 根据员工id获取客户列表
     * @return
     */
    @RequestMapping(value = "getCustomerListByEmpId")
    @ResponseBody
    public AjaxJson getCustomerListByEmpId(){
        /* TODO 后期根据员工id获取数据 */
        AjaxJson aj = new AjaxJson();
        Customer customer = new Customer();
        customer.setDelFlag("0");
        List<Customer> customerList = customerService.findList(customer);
        aj.put("customerList",customerList);
        return aj;
    }

}
