/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.wxuser.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.wxuser.entity.WxUser;
import com.jeeplus.modules.management.wxuser.service.WxUserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 微信用户Controller
 * @author Vigny
 * @version 2020-05-25
 */
@Controller
@RequestMapping(value = "${adminPath}/management/wxuser/wxUser")
public class WxUserController extends BaseController {

	@Autowired
	private WxUserService wxUserService;
	
	@ModelAttribute
	public WxUser get(@RequestParam(required=false) String id) {
		WxUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wxUserService.get(id);
		}
		if (entity == null){
			entity = new WxUser();
		}
		return entity;
	}
	
	/**
	 * 微信用户列表页面
	 */
	@RequiresPermissions("management:wxuser:wxUser:list")
	@RequestMapping(value = {"list", ""})
	public String list(WxUser wxUser, Model model) {
		model.addAttribute("wxUser", wxUser);
		return "modules/management/wxuser/wxUserList";
	}
	
		/**
	 * 微信用户列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:wxuser:wxUser:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WxUser wxUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WxUser> page = wxUserService.findPage(new Page<WxUser>(request, response), wxUser); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑微信用户表单页面
	 */
	@RequiresPermissions(value={"management:wxuser:wxUser:view","management:wxuser:wxUser:add","management:wxuser:wxUser:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WxUser wxUser, Model model) {
		model.addAttribute("wxUser", wxUser);
		return "modules/management/wxuser/wxUserForm";
	}

	/**
	 * 保存微信用户
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:wxuser:wxUser:add","management:wxuser:wxUser:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(WxUser wxUser, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(wxUser);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		wxUserService.save(wxUser);//保存
		j.setSuccess(true);
		j.setMsg("保存微信用户成功");
		return j;
	}
	
	/**
	 * 删除微信用户
	 */
	@ResponseBody
	@RequiresPermissions("management:wxuser:wxUser:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WxUser wxUser) {
		AjaxJson j = new AjaxJson();
		wxUserService.delete(wxUser);
		j.setMsg("删除微信用户成功");
		return j;
	}
	
	/**
	 * 批量删除微信用户
	 */
	@ResponseBody
	@RequiresPermissions("management:wxuser:wxUser:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			wxUserService.delete(wxUserService.get(id));
		}
		j.setMsg("删除微信用户成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:wxuser:wxUser:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WxUser wxUser, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "微信用户"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WxUser> page = wxUserService.findPage(new Page<WxUser>(request, response, -1), wxUser);
    		new ExportExcel("微信用户", WxUser.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出微信用户记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:wxuser:wxUser:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WxUser> list = ei.getDataList(WxUser.class);
			for (WxUser wxUser : list){
				try{
					wxUserService.save(wxUser);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条微信用户记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条微信用户记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入微信用户失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入微信用户数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:wxuser:wxUser:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "微信用户数据导入模板.xlsx";
    		List<WxUser> list = Lists.newArrayList(); 
    		new ExportExcel("微信用户数据", WxUser.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}