/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.monitor.utils.Common;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.AreaService;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.department.JwDepartmentAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.department.vo.Department;
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
import java.util.*;

/**
 * 机构Controller
 * @author jeeplus
 * @version 2016-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;

	@Autowired
	private AreaService areaService;
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return officeService.get(id);
		}else{
			return new Office();
		}
	}

	/**
	 * 同步部门
	 */
	@ResponseBody
	@RequestMapping(value = "synDept")
	public Map<String,Object>  synDept(String parentId) throws Exception{
		Map<String,Object> json = new HashMap<>();
		JSONArray jsonarr =
				Common.executeInter("http://192.168.1.252:8080/monica_erp/erp_get/erp_dept?token_value=20190603","POST");

		Office officeName = officeService.getByName("莫尔卡");			// 按企业名查询企业资料
		Area areaChina = areaService.findByName("中国");					// 按地域名查询地域信息
		Area area = new Area();
		List<Office> officeList = officeService.findList(new Office());
		officeService.deleteByParentId(officeName.getId());				// 导入之前先把企业里面的部门全部删除

		JSONObject jsonObject = new JSONObject();
		for (int i = 0; i < jsonarr.size(); i++) {
			System.out.println(jsonarr.get(i));
			jsonObject = jsonarr.getJSONObject(i);
			Office office = new Office();
			Office parent = new Office();
			String parentID = jsonObject.getString("FParentID");
			if ("0".equals(parentID)) {
				parent.setId(officeName.getId());
				parent.setName(officeName.getName());
			} else {
				parent.setId(parentID);
				for (Office office1 : officeList) {
					if (office1.getId().equals(parentID)) {
						parent.setName(office1.getName());
						break;
					}
				}
			}

			office.setParent(parent);
			office.setId(jsonObject.getString("FItemID"));
			office.setName(jsonObject.getString("FName"));
			office.setCode(jsonObject.getString("FNumber"));
			area.setId(areaChina.getId());
			office.setArea(area);
			office.setGrade("1");
			office.setUseable("1");
			Office findParentById = officeService.get(office.getParent().getId());
			if (findParentById != null) {
				office.setQyDeptParentId(findParentById.getQyDeptId());
				List<Office> officeListByQyDeptParentId = officeService.findByQyDeptParentId(0, office.getQyDeptParentId());
				int size = 0;
				if (officeListByQyDeptParentId.size() > 0) {
					size = officeListByQyDeptParentId.size() + 1;
				} else {
					size = 1;
				}
				office.setQyDeptId(Integer.parseInt(office.getQyDeptParentId() + "" + size));
			}
			office.setSynStatus(0);
			office.setIsSyntoent(1);

			officeService.save(office);
		}
		System.out.println("================插入成功================");
		json.put("Data",jsonarr);
		return json;
	}

	@RequiresPermissions("sys:office:list")
	@RequestMapping(value = {"", "list"})
	public String list(Office office, Model model) {
		if(office==null || office.getParentIds() == null){
			 model.addAttribute("list", officeService.findList(false));
		}else{
			 model.addAttribute("list", officeService.findList(office));
		}
		return "modules/sys/office/officeList";
	}
	
	@RequiresPermissions(value={"sys:office:view","sys:office:add","sys:office:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		User user = UserUtils.getUser();
		if (office.getParent()==null || office.getParent().getId()==null){
			office.setParent(user.getOffice());
		}
		office.setParent(officeService.get(office.getParent().getId()));
		if (office.getArea()==null){
			office.setArea(user.getOffice().getArea());
		}
		// 自动获取排序号
		if (StringUtils.isBlank(office.getId())&&office.getParent()!=null){
			int size = 0;
			List<Office> list = officeService.findAll();
			for (int i=0; i<list.size(); i++){
				Office e = list.get(i);
				if (e.getParent()!=null && e.getParent().getId()!=null
						&& e.getParent().getId().equals(office.getParent().getId())){
					size++;
				}
			}
			office.setCode(office.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size+1 : 1), 3, "0"));
		}
		model.addAttribute("office", office);
		return "modules/sys/office/officeForm";
	}
	
	@ResponseBody
	@RequiresPermissions(value={"sys:office:add","sys:office:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Office office, Model model, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(office);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.contactSecret);
		if ("".equals(office.getId())) {
			if ("".equals(office.getParent().getId())) {
				office.setQyDeptParentId(1);
			} else {
				Office findParentById = officeService.get(office.getParent().getId());
				office.setQyDeptParentId(findParentById.getQyDeptId());
			}
			List<Office> officeListByQyDeptParentId = officeService.findByQyDeptParentId(0, office.getQyDeptParentId());
			int size = 0;
			if (officeListByQyDeptParentId.size() > 0) {
				size = officeListByQyDeptParentId.size() + 1;
			} else {
				size = 1;
			}
			office.setQyDeptId(Integer.parseInt(office.getQyDeptParentId() + "" + size));
			office.setSynStatus(0);

			// 同步到微信
			if (office.getSynStatus() == 0 && office.getIsSyntoent() == 1) {
				Department department = new Department();
				department.setId(String.valueOf(office.getQyDeptId()));
				department.setName(office.getName());
				department.setOrder(String.valueOf(office.getSort()));
				department.setParentid(String.valueOf(office.getQyDeptParentId()));
				JwDepartmentAPI.createDepartment(department, accessToken.getAccesstoken());
			}
			office.setSynStatus(1);	// 同步完之后把状态改为已同步
		} else {
			String hiddenisSyntoent = request.getParameter("hiddenisSyntoent");
			if ("0".equals(hiddenisSyntoent)) {			// 第一次保存为不需要同步
				if (!hiddenisSyntoent.equals(office.getIsSyntoent())) {		// 判断如果第一次为不需要同步，这一次为需要同步（执行创建）
					office.setSynStatus(0);
					// 同步到微信
					if (office.getSynStatus() == 0 && office.getIsSyntoent() == 1) {
						Department department = new Department();
						department.setId(String.valueOf(office.getQyDeptId()));
						department.setName(office.getName());
						department.setOrder(String.valueOf(office.getSort()));
						department.setParentid(String.valueOf(office.getQyDeptParentId()));
						JwDepartmentAPI.createDepartment(department, accessToken.getAccesstoken());
					}
					office.setSynStatus(1);	// 同步完之后把状态改为已同步
				}
			} else {									// 第一次保存为需要同步
				if (hiddenisSyntoent.equals(String.valueOf(office.getIsSyntoent()))) {		// 判断如果第一次为需要同步，这一次为需要同步（执行更新）
					office.setSynStatus(2);
					// 同步到微信
					if (office.getSynStatus() == 2 && office.getIsSyntoent() == 1) {
						Department department = new Department();
						department.setId(String.valueOf(office.getQyDeptId()));
						department.setName(office.getName());
						department.setOrder(String.valueOf(office.getSort()));
						department.setParentid(String.valueOf(office.getQyDeptParentId()));
						JwDepartmentAPI.updateDepart(department, accessToken.getAccesstoken());
					}
					office.setSynStatus(1);	// 同步完之后把状态改为已同步
				} else {													// 判断如果第一次为需要同步，这一次为不需要同步（执行删除）
					office.setSynStatus(3);
					// 同步到微信
					if (office.getSynStatus() == 3 && office.getIsSyntoent() == 0) {
						JwDepartmentAPI.deleteDepart(String.valueOf(office.getQyDeptId()), accessToken.getAccesstoken());
					}
					office.setSynStatus(1);	// 同步完之后把状态改为已同步
				}
			}
		}
		officeService.save(office);
		
		if(office.getChildDeptList()!=null){
			Office childOffice = null;
			for(String id : office.getChildDeptList()){
				childOffice = new Office();
				childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
				childOffice.setParent(office);
				childOffice.setArea(office.getArea());
				childOffice.setType("2");
				childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade())+1));
				childOffice.setUseable(Global.YES);
				officeService.save(childOffice);
			}
		}
		
		j.setSuccess(true);
		j.setMsg("保存机构'" + office.getName() + "'成功");
		j.put("office", office);
		return j;
	}
	
	@ResponseBody
	@RequiresPermissions("sys:office:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Office office) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		office.setDelFlag("1");
		office.setSynStatus(3);
		officeService.deleteLogical(office);
		j.setSuccess(true);
		j.setMsg("删除成功！");
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<Office> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return officeService.getChildren(parentId);
	}
	
	
	
	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(isAll);
		HashSet existIds = new HashSet();
		for(Office o : list){
			existIds.add(o.getId());
		}
		for (int i=0; i<list.size(); i++){
			Office office = list.get(i);
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(office.getId()) && office.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(office.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(office.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(office.getUseable())){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", office.getId());
				if("0".equals(office.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					if(existIds.contains(office.getParentId())){
						map.put("parent", office.getParentId());
					}else{
						map.put("parent", "#");
					}
				}
				map.put("name", office.getName());
				map.put("text", office.getName());
				map.put("type", office.getType());
				if ("1".equals(office.getType()) && "2".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "bootstrapTreeData")
	public List<Map<String, Object>> bootstrapTreeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList(); 
		List<Office> roots = officeService.getChildren("0");
		for(Office root:roots){
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", root.getId());
			map.put("name", root.getName());
			map.put("level", 1);
			deepTree(map, root);
			mapList.add(map);
		}
		return mapList;
	}
	
	public void deepTree(Map<String, Object> map, Office office){
	
		map.put("text", office.getName());
		List<Map<String, Object>> arra = new ArrayList<Map<String, Object>>();
		for(Office child:officeService.getChildren(office.getId())){
			Map<String, Object> childMap = Maps.newHashMap();
			childMap.put("id", child.getId());
			childMap.put("name", child.getName());
			arra.add(childMap);
			deepTree(childMap, child);
		}
		if(arra.size() >0){
			map.put("children", arra);
		}
	}
}
