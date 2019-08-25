/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.newsfile.web;

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
import com.jeeplus.modules.management.newsfile.entity.NewsFile;
import com.jeeplus.modules.management.newsfile.service.NewsFileService;

/**
 * 新闻公告附件Controller
 * @author KicoChan
 * @version 2019-08-24
 */
@Controller
@RequestMapping(value = "${adminPath}/management/newsfile/newsFile")
public class NewsFileController extends BaseController {

	@Autowired
	private NewsFileService newsFileService;
	
	@ModelAttribute
	public NewsFile get(@RequestParam(required=false) String id) {
		NewsFile entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = newsFileService.get(id);
		}
		if (entity == null){
			entity = new NewsFile();
		}
		return entity;
	}
	
	/**
	 * 新闻公告附件列表页面
	 */
	@RequiresPermissions("management:newsfile:newsFile:list")
	@RequestMapping(value = {"list", ""})
	public String list(NewsFile newsFile, Model model) {
		model.addAttribute("newsFile", newsFile);
		return "modules/management/newsfile/newsFileList";
	}
	
		/**
	 * 新闻公告附件列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:newsfile:newsFile:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(NewsFile newsFile, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NewsFile> page = newsFileService.findPage(new Page<NewsFile>(request, response), newsFile); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑新闻公告附件表单页面
	 */
	@RequiresPermissions(value={"management:newsfile:newsFile:view","management:newsfile:newsFile:add","management:newsfile:newsFile:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(NewsFile newsFile, Model model) {
		model.addAttribute("newsFile", newsFile);
		return "modules/management/newsfile/newsFileForm";
	}

	/**
	 * 保存新闻公告附件
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:newsfile:newsFile:add","management:newsfile:newsFile:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(NewsFile newsFile, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(newsFile);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		newsFileService.save(newsFile);//保存
		j.setSuccess(true);
		j.setMsg("保存新闻公告附件成功");
		return j;
	}
	
	/**
	 * 删除新闻公告附件
	 */
	@ResponseBody
	@RequiresPermissions("management:newsfile:newsFile:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(NewsFile newsFile) {
		AjaxJson j = new AjaxJson();
		newsFileService.delete(newsFile);
		j.setMsg("删除新闻公告附件成功");
		return j;
	}
	
	/**
	 * 批量删除新闻公告附件
	 */
	@ResponseBody
	@RequiresPermissions("management:newsfile:newsFile:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			newsFileService.delete(newsFileService.get(id));
		}
		j.setMsg("删除新闻公告附件成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:newsfile:newsFile:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(NewsFile newsFile, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "新闻公告附件"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<NewsFile> page = newsFileService.findPage(new Page<NewsFile>(request, response, -1), newsFile);
    		new ExportExcel("新闻公告附件", NewsFile.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出新闻公告附件记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:newsfile:newsFile:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<NewsFile> list = ei.getDataList(NewsFile.class);
			for (NewsFile newsFile : list){
				try{
					newsFileService.save(newsFile);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条新闻公告附件记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条新闻公告附件记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入新闻公告附件失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入新闻公告附件数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:newsfile:newsFile:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "新闻公告附件数据导入模板.xlsx";
    		List<NewsFile> list = Lists.newArrayList(); 
    		new ExportExcel("新闻公告附件数据", NewsFile.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}