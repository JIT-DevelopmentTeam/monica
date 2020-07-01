/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.token.AccessTokenUtils;
import com.jeeplus.core.persistence.BaseEntity;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.apiurl.entity.ApiUrl;
import com.jeeplus.modules.management.apiurl.service.ApiUrlService;
import com.jeeplus.modules.monitor.utils.Common;
import com.jeeplus.modules.sys.dto.ErpDeptDTO;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.DataRule;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.department.JwDepartmentAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.department.vo.Department;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.user.JwUserAPI;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
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
    private UserMapper userMapper;

	@Autowired
	private ApiUrlService apiUrlService;

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

	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return officeService.get(id);
		}else{
			return new Office();
		}
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
		AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.contactSecret);
		JwDepartmentAPI.deleteDepart(String.valueOf(office.getQyDeptId()), accessToken.getAccesstoken());
		officeService.delete(office);
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

    @ResponseBody
    @RequestMapping(value = "synchronize")
    public AjaxJson synchronize() {
        // 同步拦截
        AjaxJson aj = new AjaxJson();
        try {
            String addressBookAccessToken;
            // 获取缓存通讯录token
            if (CacheUtils.get("addressBookAccessToken") != null) {
                addressBookAccessToken = CacheUtils.get("addressBookAccessToken").toString();
            } else {
                AccessTokenUtils.updateAgentToken();
                if (CacheUtils.get("addressBookAccessToken") == null) {
                    aj.setSuccess(false);
                    aj.setMsg("同步出错,请联系管理员!");
                    return aj;
                } else {
                    addressBookAccessToken = CacheUtils.get("addressBookAccessToken").toString();
                }
            }
            List<Office> delList = new ArrayList<>();
            // 企业微信
            List<Department> departmentList = JwDepartmentAPI.getAllDepartment(addressBookAccessToken);
            // 新增/编辑
            for (Department department : departmentList) {
//                if (!"1".equals(department.getId())) {
                    Office editOffice = new Office();
                    editOffice.setQyDeptId(department.getId());
                    editOffice = officeService.getEntity(editOffice);
                    Office localOffice = saveOffice(department.getId(),department.getName(),department.getParentid(),editOffice);
                    List<com.jeeplus.modules.wxapi.jeecg.qywx.api.user.vo.User> qywxUserList = JwUserAPI.getUsersByDepartid(department.getId(),"0",null,addressBookAccessToken);
                    for (com.jeeplus.modules.wxapi.jeecg.qywx.api.user.vo.User qywxUser : qywxUserList) {
                        saveEnterpriseUser(qywxUser.getUserid(),qywxUser.getName(),qywxUser.getEmail(),null,qywxUser.getMobile(),qywxUser.getPosition(),qywxUser.getGender(),localOffice);
                    }
//                }
            }
            // 删除
            Office office = new Office();
            List<Office> officeList = officeService.findList(office);
            for (Office officeEntity : officeList) {
                boolean isDel = true;
                for (Department department : departmentList) {
                    if (department.getId().equals(officeEntity.getQyDeptId()) || "1".equals(officeEntity.getId())) {
                        isDel = false;
                    }
                }
                if (isDel) {
                    delList.add(officeEntity);
                }
            }
            for (Office delOffice : delList) {
                officeService.delete(delOffice);
            }
        } catch (Exception e) {
            e.printStackTrace();
            aj.setSuccess(false);
            aj.setMsg("同步出错,请联系管理员!");
            return aj;
        }
        aj.setMsg("同步成功!");
        return aj;
    }

    /**
     * 保存/编辑机构
     * @param id 企业应用机构id
     * @param name 机构名称
     * @param parentId 企业应用机构父级id
     * @param office 部门
     */
    private Office saveOffice(String id,String name,String parentId,Office office) {
        if (office == null) {
            // 新增
            Office saveOffice = new Office();
            saveOffice.setIsNewRecord(true);
            saveOffice.setId(id);
            Office parentOffice = new Office();
            parentOffice.setId(parentId);
            saveOffice.setParent(parentOffice);
            saveOffice.setQyDeptId(id);
            saveOffice.setName(name);
            // 中国
            Area area = new Area();
            area.setId("a9beb8c645ff448d89e71f96dc97bc09");
            saveOffice.setArea(area);
            saveOffice.setGrade("1");
            saveOffice.setUseable("1");
            setParentOffice(parentId, saveOffice);
            // 自动获取排序号
            if (StringUtils.isBlank(saveOffice.getId())&&saveOffice.getParent()!=null){
                int size = 0;
                List<Office> list = officeService.findAll();
                for (int i=0; i<list.size(); i++){
                    Office e = list.get(i);
                    if (e.getParent()!=null && e.getParent().getId()!=null
                            && e.getParent().getId().equals(saveOffice.getParent().getId())){
                        size++;
                    }
                }
                saveOffice.setCode(saveOffice.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size+1 : 1), 3, "0"));
            }
            officeService.save(saveOffice);
            return saveOffice;
        } else {
            // 修改
            office.setName(name);
            setParentOffice(parentId, office);
            officeService.save(office);
        }
        return office;
    }

    /**
     * 设置父级机构
     * @param parentId
     * @param office
     */
    private void setParentOffice(String parentId,Office office){
        if ("1".equals(parentId)) {
            Office parent = officeService.get("1");
            office.setParent(parent);
        } else {
            Office parentOffice = new Office();
            parentOffice.setQyDeptId(parentId);
            Office parent = officeService.getEntity(parentOffice);
            if (parent != null) {
                office.setParent(parent);
            }
        }
    }

    /**
     * 保存/编辑企业通讯录应用用户
     * @param userId 企业通讯录应用用户id
     * @param name 用户名
     * @param email 邮箱
     * @param phone 电话
     * @param mobile 手机
     * @param position 职务
     * @param sex 性别(0:待定,1:男;2:女)
     * @param office 本地部门机构
     */
    private void saveEnterpriseUser(String userId,String name,String email,String phone,String mobile,String position,String sex,Office office) throws BadHanyuPinyinOutputFormatCombination {
        User editUser = userMapper.getByQyUserId(userId);
        if (editUser == null) {
            // 新增
            User saveUser = new User();
            saveUser.setCompany(UserUtils.getUser().getCompany());
            saveUser.setOffice(office);
            saveUser.setLoginName(userId);
            saveUser.setPassword(SystemService.entryptPassword("123456"));
            saveUser.setQyUserId(userId);
            saveUser.setNo(userId);
            saveUser.setName(name);
            saveUser.setEmail(email);
            saveUser.setPhone(phone);
            saveUser.setMobile(mobile);
            saveUser.setPhoto("/jitcrm/static/common/images/flat-avatar.png");
            saveUser.setId(userId);
            saveUser.setIsNewRecord(true);
            saveUser.setCreateBy(UserUtils.getUser());
            saveUser.setCreateDate(new Date());
            userMapper.insert(saveUser);
        } else {
            // 编辑
            editUser.setOffice(office);
            editUser.setLoginName(userId);
            editUser.setNo(userId);
            editUser.setName(name);
            editUser.setEmail(email);
            editUser.setPhone(phone);
            editUser.setMobile(mobile);
            userMapper.update(editUser);
        }
    }

	/**
	 * 跳转ERP部门列表页
	 * @return
	 */
	@RequestMapping(value = {"erpDeptList", ""})
	public String erpDeptList(String id, Model model) {
		model.addAttribute("id", id);
		return "modules/sys/office/erpDeptList";
	}

	@ResponseBody
	@RequestMapping(value = "erpDeptData")
	public Map<String, Object> erpDeptData(ErpDeptDTO erpDeptDTO, HttpServletRequest request, HttpServletResponse response, Model model) {
		ApiUrl apiUrlList = apiUrlService.getByUsefulness("9");
		ApiUrl apiUrlTotal = apiUrlService.getByUsefulness("11");
		String pageNo = request.getParameter("pageNo");
		JSONArray jsonArray = Common.executeInter(apiUrlList.getUrl() + "&currentPage=" + pageNo, apiUrlList.getProtocol());
		JSONArray jsonArrayTotal = Common.executeInter(apiUrlTotal.getUrl(), apiUrlTotal.getProtocol());
		List<ErpDeptDTO> erpDeptDTOList = JSONArray.toList(jsonArray, erpDeptDTO, new JsonConfig());
		Page<ErpDeptDTO> page = findPage(new Page<ErpDeptDTO>(request, response), erpDeptDTO, erpDeptDTOList);
		for (int i = 0; i < jsonArrayTotal.size(); i++) {
			page.setCount(Integer.parseInt(jsonArrayTotal.get(i).toString()));
		}
		return getBootstrapData(page);
	}

	/**
	 * 数据范围过滤
	 * @param entity 当前过滤的实体类
	 */
	public static void dataRuleFilter(BaseEntity<?> entity) {

		entity.setCurrentUser(UserUtils.getUser());
		List<DataRule> dataRuleList = UserUtils.getDataRuleList();

		// 如果是超级管理员，则不过滤数据
		if (dataRuleList.size() == 0) {
			return;
		}

		// 数据范围
		StringBuilder sqlString = new StringBuilder();


		for(DataRule dataRule : dataRuleList){
			if(entity.getClass().getSimpleName().equals(dataRule.getClassName())){
				sqlString.append(dataRule.getDataScopeSql());
			}

		}

		entity.setDataScope(sqlString.toString());

	}

	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @return
	 */
	public Page<ErpDeptDTO> findPage(Page<ErpDeptDTO> page, ErpDeptDTO erpDeptDTO, List<ErpDeptDTO> list) {
		dataRuleFilter(erpDeptDTO);
		erpDeptDTO.setPage(page);
		page.setList(list);
		return page;
	}

	@ResponseBody
	@RequestMapping(value = "/updateErp")
	public AjaxJson updateErp(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String id = request.getParameter("id");
		String number = request.getParameter("number");
		String name = request.getParameter("name");
		officeService.updateERP(id, number, name);
		j.setSuccess(true);
		j.setMsg("绑定成功");
		return j;
	}

}
