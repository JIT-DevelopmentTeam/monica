/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.newspush.web;

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
import com.jeeplus.modules.management.newspush.entity.NewsPush;
import com.jeeplus.modules.management.newspush.service.NewsPushService;

/**
 * 微信推送新闻对象Controller
 * @author commit
 * @version 2019-06-18
 */
@Controller
@RequestMapping(value = "${adminPath}/management/newspush/newsPush")
public class NewsPushController extends BaseController {

	@Autowired
	private NewsPushService newsPushService;
	
	@ModelAttribute
	public NewsPush get(@RequestParam(required=false) String id) {
		NewsPush entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = newsPushService.get(id);
		}
		if (entity == null){
			entity = new NewsPush();
		}
		return entity;
	}
	
	/**
	 * 推送新闻对象列表页面
	 */
	@RequiresPermissions("management:newspush:newsPush:list")
	@RequestMapping(value = {"list", ""})
	public String list(NewsPush newsPush, Model model) {
		model.addAttribute("newsPush", newsPush);
		return "modules/management/newspush/newsPushList";
	}
	
		/**
	 * 推送新闻对象列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:newspush:newsPush:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(NewsPush newsPush, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NewsPush> page = newsPushService.findPage(new Page<NewsPush>(request, response), newsPush); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑推送新闻对象表单页面
	 */
	@RequiresPermissions(value={"management:newspush:newsPush:view","management:newspush:newsPush:add","management:newspush:newsPush:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(NewsPush newsPush, Model model) {
		model.addAttribute("newsPush", newsPush);
		return "modules/management/newspush/newsPushForm";
	}

	/**
	 * 保存推送新闻对象
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:newspush:newsPush:add","management:newspush:newsPush:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(NewsPush newsPush, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(newsPush);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		newsPushService.save(newsPush);//保存
		j.setSuccess(true);
		j.setMsg("保存推送新闻对象成功");
		return j;
	}
	
	/**
	 * 删除推送新闻对象
	 */
	@ResponseBody
	@RequiresPermissions("management:newspush:newsPush:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(NewsPush newsPush) {
		AjaxJson j = new AjaxJson();
		newsPushService.delete(newsPush);
		j.setMsg("删除推送新闻对象成功");
		return j;
	}
	
	/**
	 * 批量删除推送新闻对象
	 */
	@ResponseBody
	@RequiresPermissions("management:newspush:newsPush:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			newsPushService.delete(newsPushService.get(id));
		}
		j.setMsg("删除推送新闻对象成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:newspush:newsPush:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(NewsPush newsPush, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "推送新闻对象"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<NewsPush> page = newsPushService.findPage(new Page<NewsPush>(request, response, -1), newsPush);
    		new ExportExcel("推送新闻对象", NewsPush.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出推送新闻对象记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:newspush:newsPush:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<NewsPush> list = ei.getDataList(NewsPush.class);
			for (NewsPush newsPush : list){
				try{
					newsPushService.save(newsPush);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条推送新闻对象记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条推送新闻对象记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入推送新闻对象失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入推送新闻对象数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:newspush:newsPush:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "推送新闻对象数据导入模板.xlsx";
    		List<NewsPush> list = Lists.newArrayList(); 
    		new ExportExcel("推送新闻对象数据", NewsPush.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}