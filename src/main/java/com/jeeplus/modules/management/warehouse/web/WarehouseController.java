/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.warehouse.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.apiurl.entity.ApiUrl;
import com.jeeplus.modules.management.apiurl.service.ApiUrlService;
import com.jeeplus.modules.management.warehouse.entity.Warehouse;
import com.jeeplus.modules.management.warehouse.service.WarehouseService;
import com.jeeplus.modules.monitor.utils.Common;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 库存管理Controller
 * @author Vigny
 * @version 2019-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/management/warehouse/warehouse")
public class WarehouseController extends BaseController {

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private ApiUrlService apiUrlService;
	
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
	 * 同步仓库
	 */
	@ResponseBody
	@RequiresPermissions("management:warehouse:warehouse:sync")
	@RequestMapping(value = "synWareHouse")
	public AjaxJson  synWareHouse(String parentId) throws Exception{
		AjaxJson aj = new AjaxJson();
		ApiUrl apiUrl = apiUrlService.getByUsefulness("13");
		if (apiUrl == null || StringUtils.isBlank(apiUrl.getUrl())) {
			aj.setSuccess(false);
			aj.setMsg("同步出错,请检查接口配置是否准确!");
			return aj;
		}
		String url = apiUrl.getUrl() + "&modifyTime=";
		Long maxModifyTime = warehouseService.findMaxModifyTime();
		if (maxModifyTime != null) {
			url += maxModifyTime;
		}
		JSONArray jsonarr = Common.executeInter(url, apiUrl.getProtocol());

		JSONObject jsonObject;
		for (int i = 0; i < jsonarr.size(); i++) {
			jsonObject = jsonarr.getJSONObject(i);
			Warehouse warehouse = new Warehouse();
			Warehouse parent = new Warehouse();
			parent.setId(jsonObject.getString("FParentID"));
			warehouse.setParent(parent);
			warehouse.setId(jsonObject.getString("FItemID"));
			warehouse.setErpid(jsonObject.getString("FItemID"));
			warehouse.setNumber(jsonObject.getString("FNumber"));
			warehouse.setName(jsonObject.getString("FName"));
			String status = jsonObject.getString("FMRPAvail");
			if (status == "true") {
				warehouse.setStatus(1);
			} else if (status == "false") {
				warehouse.setStatus(0);
			}
			warehouse.setModifytime(jsonObject.getLong("FModifyTime"));
			warehouse.setIsNewRecord(true);
			warehouseService.save(warehouse);
		}
		return aj;
	}
	
	/**
	 * 库存管理列表页面
	 */
	@RequiresPermissions("management:warehouse:warehouse:list")
	@RequestMapping(value = {"list", ""})
	public String list(Warehouse warehouse, Model model) {
		model.addAttribute("warehouse", warehouse);
		return "modules/management/warehouse/warehouseList";
	}

	/**
	 * 库存列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:warehouse:warehouse:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Warehouse warehouse, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Warehouse> page = warehouseService.findPage(new Page<Warehouse>(request, response), warehouse);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑库存管理表单页面
	 */
	@RequiresPermissions(value={"management:warehouse:warehouse:view","management:warehouse:warehouse:add","management:warehouse:warehouse:edit"},logical= Logical.OR)
	@RequestMapping(value = "form")
	public String form(Warehouse warehouse, Model model) {
		model.addAttribute("warehouse", warehouse);
		return "modules/management/warehouse/warehouseForm";


		/*if (warehouse.getParent()!=null && StringUtils.isNotBlank(warehouse.getParent().getId())){
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
		return "itemClassForm";*/
	}

	/**
	 * 保存库存管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:warehouse:warehouse:add","management:warehouse:warehouse:edit"},logical=Logical.OR)
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
		j.setMsg("保存库存管理成功");
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
	 * 删除库存管理
	 */
	@ResponseBody
	@RequiresPermissions("management:warehouse:warehouse:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Warehouse warehouse) {
		AjaxJson j = new AjaxJson();
		warehouseService.delete(warehouse);
		j.setSuccess(true);
		j.setMsg("删除库存管理成功");
		return j;
	}

	/**
	 * 批量删除库存
	 */
	@ResponseBody
	@RequiresPermissions("management:warehouse:warehouse:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			warehouseService.delete(warehouseService.get(id));
		}
		j.setMsg("删除库存成功");
		return j;
	}

	/*@RequiresPermissions("user")
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
	}*/
	
}