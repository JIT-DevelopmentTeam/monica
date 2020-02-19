/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.customer.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.apiurl.entity.ApiUrl;
import com.jeeplus.modules.management.apiurl.service.ApiUrlService;
import com.jeeplus.modules.management.customer.entity.Customer;
import com.jeeplus.modules.management.customer.service.CustomerService;
import com.jeeplus.modules.monitor.utils.Common;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 客户管理Controller
 * @author commit
 * @version 2019-05-30
 */
@Controller
@RequestMapping(value = "${adminPath}/management/customer/customer")
public class CustomerController extends BaseController {

	@Autowired
	private CustomerService customerService;

    @Autowired
    private ApiUrlService apiUrlService;

	@Autowired
	private UserMapper userMapper;


	
	@ModelAttribute
	public Customer get(@RequestParam(required=false) String id) {
		Customer entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customerService.get(id);
		}
		if (entity == null){
			entity = new Customer();
		}
		return entity;
	}
	
	/**
	 * 客户信息列表页面
	 */
	@RequiresPermissions("management:customer:customer:list")
	@RequestMapping(value = {"list", ""})
	public String list(Customer customer, Model model) {
		model.addAttribute("customer", customer);
		return "modules/management/customer/customerList";
	}
	
		/**
	 * 客户信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:customer:customer:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Customer customer, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Customer> page = customerService.findPage(new Page<Customer>(request, response), customer); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑客户信息表单页面
	 */
	@RequiresPermissions(value={"management:customer:customer:view","management:customer:customer:add","management:customer:customer:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Customer customer, Model model) {
		model.addAttribute("customer", customer);
		return "modules/management/customer/customerForm";
	}

	/**
	 * 保存客户信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:customer:customer:add","management:customer:customer:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Customer customer, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(customer);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		customerService.save(customer);//保存
		j.setSuccess(true);
		j.setMsg("保存客户信息成功");
		return j;
	}
	
	/**
	 * 删除客户信息
	 */
	@ResponseBody
	@RequiresPermissions("management:customer:customer:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Customer customer) {
		AjaxJson j = new AjaxJson();
		customerService.delete(customer);
		j.setMsg("删除客户信息成功");
		return j;
	}
	
	/**
	 * 批量删除客户信息
	 */
	@ResponseBody
	@RequiresPermissions("management:customer:customer:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			customerService.delete(customerService.get(id));
		}
		j.setMsg("删除客户信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:customer:customer:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Customer customer, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "客户信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Customer> page = customerService.findPage(new Page<Customer>(request, response, -1), customer);
    		new ExportExcel("客户信息", Customer.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出客户信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 远程同步客户信息
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "synchCustomerInfo")
	public AjaxJson synchCustomer(){
        AjaxJson aj = new AjaxJson();
        ApiUrl apiUrl = apiUrlService.getByUsefulness("3");
        if (apiUrl == null || StringUtils.isBlank(apiUrl.getUrl())) {
            aj.setSuccess(false);
            aj.setMsg("同步出错,请检查接口配置是否准确!");
            return aj;
        }
        try {
            JSONArray jsonArray = Common.executeInter(apiUrl.getUrl(),apiUrl.getProtocol());
            customerService.deleteAllData();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Customer customer = new Customer();
                customer.setId(jsonObject.getString("FItemID"));
                customer.setErpId(jsonObject.getString("FItemID"));
                customer.setName(jsonObject.getString("A_FName"));
                customer.setNumber(jsonObject.getString("FNumber"));
                customer.setEmplId(jsonObject.getString("FEmpID"));
                customer.setStatus(1);
                customer.setIsNewRecord(true);
                customerService.save(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            aj.setSuccess(false);
            aj.setMsg("同步出错,请联系管理员!");
        }
		return aj;
	}

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:customer:customer:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Customer> list = ei.getDataList(Customer.class);
			for (Customer customer : list){
				try{
					customerService.save(customer);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条客户信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条客户信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入客户信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入客户信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:customer:customer:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "客户信息数据导入模板.xlsx";
    		List<Customer> list = Lists.newArrayList(); 
    		new ExportExcel("客户信息数据", Customer.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

    @ResponseBody
	@RequestMapping(value = "findUserList")
    public List<Map<String,Object>> userList(){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		User user=new User();
		user.setDelFlag("0");
		user.setCompany(null);
		user.setOffice(null);
		List<User> users = userMapper.findListByUserOfficeList(user);
		for (int i=0; i<users.size(); i++){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", user.getOffice());
				map.put("parent", "#");
				map.put("name", user.getName());
				map.put("text", user.getName());
				map.put("type", user.getIdType());
				mapList.add(map);
		}
		return mapList;
	}

}