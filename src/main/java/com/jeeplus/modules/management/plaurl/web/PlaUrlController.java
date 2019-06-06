/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.plaurl.web;

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
import com.jeeplus.modules.management.plaurl.entity.PlaUrl;
import com.jeeplus.modules.management.plaurl.service.PlaUrlService;

/**
 * 平台链接管理Controller
 * @author 家成
 * @version 2019-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/management/plaurl/plaUrl")
public class PlaUrlController extends BaseController {

	@Autowired
	private PlaUrlService plaUrlService;
	
	@ModelAttribute
	public PlaUrl get(@RequestParam(required=false) String id) {
		PlaUrl entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = plaUrlService.get(id);
		}
		if (entity == null){
			entity = new PlaUrl();
		}
		return entity;
	}
	
	/**
	 * 平台链接管理列表页面
	 */
	@RequiresPermissions("management:plaurl:plaUrl:list")
	@RequestMapping(value = {"list", ""})
	public String list(PlaUrl plaUrl, Model model) {
		model.addAttribute("plaUrl", plaUrl);
		return "modules/management/plaurl/plaUrlList";
	}
	
		/**
	 * 平台链接管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:plaurl:plaUrl:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PlaUrl plaUrl, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PlaUrl> page = plaUrlService.findPage(new Page<PlaUrl>(request, response), plaUrl); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑平台链接管理表单页面
	 */
	@RequiresPermissions(value={"management:plaurl:plaUrl:view","management:plaurl:plaUrl:add","management:plaurl:plaUrl:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PlaUrl plaUrl, Model model) {
		model.addAttribute("plaUrl", plaUrl);
		return "modules/management/plaurl/plaUrlForm";
	}

	/**
	 * 保存平台链接管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:plaurl:plaUrl:add","management:plaurl:plaUrl:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(PlaUrl plaUrl, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(plaUrl);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		plaUrlService.save(plaUrl);//保存
		j.setSuccess(true);
		j.setMsg("保存平台链接管理成功");
		return j;
	}
	
	/**
	 * 删除平台链接管理
	 */
	@ResponseBody
	@RequiresPermissions("management:plaurl:plaUrl:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PlaUrl plaUrl) {
		AjaxJson j = new AjaxJson();
		plaUrlService.delete(plaUrl);
		j.setMsg("删除平台链接管理成功");
		return j;
	}
	
	/**
	 * 批量删除平台链接管理
	 */
	@ResponseBody
	@RequiresPermissions("management:plaurl:plaUrl:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			plaUrlService.delete(plaUrlService.get(id));
		}
		j.setMsg("删除平台链接管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:plaurl:plaUrl:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(PlaUrl plaUrl, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "平台链接管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PlaUrl> page = plaUrlService.findPage(new Page<PlaUrl>(request, response, -1), plaUrl);
    		new ExportExcel("平台链接管理", PlaUrl.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出平台链接管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:plaurl:plaUrl:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PlaUrl> list = ei.getDataList(PlaUrl.class);
			for (PlaUrl plaUrl : list){
				try{
					plaUrlService.save(plaUrl);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条平台链接管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条平台链接管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入平台链接管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入平台链接管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:plaurl:plaUrl:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "平台链接管理数据导入模板.xlsx";
    		List<PlaUrl> list = Lists.newArrayList(); 
    		new ExportExcel("平台链接管理数据", PlaUrl.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}