/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.messagesend.web;

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
import com.jeeplus.modules.management.messagesend.entity.Messagesend;
import com.jeeplus.modules.management.messagesend.service.MessagesendService;

/**
 * 消息发送Controller
 * @author MrLISH
 * @version 2019-08-14
 */
@Controller
@RequestMapping(value = "${adminPath}/management/messagesend/messagesend")
public class MessagesendController extends BaseController {

	@Autowired
	private MessagesendService messagesendService;
	
	@ModelAttribute
	public Messagesend get(@RequestParam(required=false) String id) {
		Messagesend entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = messagesendService.get(id);
		}
		if (entity == null){
			entity = new Messagesend();
		}
		return entity;
	}
	
	/**
	 * 消息发送列表页面
	 */
	@RequiresPermissions("management:messagesend:messagesend:list")
	@RequestMapping(value = {"list", ""})
	public String list(Messagesend messagesend, Model model) {
		model.addAttribute("messagesend", messagesend);
		return "modules/management/messagesend/messagesendList";
	}
	
		/**
	 * 消息发送列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:messagesend:messagesend:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Messagesend messagesend, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Messagesend> page = messagesendService.findPage(new Page<Messagesend>(request, response), messagesend); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑消息发送表单页面
	 */
	@RequiresPermissions(value={"management:messagesend:messagesend:view","management:messagesend:messagesend:add","management:messagesend:messagesend:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Messagesend messagesend, Model model) {
		model.addAttribute("messagesend", messagesend);
		return "modules/management/messagesend/messagesendForm";
	}

	/**
	 * 保存消息发送
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:messagesend:messagesend:add","management:messagesend:messagesend:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Messagesend messagesend, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(messagesend);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		messagesendService.save(messagesend);//保存
		j.setSuccess(true);
		j.setMsg("保存消息发送成功");
		return j;
	}
	
	/**
	 * 删除消息发送
	 */
	@ResponseBody
	@RequiresPermissions("management:messagesend:messagesend:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Messagesend messagesend) {
		AjaxJson j = new AjaxJson();
		messagesendService.delete(messagesend);
		j.setMsg("删除消息发送成功");
		return j;
	}
	
	/**
	 * 批量删除消息发送
	 */
	@ResponseBody
	@RequiresPermissions("management:messagesend:messagesend:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			messagesendService.delete(messagesendService.get(id));
		}
		j.setMsg("删除消息发送成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:messagesend:messagesend:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Messagesend messagesend, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "消息发送"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Messagesend> page = messagesendService.findPage(new Page<Messagesend>(request, response, -1), messagesend);
    		new ExportExcel("消息发送", Messagesend.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出消息发送记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:messagesend:messagesend:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Messagesend> list = ei.getDataList(Messagesend.class);
			for (Messagesend messagesend : list){
				try{
					messagesendService.save(messagesend);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条消息发送记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条消息发送记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入消息发送失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入消息发送数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:messagesend:messagesend:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "消息发送数据导入模板.xlsx";
    		List<Messagesend> list = Lists.newArrayList(); 
    		new ExportExcel("消息发送数据", Messagesend.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}