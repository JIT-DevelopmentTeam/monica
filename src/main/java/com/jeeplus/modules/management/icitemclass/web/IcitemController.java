/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.management.icitemclass.entity.IcitemClass;
import com.jeeplus.modules.monitor.utils.Common;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
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
import com.jeeplus.modules.management.icitemclass.entity.Icitem;
import com.jeeplus.modules.management.icitemclass.service.IcitemService;

/**
 * 商品资料Controller
 * @author JiaChe
 * @version 2019-05-29
 */
@Controller
@RequestMapping(value = "${adminPath}/management/icitemclass/icitem")
public class IcitemController extends BaseController {

	@Autowired
	private IcitemService icitemService;
	
	@ModelAttribute
	public Icitem get(@RequestParam(required=false) String id) {
		Icitem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = icitemService.get(id);
		}
		if (entity == null){
			entity = new Icitem();
		}
		return entity;
	}

	/**
	 * 同步物料分类
	 */
	@ResponseBody
	@RequestMapping(value = "synIcitem")
	public Map<String,Object>  synIcitem(String parentId) throws Exception{
		Map<String,Object> json = new HashMap<>();
		JSONArray jsonarr =
				Common.executeInter("http://192.168.1.252:8080/monica_erp/erp_get/erp_icitem?token_value=122331111","POST");
		Icitem icitem = new Icitem();
		JSONObject jsonObject = new JSONObject();
		for (int i = 0; i < jsonarr.size(); i++) {
			jsonObject = jsonarr.getJSONObject(i);
			icitem = new Icitem();
			icitem.setName(jsonObject.getString("f_name"));
			icitem.setErpId(jsonObject.getString("id"));
			icitem.setNumber(jsonObject.getString("f_number"));
			icitem.setUnit(jsonObject.getString("f_unitname"));
			icitem.setModel(jsonObject.getString("f_model"));
			IcitemClass icitemClass = new IcitemClass();
			icitemClass.setId(jsonObject.getString("f_classid"));
			icitem.setClassId(icitemClass);
			icitemService.save(icitem,true);
			System.out.println("================已同步物料:"+jsonObject.getString("f_name")+"================");
		}
		System.out.println("================插入成功================");
		json.put("Data",jsonarr);
		return json;
	}
	
	/**
	 * 商品资料列表页面
	 */
	@RequiresPermissions("management:icitemclass:icitem:list")
	@RequestMapping(value = {"list", ""})
	public String list(Icitem icitem, Model model) {
		model.addAttribute("icitem", icitem);
		return "modules/management/icitemclass/icitemList";
	}
	
		/**
	 * 商品资料列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:icitemclass:icitem:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Icitem icitem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Icitem> page = icitemService.findPage(new Page<Icitem>(request, response), icitem); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商品资料表单页面
	 */
	@RequiresPermissions(value={"management:icitemclass:icitem:view","management:icitemclass:icitem:add","management:icitemclass:icitem:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Icitem icitem, Model model) {
		model.addAttribute("icitem", icitem);
		return "modules/management/icitemclass/icitemForm";
	}

	/**
	 * 保存商品资料
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:icitemclass:icitem:add","management:icitemclass:icitem:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Icitem icitem, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(icitem);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		icitemService.save(icitem);//保存
		j.setSuccess(true);
		j.setMsg("保存商品资料成功");
		return j;
	}
	
	/**
	 * 删除商品资料
	 */
	@ResponseBody
	@RequiresPermissions("management:icitemclass:icitem:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Icitem icitem) {
		AjaxJson j = new AjaxJson();
		icitemService.delete(icitem);
		j.setMsg("删除商品资料成功");
		return j;
	}
	
	/**
	 * 批量删除商品资料
	 */
	@ResponseBody
	@RequiresPermissions("management:icitemclass:icitem:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			icitemService.delete(icitemService.get(id));
		}
		j.setMsg("删除商品资料成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:icitemclass:icitem:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Icitem icitem, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品资料"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Icitem> page = icitemService.findPage(new Page<Icitem>(request, response, -1), icitem);
    		new ExportExcel("商品资料", Icitem.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商品资料记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:icitemclass:icitem:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Icitem> list = ei.getDataList(Icitem.class);
			for (Icitem icitem : list){
				try{
					icitemService.save(icitem);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商品资料记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条商品资料记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入商品资料失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入商品资料数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:icitemclass:icitem:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品资料数据导入模板.xlsx";
    		List<Icitem> list = Lists.newArrayList(); 
    		new ExportExcel("商品资料数据", Icitem.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

    @RequestMapping(value = "getById")
	@ResponseBody
	public AjaxJson getById(Icitem icitem){
		AjaxJson aj = new AjaxJson();
		icitem = icitemService.get(icitem.getId());
		aj.put("icitem",icitem);
		return aj;
	}

	@RequestMapping(value = "getItemsList")
	@ResponseBody
	public AjaxJson getItemsList(Icitem icitem){
		AjaxJson aj = new AjaxJson();
		icitem.setDelFlag("0");
		List<Icitem> icitemList = icitemService.findList(icitem);
		aj.put("icitemList",icitemList);
		return aj;
	}

	@RequestMapping(value = "getListByName")
	@ResponseBody
	public AjaxJson getListByName(@Param("name") String name){
		AjaxJson aj = new AjaxJson();
		Icitem icitem = new Icitem();
		icitem.setDelFlag("0");
		icitem.setName(name.trim());
		List<Icitem> icitemList = icitemService.findList(icitem);
		aj.put("icitemList",icitemList);
		return aj;
	}

}