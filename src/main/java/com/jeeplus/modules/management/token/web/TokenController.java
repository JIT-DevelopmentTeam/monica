/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.token.web;

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
import com.jeeplus.modules.management.token.entity.Token;
import com.jeeplus.modules.management.token.service.TokenService;

/**
 * Token表Controller
 * @author KicoChan
 * @version 2019-08-24
 */
@Controller
@RequestMapping(value = "${adminPath}/management/token/token")
public class TokenController extends BaseController {

	@Autowired
	private TokenService tokenService;
	
	@ModelAttribute
	public Token get(@RequestParam(required=false) String id) {
		Token entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tokenService.get(id);
		}
		if (entity == null){
			entity = new Token();
		}
		return entity;
	}
	
	/**
	 * Token列表页面
	 */
	@RequiresPermissions("management:token:token:list")
	@RequestMapping(value = {"list", ""})
	public String list(Token token, Model model) {
		model.addAttribute("token", token);
		return "modules/management/token/tokenList";
	}
	
		/**
	 * Token列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:token:token:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Token token, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Token> page = tokenService.findPage(new Page<Token>(request, response), token); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑Token表单页面
	 */
	@RequiresPermissions(value={"management:token:token:view","management:token:token:add","management:token:token:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Token token, Model model) {
		model.addAttribute("token", token);
		return "modules/management/token/tokenForm";
	}

	/**
	 * 保存Token
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:token:token:add","management:token:token:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Token token, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(token);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		tokenService.save(token);//保存
		j.setSuccess(true);
		j.setMsg("保存Token成功");
		return j;
	}
	
	/**
	 * 删除Token
	 */
	@ResponseBody
	@RequiresPermissions("management:token:token:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Token token) {
		AjaxJson j = new AjaxJson();
		tokenService.delete(token);
		j.setMsg("删除Token成功");
		return j;
	}
	
	/**
	 * 批量删除Token
	 */
	@ResponseBody
	@RequiresPermissions("management:token:token:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tokenService.delete(tokenService.get(id));
		}
		j.setMsg("删除Token成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:token:token:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Token token, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "Token"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Token> page = tokenService.findPage(new Page<Token>(request, response, -1), token);
    		new ExportExcel("Token", Token.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出Token记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:token:token:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Token> list = ei.getDataList(Token.class);
			for (Token token : list){
				try{
					tokenService.save(token);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条Token记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条Token记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入Token失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入Token数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:token:token:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "Token数据导入模板.xlsx";
    		List<Token> list = Lists.newArrayList(); 
    		new ExportExcel("Token数据", Token.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}