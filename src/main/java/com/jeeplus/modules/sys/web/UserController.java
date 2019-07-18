/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.beanvalidator.BeanValidators;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.monitor.utils.Common;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.SystemConfig;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemConfigService;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.tools.utils.TwoDimensionCode;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.user.JwUserAPI;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 * @author jeeplus
 * @version 2016-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemConfigService systemConfigService;
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private OfficeService officeService;

	public String CNToPinyin(String ChineseLanguage) throws BadHanyuPinyinOutputFormatCombination {
		char[] cl_chars = ChineseLanguage.trim().toCharArray();
		String hanyupinyin = "";
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部小写
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V) ;
		try {
			for (int i=0; i<cl_chars.length; i++){
				if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")){// 如果字符是中文,则将中文转为汉语拼音
					hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0].substring(0,1).toUpperCase()
							+ PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0].substring(1);
				} else {// 如果字符不是中文,则不转换
					hanyupinyin += cl_chars[i];
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			//hanyupinyin = ChineseLanguage+ ":字符不能转成汉语拼音";
			System.out.println("字符不能转成汉语拼音");
		}
		return hanyupinyin;
	}

	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getUser(id);
		}else{
			return new User();
		}
	}

	/**
	 * 同步用户
	 */
	@ResponseBody
	@RequestMapping(value = "synUser")
	public Map<String,Object>  synUser(String parentId) throws Exception{
		Map<String,Object> json = new HashMap<>();
		JSONArray jsonarr =
				Common.executeInter("http://192.168.1.252:8080/monica_erp/erp_get/erp_empl?token_value=20190603","POST");

		JSONObject jsonObject = new JSONObject();
		Office company = officeService.getByName("莫尔卡");			// 按企业名查询企业资料
		Office dept = new Office();
		for (int i = 0; i < jsonarr.size(); i++) {
			jsonObject = jsonarr.getJSONObject(i);
			User user = new User();
			user.setId(jsonObject.getString("id"));
			user.setCompany(company);
			dept.setId(jsonObject.getString("f_deptid"));
			user.setOffice(dept);
			user.setNo(jsonObject.getString("f_number"));
			user.setName(jsonObject.getString("f_name"));
			user.setLoginName(CNToPinyin(jsonObject.getString("f_name")));
			user.setPassword(SystemService.entryptPassword("123456"));
			user.setLoginFlag("1");
			user.setSynStatus(0);
			user.setIsSyntoent(1);

			//生成用户二维码，使用登录名
			String realPath = Global.getAttachmentDir()+ "qrcode/";
			FileUtils.createDirectory(realPath);
			String name= user.getId()+".png"; //encoderImgId此处二维码的图片名
			String filePath = realPath + name;  //存放路径
			TwoDimensionCode.encoderQRCode(user.getLoginName(), filePath, "png");//执行生成二维码
			user.setQrCode(Global.getAttachmentUrl()  + "qrcode/"+name);

			// 保存用户信息
			systemService.saveUser(user, true);
		}
		json.put("msg","success");
		return json;
	}

	/**
	 * 同步用户到企业微信
	 */
	@ResponseBody
	@RequestMapping(value = "synToQYWeChat")
	public Map<String,Object>  synToQYWeChat() throws Exception{
		Map<String,Object> json = new HashMap<>();
		List<User> unSynList = systemService.findBySynStatus(0, 0, 1); // 未同步的员工
		List<User> needUpdateList = systemService.findBySynStatus(0, 2, 1); // 需修改的员工
		List<User> needDeleteList = systemService.findBySynStatus(0, 3, 1); // 需删除的员工
		AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId,JwParamesAPI.contactSecret);
        if (unSynList.size() > 0) {
			for (int i = 0; i < unSynList.size(); i++) {
				com.jeeplus.modules.wxapi.jeecg.qywx.api.user.vo.User user = new com.jeeplus.modules.wxapi.jeecg.qywx.api.user.vo.User();
				String useId = CNToPinyin(unSynList.get(i).getName());
				user.setUserid(useId);
				user.setName(unSynList.get(i).getName());
				user.setMobile(unSynList.get(i).getMobile());
				String officeId = unSynList.get(i).getOffice().getId();
				Office office = officeService.get(officeId);
				user.setDepartment(new Integer[]{office.getQyDeptId()});
				int result = JwUserAPI.createUser(user, accessToken.getAccesstoken());
				System.out.println(result==0 ? unSynList.get(i).getName() + "创建成功！" : "失败：" + result);
				if (result == 0) {
					userMapper.updateSynstatus(1, unSynList.get(i).getId());
				}
			}
		}
        if (needUpdateList.size() > 0) {
			for (int i = 0; i < needUpdateList.size(); i++) {
				com.jeeplus.modules.wxapi.jeecg.qywx.api.user.vo.User user = new com.jeeplus.modules.wxapi.jeecg.qywx.api.user.vo.User();
				String useId = CNToPinyin(needUpdateList.get(i).getName());
				user.setUserid(useId);
				user.setName(needUpdateList.get(i).getName());
				user.setMobile(needUpdateList.get(i).getMobile());
				String officeId = needUpdateList.get(i).getOffice().getId();
				Office office = officeService.get(officeId);
				user.setDepartment(new Integer[]{office.getQyDeptId()});
				int result = JwUserAPI.updateUser(user, accessToken.getAccesstoken());
				System.out.println(result==0 ? needUpdateList.get(i).getName() + "修改成功！" : "失败：" + result);
				if (result == 0) {
					userMapper.updateSynstatus(1, needUpdateList.get(i).getId());
				}
			}
		}
        if (needDeleteList.size() > 0) {
			for (int i = 0; i < needDeleteList.size(); i++) {
				String useId = CNToPinyin(needDeleteList.get(i).getName());
				int result = JwUserAPI.deleteUser(useId, accessToken.getAccesstoken());
				System.out.println(result==0 ? needDeleteList.get(i).getName() + "删除成功！" : "失败：" + result);
				if (result == 0) {
					userMapper.updateSynstatus(1, needDeleteList.get(i).getId());
				}
			}
		}
		return json;
	}

	@RequiresPermissions("sys:user:index")
	@RequestMapping(value = "index")
	public String index(User user, Model model) {
		return "modules/sys/user/userIndex";
	}
	
	@RequiresPermissions("sys:user:index")
	@RequestMapping(value = "userSelect")
	public String userSelect(boolean isMultiSelect, Model model) {
		model.addAttribute("isMultiSelect", isMultiSelect);
		return "modules/common/userSelect";
	}

	@RequiresPermissions("sys:user:index")
	@ResponseBody
	@RequestMapping(value = {"list", ""})
	public Map<String, Object> list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
		return getBootstrapData(page);
	}

	
	@RequiresPermissions(value={"sys:user:view","sys:user:add","sys:user:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		if (user.getCompany()==null || user.getCompany().getId()==null){
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice()==null || user.getOffice().getId()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findRole(new Role()));
		return "modules/sys/user/userForm";
	}

	@RequiresPermissions(value={"sys:user:add","sys:user:edit"},logical=Logical.OR)
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(User user, HttpServletRequest request, Model model) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作!");
			return j;
		}
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
		}
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(user);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))){
			j.setSuccess(false);
			j.setMsg("保存用户'" + user.getLoginName() + "'失败，登录名已存在!");
			return j;
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		//生成用户二维码，使用登录名
		String realPath = Global.getAttachmentDir()+ "qrcode/";
		FileUtils.createDirectory(realPath);
		String name= user.getId()+".png"; //encoderImgId此处二维码的图片名
		String filePath = realPath + name;  //存放路径
		TwoDimensionCode.encoderQRCode(user.getLoginName(), filePath, "png");//执行生成二维码
		user.setQrCode(Global.getAttachmentUrl()  + "qrcode/"+name);
		if ("".equals(user.getId())) {
			user.setSynStatus(0);
		} else {
			user.setSynStatus(2);
		}
		// 保存用户信息
		systemService.saveUser(user);
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			UserUtils.clearCache();
			//UserUtils.getCacheMap().clear();
		}
		j.setSuccess(true);
		j.setMsg("保存用户'" + user.getLoginName() + "'成功!");
		return j;
	}
	
	@ResponseBody
	@RequiresPermissions("sys:user:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(User user) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作!");
			return j;
		}
		if (UserUtils.getUser().getId().equals(user.getId())){
			j.setSuccess(false);
			j.setMsg("删除失败，不允许删除当前用户!");
			return j;
		}else if (User.isAdmin(user.getId())){
			j.setSuccess(false);
			j.setMsg("删除失败，不允许删除超级管理员!");
			return j;
		}else{
			AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.contactSecret);
			try {
				String useId = CNToPinyin(user.getName());
				JwUserAPI.deleteUser(useId, accessToken.getAccesstoken());
			} catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
				badHanyuPinyinOutputFormatCombination.printStackTrace();
			}
			systemService.deleteUser(user);//删除用户成功
			j.setSuccess(true);
			j.setMsg("删除成功!");
			return j;
		}
	}
	
	/**
	 * 批量删除用户
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		String idArray[] =ids.split(",");
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作!");
			return j;
		}
		for(String id : idArray){
			User user = systemService.getUser(id);
			if (UserUtils.getUser().getId().equals(user.getId())){
				j.setSuccess(false);
				j.setMsg("删除失败，不允许删除当前用户!");
			}else if (User.isAdmin(user.getId())){
				j.setSuccess(false);
				j.setMsg("删除失败，不允许删除超级管理员!");//删除用户失败, 不允许删除超级管理员用户
			}else{
				j.setSuccess(true);
				j.setMsg("删除成功!");
				systemService.deleteUser(user);//删除用户成功
			}
		}
		return j;
	}
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:export")
    @RequestMapping(value = "export")
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
    		new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }

	/**
	 * 导入用户数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public AjaxJson importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class);
			for (User user : list){
				try{
					if ("true".equals(checkLoginName("", user.getLoginName()))){
						user.setPassword(SystemService.entryptPassword("123456"));
						BeanValidators.validateWithException(validator, user);
						systemService.saveUser(user);
						successNum++;
					}else{
						failureMsg.append("<br/>登录名 "+user.getLoginName()+" 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>登录名 "+user.getLoginName()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>登录名 "+user.getLoginName()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			j.setMsg("已成功导入 "+successNum+" 条用户"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入用户失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入用户数据模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户数据导入模板.xlsx";
    		List<User> list = Lists.newArrayList(); list.add(UserUtils.getUser());
    		new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }

	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value={"sys:user:add","sys:user:edit"},logical=Logical.OR)
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 用户信息显示
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "info")
	public String info(HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/user/userInfo";
	}
	
	/**
	 * 用户信息显示编辑保存
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "infoEdit")
	public String infoEdit(User user, boolean __ajax, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/user/userInfo";
			}
			if(user.getName() !=null )
				currentUser.setName(user.getName());
			if(user.getEmail() !=null )
				currentUser.setEmail(user.getEmail());
			if(user.getPhone() !=null )
				currentUser.setPhone(user.getPhone());
			if(user.getMobile() !=null )
				currentUser.setMobile(user.getMobile());
			if(user.getRemarks() !=null )
				currentUser.setRemarks(user.getRemarks());
//			if(user.getPhoto() !=null )
//				currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			if(__ajax){//手机访问
				AjaxJson j = new AjaxJson();
				j.setSuccess(true);
				j.setMsg("修改个人资料成功!");
				return renderString(response, j);
			}
			model.addAttribute("user", currentUser);
			model.addAttribute("Global", new Global());
			model.addAttribute("message", "保存用户信息成功");
			return "modules/sys/user/userInfo";
		}
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/user/userInfoEdit";
	}

	
	/**
	 * 用户头像显示编辑保存
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "imageEdit")
	public String imageEdit(User user, boolean __ajax, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/user/userInfo";
			}
			if(user.getPhoto() !=null )
				currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			if(__ajax){//手机访问
				AjaxJson j = new AjaxJson();
				j.setSuccess(true);
				j.setMsg("修改个人头像成功!");
				return renderString(response, j);
			}
			model.addAttribute("message", "保存用户信息成功");
			return "modules/sys/user/userInfo";
		}
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/user/userImageEdit";
	}
	/**
	 * 用户头像显示编辑保存
	 * @param user
	 * @param model
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "imageUpload")
	public String imageUpload( HttpServletRequest request, HttpServletResponse response,MultipartFile file) throws IllegalStateException, IOException {
		User currentUser = UserUtils.getUser();
		
		// 判断文件是否为空  
        if (!file.isEmpty()) {  
                // 文件保存路径  
            	String realPath =Global.getAttachmentDir() + "sys/user/images/" ;
                // 转存文件  
            	FileUtils.createDirectory(realPath);
            	file.transferTo(new File( realPath +  file.getOriginalFilename()));
            	currentUser.setPhoto(Global.getAttachmentUrl()+ "sys/user/images/" + file.getOriginalFilename());
    			systemService.updateUserInfo(currentUser);
        }  

		return "modules/sys/user/userImageEdit";
	}

	/**
	 * 返回用户信息
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "infoData")
	public AjaxJson infoData() {
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		j.setErrorCode("-1");
		j.setMsg("获取个人信息成功!");
		j.put("data", UserUtils.getUser());
		return j;
	}
	
	
	
	/**
	 * 修改个人用户密码
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "modifyPwd")
	public String modifyPwd(String oldPassword, String newPassword, Model model) {
		return "modules/sys/user/userModifyPwd";
	}
	
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "savePwd")
	public AjaxJson savePwd(String oldPassword, String newPassword, Model model) {
		AjaxJson j = new AjaxJson();
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if(Global.isDemoMode()){
				j.setSuccess(false);
				j.setMsg("演示模式，不允许操作！");
				return j;
			}
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				j.setSuccess(true);
				j.setMsg("修改密码成功！");
				return j;
			}else{
				j.setSuccess(false);
				j.setMsg("修改密码失败，旧密码错误！");
				return j;
			}
		}
		j.setSuccess(false);
		j.setMsg("参数错误！");
		return j;
	}
	
	
	/**
	 * 保存签名
	 */
	@ResponseBody
	@RequestMapping(value = "saveSign")
	public AjaxJson saveSign(User user, boolean __ajax, HttpServletResponse response, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		User currentUser = UserUtils.getUser();
		currentUser.setSign(user.getSign());
		systemService.updateUserInfo(currentUser);
		j.setMsg("设置签名成功");
		return j;
	}
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = systemService.findUserByOfficeId(officeId);
		for (int i=0; i<list.size(); i++){
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_"+e.getId());
			map.put("pId", officeId);
			map.put("name", StringUtils.replace(e.getName(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
    
	/**
	 * web端ajax验证用户名是否可用
	 * @param loginName
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "validateLoginName")
	public boolean validateLoginName(String loginName, HttpServletResponse response) {
		
	    User user =  userMapper.findUniqueByProperty("login_name", loginName);
	    if(user == null){
	    	return true;
	    }else{
		    return false;
	    }
	    
	}
	
	/**
	 * web端ajax验证手机号是否可以注册（数据库中不存在）
	 */
	@ResponseBody
	@RequestMapping(value = "validateMobile")
	public boolean validateMobile(String mobile, HttpServletResponse response, Model model) {
		  User user =  userMapper.findUniqueByProperty("mobile", mobile);
		    if(user == null){
		    	return true;
		    }else{
			    return false;
		    }
	}
	
	/**
	 * web端ajax验证手机号是否已经注册（数据库中已存在）
	 */
	@ResponseBody
	@RequestMapping(value = "validateMobileExist")
	public boolean validateMobileExist(String mobile, HttpServletResponse response, Model model) {
		  User user =  userMapper.findUniqueByProperty("mobile", mobile);
		    if(user != null){
		    	return true;
		    }else{
			    return false;
		    }
	}
	
	@ResponseBody
	@RequestMapping(value = "resetPassword")
	public AjaxJson resetPassword(String mobile, HttpServletResponse response, Model model) {
		SystemConfig config = systemConfigService.get("1");//获取短信配置的用户名和密码
		AjaxJson j = new AjaxJson();
		if(userMapper.findUniqueByProperty("mobile", mobile) == null){
			j.setSuccess(false);
			j.setMsg("手机号不存在!");
			j.setErrorCode("1");
			return j;
		}
		User user =  userMapper.findUniqueByProperty("mobile", mobile);
		String newPassword = String.valueOf((int) (Math.random() * 900000 + 100000));
		try {
			String result = UserUtils.sendPass(config.getSmsName(), config.getSmsPassword(), mobile, newPassword);
			if (!result.equals("100")) {
				j.setSuccess(false);
				j.setErrorCode("2");
				j.setMsg("短信发送失败，密码重置失败，错误代码："+result+"，请联系管理员。");
			}else{
				j.setSuccess(true);
				j.setErrorCode("-1");
				j.setMsg("短信发送成功，密码重置成功!");
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
			}
		} catch (IOException e) {
			j.setSuccess(false);
			j.setErrorCode("3");
			j.setMsg("因未知原因导致短信发送失败，请联系管理员。");
		}
		return j;
	}
	
}
