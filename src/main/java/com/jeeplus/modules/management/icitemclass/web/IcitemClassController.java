/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.core.service.BaseService;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.apiurl.entity.ApiUrl;
import com.jeeplus.modules.management.apiurl.service.ApiUrlService;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;
import com.jeeplus.modules.management.icitemclass.entity.IcitemClass;
import com.jeeplus.modules.management.icitemclass.service.IcitemClassService;
import com.jeeplus.modules.management.icitemclass.service.IcitemService;
import com.jeeplus.modules.monitor.utils.Common;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品分类管理Controller
 * @author JiaChe
 * @version 2019-05-29
 */
@Controller
@RequestMapping(value = "${adminPath}/management/icitemclass/icitemClass")
public class IcitemClassController extends BaseController {

	@Autowired
	private IcitemClassService icitemClassService;
	@Autowired
    private ApiUrlService apiUrlService;
	@Autowired
	private IcitemService icitemService;
	
	@ModelAttribute
	public IcitemClass get(@RequestParam(required=false) String id) {
		IcitemClass entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = icitemClassService.get(id);
		}
		if (entity == null){
			entity = new IcitemClass();
		}
		return entity;
	}

	/**
	 * 同步物料分类
	 */
	@ResponseBody
	@RequestMapping(value = "synIcitem_Class")
	public AjaxJson  synIcitem_Class() {
		AjaxJson aj = new AjaxJson();
		try {
			String itemClassMsg = sync(IcitemClass.class, apiUrlService, "1", icitemClassService);
			aj.setMsg(itemClassMsg);
			String itemMsg = sync(Icitem.class, apiUrlService, "2", icitemService);
			aj.setMsg(itemMsg);
		} catch (Exception e) {
			aj.setSuccess(false);
			aj.setMsg(e.getMessage());
			return aj;
		}
		return aj;
	}

	/**
	 * 同步物料和分类的业务
	 * @param apiUrlService
	 * @param syncType	1为同步分类；2为同步物料
	 * @param Service
	 * @return
	 * @throws Exception
	 */
	public <T extends DataEntity> String sync(Class<T> clazz, ApiUrlService apiUrlService, String syncType, BaseService Service) throws Exception {
		String Message = "";
		ApiUrl apiUrl = apiUrlService.getByUsefulness(syncType);
		if (apiUrl == null || StringUtils.isBlank(apiUrl.getUrl())) {
			throw new Exception("同步出错,请检查接口配置是否准确!");
		}
		try {
			Method method = Service.getClass().getMethod("findMaxModifyTime");
			Object maxModifyTime = method.invoke(Service);
			if (maxModifyTime == null) {
				maxModifyTime = "";
			}
			JSONArray jsonarr =
					Common.executeInter(apiUrl.getUrl() + "&modifyTime=" + maxModifyTime, apiUrl.getProtocol());
			Method saveMethod = null;
			switch (syncType){
				case "1" :/* 保存物料分类 */
					saveMethod = this.getClass().getMethod("saveIcitemClass", JSONObject.class);
					break;
				case "2" :/* 保存物料 */
					saveMethod = this.getClass().getMethod("saveIcitem", JSONObject.class);
					break;
			}
			List<T> dataList = new ArrayList<>();
			for (int i = 0; i < jsonarr.size(); i++) {
				JSONObject jsonObject = jsonarr.getJSONObject(i);
				T obj =(T) saveMethod.invoke(this, jsonObject);
				dataList.add(obj);
				if (i % 1000 == 0 || i == jsonarr.size() - 1) {
					Method batchSaveMethod = Service.getClass().getMethod("batchInsert", List.class);
					batchSaveMethod.invoke(Service, dataList);
					dataList.clear();
				}
			}
			Message = "同步成功!";
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "";
			switch (syncType) {
				case "1":
					msg = "商品分类：";
					break;
				case "2":
					msg = "商品：";
					break;
			}
			throw new Exception(msg + "同步出错,请联系管理员!");
		}
		return Message;
	}

	/**
	 * 保存物料分类
	 * @param jsonObject
	 */
	public IcitemClass saveIcitemClass(JSONObject jsonObject) {
		IcitemClass icitemClass = new IcitemClass();
		IcitemClass parent = new IcitemClass();
		parent.setId(jsonObject.getString("f_parentid"));
		parent.setName(jsonObject.getString("f_name"));
		icitemClass.setParent(parent);
		icitemClass.setErpId(jsonObject.getString("f_itemid"));
		icitemClass.setId(jsonObject.getString("f_itemid"));
		icitemClass.setNumber(jsonObject.getString("f_number"));
		icitemClass.setName(jsonObject.getString("f_name"));
		icitemClass.setModifyTime(jsonObject.getString("f_modifytime"));
		icitemClass.setIsNewRecord(true);
		icitemClass.preInsert();
		return icitemClass;
	}

	/**
	 * 保存物料
	 * @param jsonObject
	 */
	public Icitem saveIcitem(JSONObject jsonObject) {
		Icitem icitem = new Icitem();
		icitem.setId(jsonObject.getString("id"));
		icitem.setName(jsonObject.getString("f_name"));
		icitem.setErpId(jsonObject.getString("id"));
		icitem.setNumber(jsonObject.getString("f_number"));
		icitem.setUnit(jsonObject.getString("f_unitname"));
		icitem.setModel(jsonObject.getString("f_model"));
		icitem.setModifyTime(jsonObject.getString("f_modifytime"));
		IcitemClass icitemClass = new IcitemClass();
		icitemClass.setId(jsonObject.getString("f_classid"));
		icitem.setClassId(icitemClass);
		icitem.setIsNewRecord(true);
		icitem.preInsert();
		return icitem;
	}
	
	/**
	 * 商品分类管理列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(IcitemClass icitemClass,  HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return "modules/management/icitemclass/icitemClassList";
	}

	/**
	 * 查看，增加，编辑商品分类管理表单页面
	 */
	@RequestMapping(value = "form")
	public String form(IcitemClass icitemClass, Model model) {
		if (icitemClass.getParent()!=null && StringUtils.isNotBlank(icitemClass.getParent().getId())){
			icitemClass.setParent(icitemClassService.get(icitemClass.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(icitemClass.getId())){
				IcitemClass icitemClassChild = new IcitemClass();
				icitemClassChild.setParent(new IcitemClass(icitemClass.getParent().getId()));
				List<IcitemClass> list = icitemClassService.findList(icitemClass); 
				if (list.size() > 0){
					icitemClass.setSort(list.get(list.size()-1).getSort());
					if (icitemClass.getSort() != null){
						icitemClass.setSort(icitemClass.getSort() + 30);
					}
				}
			}
		}
		if (icitemClass.getSort() == null){
			icitemClass.setSort(30);
		}
		model.addAttribute("icitemClass", icitemClass);
		return "modules/management/icitemclass/icitemClassForm";
	}

	/**
	 * 保存商品分类管理
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(IcitemClass icitemClass, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(icitemClass);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}

		//新增或编辑表单保存
		icitemClassService.save(icitemClass);//保存
		j.setSuccess(true);
		j.put("icitemClass", icitemClass);
		j.setMsg("保存商品分类管理成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<IcitemClass> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return icitemClassService.getChildren(parentId);
	}
	
	/**
	 * 删除商品分类管理
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(IcitemClass icitemClass) {
		AjaxJson j = new AjaxJson();
		icitemClassService.delete(icitemClass);
		j.setSuccess(true);
		j.setMsg("删除商品分类管理成功");
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<IcitemClass> list = icitemClassService.findList(new IcitemClass());
		for (int i=0; i<list.size(); i++){
			IcitemClass e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getName());
				map.put("number", e.getNumber());
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