/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.warehouse.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.config.Global;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.management.warehouse.entity.Warehouse;
import com.jeeplus.modules.management.warehouse.service.WarehouseService;

/**
 * 仓库管理表Controller
 * @author Vigny
 * @version 2019-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/management/warehouse/warehouse")
public class WarehouseController extends BaseController {

	@Autowired
	private WarehouseService warehouseService;
	
	@ModelAttribute
	public Warehouse get(@RequestParam(required=false) String id) {
		Warehouse entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = warehouseService.get(id);
		}
		if (entity == null){
			entity = new Warehouse();
		}
		return entity;
	}
	
	/**
	 * 仓库管理表列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(Warehouse warehouse,  HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return "modules/management/warehouse/warehouseList";
	}

	/**
	 * 查看，增加，编辑仓库管理表表单页面
	 */
	@RequestMapping(value = "form")
	public String form(Warehouse warehouse, Model model) {
		if (warehouse.getParent()!=null && StringUtils.isNotBlank(warehouse.getParent().getId())){
			warehouse.setParent(warehouseService.get(warehouse.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(warehouse.getId())){
				Warehouse warehouseChild = new Warehouse();
				warehouseChild.setParent(new Warehouse(warehouse.getParent().getId()));
				List<Warehouse> list = warehouseService.findList(warehouse); 
				if (list.size() > 0){
					warehouse.setSort(list.get(list.size()-1).getSort());
					if (warehouse.getSort() != null){
						warehouse.setSort(warehouse.getSort() + 30);
					}
				}
			}
		}
		if (warehouse.getSort() == null){
			warehouse.setSort(30);
		}
		model.addAttribute("warehouse", warehouse);
		return "modules/management/warehouse/warehouseForm";
	}

	/**
	 * 保存仓库管理表
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(Warehouse warehouse, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(warehouse);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}

		//新增或编辑表单保存
		warehouseService.save(warehouse);//保存
		j.setSuccess(true);
		j.put("warehouse", warehouse);
		j.setMsg("保存仓库管理表成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<Warehouse> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return warehouseService.getChildren(parentId);
	}
	
	/**
	 * 删除仓库管理表
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(Warehouse warehouse) {
		AjaxJson j = new AjaxJson();
		warehouseService.delete(warehouse);
		j.setSuccess(true);
		j.setMsg("删除仓库管理表成功");
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Warehouse> list = warehouseService.findList(new Warehouse());
		for (int i=0; i<list.size(); i++){
			Warehouse e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getName());
				if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}