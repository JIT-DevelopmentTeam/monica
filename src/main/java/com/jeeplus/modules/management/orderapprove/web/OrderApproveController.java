/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.orderapprove.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.management.orderapprove.entity.OrderApprove;
import com.jeeplus.modules.management.orderapprove.service.OrderApproveService;

/**
 * 订单流程绑定Controller
 * @author KicoChan
 * @version 2019-08-14
 */
@Controller
@RequestMapping(value = "${adminPath}/management/orderapprove/orderApprove")
public class OrderApproveController extends BaseController {

	@Autowired
	private OrderApproveService orderApproveService;
	
	@ModelAttribute
	public OrderApprove get(@RequestParam(required=false) String id) {
		OrderApprove entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = orderApproveService.get(id);
		}
		if (entity == null){
			entity = new OrderApprove();
		}
		return entity;
	}
	
	/**
	 * 订单流程列表页面
	 */
	@RequiresPermissions("management:orderapprove:orderApprove:list")
	@RequestMapping(value = {"list", ""})
	public String list(OrderApprove orderApprove, Model model) {
		model.addAttribute("orderApprove", orderApprove);
		return "modules/management/orderapprove/orderApproveList";
	}
	
		/**
	 * 订单流程列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:orderapprove:orderApprove:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(OrderApprove orderApprove, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OrderApprove> page = orderApproveService.findPage(new Page<OrderApprove>(request, response), orderApprove); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑订单流程表单页面
	 */
	@RequiresPermissions(value={"management:orderapprove:orderApprove:view","management:orderapprove:orderApprove:add","management:orderapprove:orderApprove:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OrderApprove orderApprove, Model model) {
		model.addAttribute("orderApprove", orderApprove);
		return "modules/management/orderapprove/orderApproveForm";
	}

	/**
	 * 保存订单流程
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:orderapprove:orderApprove:add","management:orderapprove:orderApprove:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(OrderApprove orderApprove, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(orderApprove);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		orderApproveService.save(orderApprove);//保存
		j.setSuccess(true);
		j.setMsg("保存订单流程成功");
		return j;
	}
	
	/**
	 * 删除订单流程
	 */
	@ResponseBody
	@RequiresPermissions("management:orderapprove:orderApprove:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(OrderApprove orderApprove) {
		AjaxJson j = new AjaxJson();
		orderApproveService.delete(orderApprove);
		j.setMsg("删除订单流程成功");
		return j;
	}
	
	/**
	 * 批量删除订单流程
	 */
	@ResponseBody
	@RequiresPermissions("management:orderapprove:orderApprove:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			orderApproveService.delete(orderApproveService.get(id));
		}
		j.setMsg("删除订单流程成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:orderapprove:orderApprove:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(OrderApprove orderApprove, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单流程"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<OrderApprove> page = orderApproveService.findPage(new Page<OrderApprove>(request, response, -1), orderApprove);
    		new ExportExcel("订单流程", OrderApprove.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出订单流程记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:orderapprove:orderApprove:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OrderApprove> list = ei.getDataList(OrderApprove.class);
			for (OrderApprove orderApprove : list){
				try{
					orderApproveService.save(orderApprove);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单流程记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条订单流程记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入订单流程失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入订单流程数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:orderapprove:orderApprove:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单流程数据导入模板.xlsx";
    		List<OrderApprove> list = Lists.newArrayList(); 
    		new ExportExcel("订单流程数据", OrderApprove.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}