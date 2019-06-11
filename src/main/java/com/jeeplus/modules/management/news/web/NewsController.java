/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.news.web;

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
import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.management.news.service.NewsService;

/**
 * 新闻公告Controller
 * @author Vigny
 * @version 2019-06-10
 */
@Controller
@RequestMapping(value = "${adminPath}/management/news/news")
public class NewsController extends BaseController {

	@Autowired
	private NewsService newsService;
	
	@ModelAttribute
	public News get(@RequestParam(required=false) String id) {
		News entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = newsService.get(id);
		}
		if (entity == null){
			entity = new News();
		}
		return entity;
	}
	
	/**
	 * 新闻公告列表页面
	 */
	@RequiresPermissions("management:news:news:list")
	@RequestMapping(value = {"list", ""})
	public String list(News news, Model model) {
		model.addAttribute("news", news);
		return "modules/management/news/newsList";
	}
	
		/**
	 * 新闻公告列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:news:news:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(News news, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<News> page = newsService.findPage(new Page<News>(request, response), news); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑新闻公告表单页面
	 */
	@RequiresPermissions(value={"management:news:news:view","management:news:news:add","management:news:news:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(News news, Model model) {
		model.addAttribute("news", news);
		return "modules/management/news/newsForm";
	}

	/**
	 * 保存新闻公告
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:news:news:add","management:news:news:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(News news, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(news);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		newsService.save(news);//保存
		j.setSuccess(true);
		j.setMsg("保存新闻公告成功");
		return j;
	}
	
	/**
	 * 删除新闻公告
	 */
	@ResponseBody
	@RequiresPermissions("management:news:news:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(News news) {
		AjaxJson j = new AjaxJson();
		newsService.delete(news);
		j.setMsg("删除新闻公告成功");
		return j;
	}
	
	/**
	 * 批量删除新闻公告
	 */
	@ResponseBody
	@RequiresPermissions("management:news:news:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			newsService.delete(newsService.get(id));
		}
		j.setMsg("删除新闻公告成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:news:news:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(News news, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "新闻公告"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<News> page = newsService.findPage(new Page<News>(request, response, -1), news);
    		new ExportExcel("新闻公告", News.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出新闻公告记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:news:news:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<News> list = ei.getDataList(News.class);
			for (News news : list){
				try{
					newsService.save(news);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条新闻公告记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条新闻公告记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入新闻公告失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入新闻公告数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:news:news:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "新闻公告数据导入模板.xlsx";
    		List<News> list = Lists.newArrayList(); 
    		new ExportExcel("新闻公告数据", News.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}