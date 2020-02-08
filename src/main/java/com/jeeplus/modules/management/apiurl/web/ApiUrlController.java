/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.apiurl.web;

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
import com.jeeplus.modules.management.apiurl.entity.ApiUrl;
import com.jeeplus.modules.management.apiurl.service.ApiUrlService;

/**
 * 接口管理Controller
 * @author 家成
 * @version 2019-05-30
 */
@Controller
@RequestMapping(value = "${adminPath}/management/apiurl/apiUrl")
public class ApiUrlController extends BaseController {

	@Autowired
	private ApiUrlService apiUrlService;
	
	@ModelAttribute
	public ApiUrl get(@RequestParam(required=false) String id) {
		ApiUrl entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = apiUrlService.get(id);
		}
		if (entity == null){
			entity = new ApiUrl();
		}
		return entity;
	}
	
	/**
	 * 接口管理列表页面
	 */
	@RequiresPermissions("management:apiurl:apiUrl:list")
	@RequestMapping(value = {"list", ""})
	public String list(ApiUrl apiUrl, Model model) {
		model.addAttribute("apiUrl", apiUrl);
		return "modules/management/apiurl/apiUrlList";
	}
	
	/**
	 * 接口管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:apiurl:apiUrl:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ApiUrl apiUrl, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ApiUrl> page = apiUrlService.findPage(new Page<ApiUrl>(request, response), apiUrl); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑接口管理表单页面
	 */
	@RequiresPermissions(value={"management:apiurl:apiUrl:view","management:apiurl:apiUrl:add","management:apiurl:apiUrl:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ApiUrl apiUrl, Model model) {
	    if (StringUtils.isBlank(apiUrl.getId())) {
	        apiUrl.setIsToken("1");
	        apiUrl.setStatus("1");
        }
		model.addAttribute("apiUrl", apiUrl);
		return "modules/management/apiurl/apiUrlForm";
	}

	/**
	 * 保存接口管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:apiurl:apiUrl:add","management:apiurl:apiUrl:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ApiUrl apiUrl, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(apiUrl);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		apiUrlService.save(apiUrl);//保存
		j.setSuccess(true);
		j.setMsg("保存接口管理成功");
		return j;
	}
	
	/**
	 * 删除接口管理
	 */
	@ResponseBody
	@RequiresPermissions("management:apiurl:apiUrl:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ApiUrl apiUrl) {
		AjaxJson j = new AjaxJson();
		apiUrlService.delete(apiUrl);
		j.setMsg("删除接口管理成功");
		return j;
	}
	
	/**
	 * 批量删除接口管理
	 */
	@ResponseBody
	@RequiresPermissions("management:apiurl:apiUrl:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			apiUrlService.delete(apiUrlService.get(id));
		}
		j.setMsg("删除接口管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:apiurl:apiUrl:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ApiUrl apiUrl, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "接口管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ApiUrl> page = apiUrlService.findPage(new Page<ApiUrl>(request, response, -1), apiUrl);
    		new ExportExcel("接口管理", ApiUrl.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出接口管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:apiurl:apiUrl:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ApiUrl> list = ei.getDataList(ApiUrl.class);
			for (ApiUrl apiUrl : list){
				try{
					apiUrlService.save(apiUrl);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条接口管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条接口管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入接口管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入接口管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:apiurl:apiUrl:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "接口管理数据导入模板.xlsx";
    		List<ApiUrl> list = Lists.newArrayList(); 
    		new ExportExcel("接口管理数据", ApiUrl.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}