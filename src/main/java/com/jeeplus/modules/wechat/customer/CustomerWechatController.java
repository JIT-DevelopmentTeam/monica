package com.jeeplus.modules.wechat.customer;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.customer.entity.Customer;
import com.jeeplus.modules.management.customer.service.CustomerService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "${frontPath}/wechat/customer")
public class CustomerWechatController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据员工id获取客户列表
     * @return
     */
    @RequestMapping(value = "getCustomerListByEmpId")
    @ResponseBody
    public AjaxJson getCustomerListByEmpId(Customer customer){
        AjaxJson aj = new AjaxJson();
        List<Customer> customerList = customerService.findList(customer);
        if (customerList.isEmpty()) {
            aj.setSuccess(false);
        }
        aj.put("customerList",customerList);
        return aj;
    }

    /**
     * 获取客户对象
     * @param id
     * @return
     */
    @RequestMapping(value = "getCustomerById")
    @ResponseBody
    public AjaxJson getCustomerById(@RequestParam("id") String id){
        AjaxJson aj = new AjaxJson();
        Customer customer = customerService.get(id);
        aj.put("customer",customer);
        return aj;
    }

}
