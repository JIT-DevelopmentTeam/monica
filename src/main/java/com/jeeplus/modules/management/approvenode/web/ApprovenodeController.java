/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.approvenode.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.approvenode.entity.Approvenode;
import com.jeeplus.modules.management.approvenode.service.ApprovenodeService;
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
 * 流程节点Controller
 * @author Vigny
 * @version 2019-08-14
 */
@Controller
@RequestMapping(value = "${adminPath}/management/approvenode/approvenode")
public class ApprovenodeController extends BaseController {

	@Autowired
	private ApprovenodeService approvenodeService;
	
	@ModelAttribute
	public Approvenode get(@RequestParam(required=false) String id) {
		Approvenode entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = approvenodeService.get(id);
		}
		if (entity == null){
			entity = new Approvenode();
		}
		return entity;
	}
	
	/**
	 * 流程节点列表页面
	 */
	@RequiresPermissions("management:approvenode:approvenode:list")
	@RequestMapping(value = {"list", ""})
	public String list(Approvenode approvenode, Model model) {
		model.addAttribute("approvenode", approvenode);
		return "modules/management/approvenode/approvenodeList";
	}
	
		/**
	 * 流程节点列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:approvenode:approvenode:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Approvenode approvenode, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Approvenode> page = approvenodeService.findPage(new Page<Approvenode>(request, response), approvenode); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑流程节点表单页面
	 */
	@RequiresPermissions(value={"management:approvenode:approvenode:view","management:approvenode:approvenode:add","management:approvenode:approvenode:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Approvenode approvenode, Model model) {
		model.addAttribute("approvenode", approvenode);
		return "modules/management/approvenode/approvenodeForm";
	}

	/**
	 * 保存流程节点
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:approvenode:approvenode:add","management:approvenode:approvenode:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Approvenode approvenode, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(approvenode);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		approvenodeService.save(approvenode);//保存
		j.setSuccess(true);
		j.setMsg("保存流程节点成功");
		return j;
	}
	
	/**
	 * 删除流程节点
	 */
	@ResponseBody
	@RequiresPermissions("management:approvenode:approvenode:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Approvenode approvenode) {
		AjaxJson j = new AjaxJson();
		approvenodeService.delete(approvenode);
		j.setMsg("删除流程节点成功");
		return j;
	}
	
	/**
	 * 批量删除流程节点
	 */
	@ResponseBody
	@RequiresPermissions("management:approvenode:approvenode:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			approvenodeService.delete(approvenodeService.get(id));
		}
		j.setMsg("删除流程节点成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:approvenode:approvenode:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Approvenode approvenode, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "流程节点"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Approvenode> page = approvenodeService.findPage(new Page<Approvenode>(request, response, -1), approvenode);
    		new ExportExcel("流程节点", Approvenode.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出流程节点记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:approvenode:approvenode:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Approvenode> list = ei.getDataList(Approvenode.class);
			for (Approvenode approvenode : list){
				try{
					approvenodeService.save(approvenode);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条流程节点记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条流程节点记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入流程节点失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入流程节点数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:approvenode:approvenode:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "流程节点数据导入模板.xlsx";
    		List<Approvenode> list = Lists.newArrayList(); 
    		new ExportExcel("流程节点数据", Approvenode.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}