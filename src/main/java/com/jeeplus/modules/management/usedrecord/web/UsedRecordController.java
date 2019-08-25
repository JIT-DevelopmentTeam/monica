/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.usedrecord.web;

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
import com.jeeplus.modules.management.usedrecord.entity.UsedRecord;
import com.jeeplus.modules.management.usedrecord.service.UsedRecordService;

/**
 * token范围Controller
 * @author KicoChan
 * @version 2019-08-24
 */
@Controller
@RequestMapping(value = "${adminPath}/management/usedrecord/usedRecord")
public class UsedRecordController extends BaseController {

	@Autowired
	private UsedRecordService usedRecordService;
	
	@ModelAttribute
	public UsedRecord get(@RequestParam(required=false) String id) {
		UsedRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = usedRecordService.get(id);
		}
		if (entity == null){
			entity = new UsedRecord();
		}
		return entity;
	}
	
	/**
	 * token范围列表页面
	 */
	@RequiresPermissions("management:usedrecord:usedRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list(UsedRecord usedRecord, Model model) {
		model.addAttribute("usedRecord", usedRecord);
		return "modules/management/usedrecord/usedRecordList";
	}
	
		/**
	 * token范围列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:usedrecord:usedRecord:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(UsedRecord usedRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UsedRecord> page = usedRecordService.findPage(new Page<UsedRecord>(request, response), usedRecord); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑token范围表单页面
	 */
	@RequiresPermissions(value={"management:usedrecord:usedRecord:view","management:usedrecord:usedRecord:add","management:usedrecord:usedRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UsedRecord usedRecord, Model model) {
		model.addAttribute("usedRecord", usedRecord);
		return "modules/management/usedrecord/usedRecordForm";
	}

	/**
	 * 保存token范围
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:usedrecord:usedRecord:add","management:usedrecord:usedRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(UsedRecord usedRecord, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(usedRecord);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		usedRecordService.save(usedRecord);//保存
		j.setSuccess(true);
		j.setMsg("保存token范围成功");
		return j;
	}
	
	/**
	 * 删除token范围
	 */
	@ResponseBody
	@RequiresPermissions("management:usedrecord:usedRecord:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(UsedRecord usedRecord) {
		AjaxJson j = new AjaxJson();
		usedRecordService.delete(usedRecord);
		j.setMsg("删除token范围成功");
		return j;
	}
	
	/**
	 * 批量删除token范围
	 */
	@ResponseBody
	@RequiresPermissions("management:usedrecord:usedRecord:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			usedRecordService.delete(usedRecordService.get(id));
		}
		j.setMsg("删除token范围成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:usedrecord:usedRecord:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(UsedRecord usedRecord, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "token范围"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UsedRecord> page = usedRecordService.findPage(new Page<UsedRecord>(request, response, -1), usedRecord);
    		new ExportExcel("token范围", UsedRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出token范围记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:usedrecord:usedRecord:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UsedRecord> list = ei.getDataList(UsedRecord.class);
			for (UsedRecord usedRecord : list){
				try{
					usedRecordService.save(usedRecord);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条token范围记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条token范围记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入token范围失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入token范围数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:usedrecord:usedRecord:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "token范围数据导入模板.xlsx";
    		List<UsedRecord> list = Lists.newArrayList(); 
    		new ExportExcel("token范围数据", UsedRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}