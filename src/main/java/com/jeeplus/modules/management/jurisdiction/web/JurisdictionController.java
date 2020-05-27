/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.jurisdiction.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.jeeplus.modules.management.jurisdiction.entity.Jurisdiction;
import com.jeeplus.modules.management.jurisdiction.service.JurisdictionService;

/**
 * 库存数据权限Controller
 * @author Vigny
 * @version 2020-05-27
 */
@Controller
@RequestMapping(value = "${adminPath}/management/jurisdiction/jurisdiction")
public class JurisdictionController extends BaseController {

	@Autowired
	private JurisdictionService jurisdictionService;
	
	@ModelAttribute
	public Jurisdiction get(@RequestParam(required=false) String id) {
		Jurisdiction entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jurisdictionService.get(id);
		}
		if (entity == null){
			entity = new Jurisdiction();
		}
		return entity;
	}
	
	/**
	 * 库存数据权限列表页面
	 */
	@RequiresPermissions("management:jurisdiction:jurisdiction:list")
	@RequestMapping(value = {"list", ""})
	public String list(Jurisdiction jurisdiction, Model model) {
		model.addAttribute("jurisdiction", jurisdiction);
		return "modules/management/jurisdiction/jurisdictionList";
	}
	
		/**
	 * 库存数据权限列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:jurisdiction:jurisdiction:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Jurisdiction jurisdiction, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Jurisdiction> page = jurisdictionService.findPage(new Page<Jurisdiction>(request, response), jurisdiction); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑库存数据权限表单页面
	 */
	@RequiresPermissions(value={"management:jurisdiction:jurisdiction:view","management:jurisdiction:jurisdiction:add","management:jurisdiction:jurisdiction:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Jurisdiction jurisdiction, Model model) {
		model.addAttribute("jurisdiction", jurisdiction);
		return "modules/management/jurisdiction/jurisdictionForm";
	}

	/**
	 * 保存库存数据权限
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:jurisdiction:jurisdiction:add","management:jurisdiction:jurisdiction:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Jurisdiction jurisdiction, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(jurisdiction);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		jurisdictionService.save(jurisdiction);//保存
		j.setSuccess(true);
		j.setMsg("保存库存数据权限成功");
		return j;
	}
	
	/**
	 * 删除库存数据权限
	 */
	@ResponseBody
	@RequiresPermissions("management:jurisdiction:jurisdiction:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Jurisdiction jurisdiction) {
		AjaxJson j = new AjaxJson();
		jurisdictionService.delete(jurisdiction);
		j.setMsg("删除库存数据权限成功");
		return j;
	}
	
	/**
	 * 批量删除库存数据权限
	 */
	@ResponseBody
	@RequiresPermissions("management:jurisdiction:jurisdiction:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			jurisdictionService.delete(jurisdictionService.get(id));
		}
		j.setMsg("删除库存数据权限成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:jurisdiction:jurisdiction:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Jurisdiction jurisdiction, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "库存数据权限"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Jurisdiction> page = jurisdictionService.findPage(new Page<Jurisdiction>(request, response, -1), jurisdiction);
    		new ExportExcel("库存数据权限", Jurisdiction.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出库存数据权限记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:jurisdiction:jurisdiction:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Jurisdiction> list = ei.getDataList(Jurisdiction.class);
			for (Jurisdiction jurisdiction : list){
				try{
					jurisdictionService.save(jurisdiction);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条库存数据权限记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条库存数据权限记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入库存数据权限失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入库存数据权限数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:jurisdiction:jurisdiction:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "库存数据权限数据导入模板.xlsx";
    		List<Jurisdiction> list = Lists.newArrayList(); 
    		new ExportExcel("库存数据权限数据", Jurisdiction.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}