/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.modules.monitor.utils.Common;
import net.sf.json.JSONArray;
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
import com.jeeplus.modules.management.icitemclass.entity.IcitemClass;
import com.jeeplus.modules.management.icitemclass.service.IcitemClassService;

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
	@RequestMapping(value = "synIcitemClass")
	public Map<String,Object>  synIcitemClass(String parentId){
		Map<String,Object> json = new HashMap<>();
		JSONArray jsonarr = Common.executeInter("http://www.phonegap100.com/appapi.php?a=getPortalList&catid=20&page=1","POST");
		json.put("result",jsonarr);
		return json;
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