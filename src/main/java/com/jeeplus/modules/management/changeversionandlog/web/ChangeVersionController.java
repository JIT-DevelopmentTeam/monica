/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.changeversionandlog.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.base.ObjectUtil;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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
import com.jeeplus.modules.management.changeversionandlog.entity.ChangeVersion;
import com.jeeplus.modules.management.changeversionandlog.service.ChangeVersionService;

/**
 * 变更版本和记录Controller
 * @author KicoChan
 * @version 2020-03-09
 */
@Controller
@RequestMapping(value = "${adminPath}/management/changeversionandlog/changeVersion")
public class ChangeVersionController extends BaseController {

	@Autowired
	private ChangeVersionService changeVersionService;
	
	@ModelAttribute
	public ChangeVersion get(@RequestParam(required=false) String id) {
		ChangeVersion entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = changeVersionService.get(id);
		}
		if (entity == null){
			entity = new ChangeVersion();
		}
		return entity;
	}
	
	/**
	 * 变更列表页面
	 */
	@RequiresPermissions("management:changeversionandlog:changeVersion:list")
	@RequestMapping(value = {"list", ""})
	public String list(ChangeVersion changeVersion, Model model) {
		model.addAttribute("changeVersion", changeVersion);
		return "modules/management/changeversionandlog/changeVersionList";
	}
	
		/**
	 * 变更列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:changeversionandlog:changeVersion:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ChangeVersion changeVersion, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (!ObjectUtils.isEmpty(changeVersion.getSobill())) {
            Sobill sobill = new Sobill();
            sobill.setId("0");
            changeVersion.setSobill(sobill);
        }
		Page<ChangeVersion> page = changeVersionService.findPage(new Page<ChangeVersion>(request, response), changeVersion);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑变更表单页面
	 */
	@RequiresPermissions(value={"management:changeversionandlog:changeVersion:view","management:changeversionandlog:changeVersion:add","management:changeversionandlog:changeVersion:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ChangeVersion changeVersion, Model model) {
		model.addAttribute("changeVersion", changeVersion);
		return "modules/management/changeversionandlog/changeVersionForm";
	}

	/**
	 * 保存变更
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:changeversionandlog:changeVersion:add","management:changeversionandlog:changeVersion:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ChangeVersion changeVersion, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(changeVersion);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		changeVersionService.save(changeVersion);//保存
		j.setSuccess(true);
		j.setMsg("保存变更成功");
		return j;
	}
	
	/**
	 * 删除变更
	 */
	@ResponseBody
	@RequiresPermissions("management:changeversionandlog:changeVersion:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ChangeVersion changeVersion) {
		AjaxJson j = new AjaxJson();
		changeVersionService.delete(changeVersion);
		j.setMsg("删除变更成功");
		return j;
	}
	
	/**
	 * 批量删除变更
	 */
	@ResponseBody
	@RequiresPermissions("management:changeversionandlog:changeVersion:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			changeVersionService.delete(changeVersionService.get(id));
		}
		j.setMsg("删除变更成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:changeversionandlog:changeVersion:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ChangeVersion changeVersion, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "变更"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ChangeVersion> page = changeVersionService.findPage(new Page<ChangeVersion>(request, response, -1), changeVersion);
    		new ExportExcel("变更", ChangeVersion.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出变更记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public ChangeVersion detail(String id) {
		return changeVersionService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:changeversionandlog:changeVersion:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ChangeVersion> list = ei.getDataList(ChangeVersion.class);
			for (ChangeVersion changeVersion : list){
				try{
					changeVersionService.save(changeVersion);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条变更记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条变更记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入变更失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入变更数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:changeversionandlog:changeVersion:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "变更数据导入模板.xlsx";
    		List<ChangeVersion> list = Lists.newArrayList(); 
    		new ExportExcel("变更数据", ChangeVersion.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }
	

}