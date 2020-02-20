/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.sobillandentry.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.time.DateFormatUtil;
import com.jeeplus.core.persistence.BaseEntity;
import com.jeeplus.modules.management.sobillandentry.entity.Sobillentry;
import com.jeeplus.modules.management.sobillandentry.mapper.SobillentryMapper;
import com.jeeplus.modules.sys.entity.DataRule;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import io.swagger.models.auth.In;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.service.SobillService;

/**
 * 订单模块Controller
 * @author KicoChan
 * @version 2019-05-30
 */
@Controller
@RequestMapping(value = "${adminPath}/management/sobillandentry/sobill")
public class SobillController extends BaseController {

	@Autowired
	private SobillService sobillService;

	@Autowired
    private SobillentryMapper sobillentryMapper;
	
	@ModelAttribute
	public Sobill get(@RequestParam(required=false) String id) {
		Sobill entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sobillService.get(id);
		}
		if (entity == null){
			entity = new Sobill();
		}
		return entity;
	}
	
	/**
	 * 订单列表页面
	 */
	@RequiresPermissions("management:sobillandentry:sobill:list")
	@RequestMapping(value = {"list", ""})
	public String list(Sobill sobill, Model model) {
		model.addAttribute("sobill", sobill);
		return "modules/management/sobillandentry/sobillList";
	}
	
		/**
	 * 订单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:sobillandentry:sobill:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Sobill sobill, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Sobill> page = sobillService.findPage(new Page<Sobill>(request, response), sobill); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑订单表单页面
	 */
	@RequiresPermissions(value={"management:sobillandentry:sobill:view","management:sobillandentry:sobill:add","management:sobillandentry:sobill:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Sobill sobill, Model model) {
		if (StringUtils.isNotBlank(sobill.getId())){
            StringBuffer itemIdsStr = new StringBuffer();
            for (Sobillentry sobillentry : sobill.getSobillentryList()) {
                itemIdsStr.append(sobillentry.getItemId()+",");
            }
            if (itemIdsStr.length() > 0){
                itemIdsStr = itemIdsStr.deleteCharAt(itemIdsStr.length()-1);
                model.addAttribute("itemIdsStr",itemIdsStr.toString());
            }
		}
		model.addAttribute("sobill", sobill);
		return "modules/management/sobillandentry/sobillForm";
	}

	/**
	 * 保存订单
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:sobillandentry:sobill:add","management:sobillandentry:sobill:edit"},logical=Logical.OR)
	@RequestMapping(value = "save",produces = {"application/json;charset=UTF-8"})
	public AjaxJson save(@RequestBody Object object) throws Exception{
		AjaxJson aj = new AjaxJson();
        JSONObject jsonObject = JSONObject.fromObject(object);
        if (jsonObject.getString("id") == null || "".equals(jsonObject.getString("id"))){
            Sobill sobill = new Sobill();
            sobill.setEmplId(jsonObject.getString("emplId"));
            sobill.setBillNo(jsonObject.getString("billNo"));
            sobill.setCustId(jsonObject.getString("custId"));
            sobill.setNeedTime(DateUtils.parseDate(jsonObject.getString("needTime")));
            sobill.setType(jsonObject.get("type").toString());
            sobill.setCurrencyId(Integer.parseInt(jsonObject.get("currencyId").toString()));
            sobill.setAmount(Double.parseDouble(jsonObject.get("amount").toString()));
            sobill.setRemarks(jsonObject.getString("remarks"));
            sobill.setSynStatus(0);
            sobill.setStatus(0);
            sobill.setCancellation(0);
            sobill.setCheckStatus(0);
            sobill.setId(IdGen.uuid());
            sobill.setIsNewRecord(true);
            List<Sobillentry> sobillentryList = new ArrayList<>();
            JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("sobillentryList"));
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject sobillEntryObject = jsonArray.getJSONObject(i);
                Sobillentry sobillentry = new Sobillentry();
                sobillentry.setSobillId(sobill);
                sobillentry.setItemId(sobillEntryObject.getString("itemId"));
                sobillentry.setAuxqty(Double.parseDouble(sobillEntryObject.get("auxqty").toString()));
                sobillentry.setRowId(Integer.parseInt(sobillEntryObject.get("rowId").toString()));
                sobillentry.setRemarks(sobillEntryObject.getString("remarks"));
                sobillentry.setDelFlag("0");
                // id设置为空字符串才会插入数据
                sobillentry.setId("");
                sobillentryList.add(sobillentry);
            }
            sobill.setSobillentryList(sobillentryList);
            sobillService.save(sobill);
        } else {
            Sobill sobill = sobillService.get(jsonObject.getString("id"));
            if (sobill != null){
                sobill.setEmplId(jsonObject.getString("emplId"));
                sobill.setCustId(jsonObject.getString("custId"));
                sobill.setNeedTime(DateUtils.parseDate(jsonObject.getString("needTime")));
                sobill.setType(jsonObject.get("type").toString());
                sobill.setNeedTime(DateUtils.parseDate(jsonObject.get("needTime")));
                sobill.setCurrencyId(Integer.parseInt(jsonObject.get("currencyId").toString()));
                sobill.setAmount(Double.parseDouble(jsonObject.get("amount").toString()));
                sobill.setRemarks(jsonObject.getString("remarks"));
                List<Sobillentry> sobillentryList = sobill.getSobillentryList();
                JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("sobillentryList"));
                checkDelect(sobillentryList, jsonArray);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject sobillEntryObject = jsonArray.getJSONObject(i);
                    boolean exist = false;
                    for (int j = 0; j < sobillentryList.size(); j++) {
                        if (sobillentryList.get(j).getItemId().equals(sobillEntryObject.getString("itemId"))){
                            exist = true;
                            sobillentryList.get(j).setAuxqty(Double.parseDouble(sobillEntryObject.get("auxqty").toString()));
                            sobillentryList.get(j).setRowId(Integer.parseInt(sobillEntryObject.get("rowId").toString()));
                            sobillentryList.get(j).setRemarks(sobillEntryObject.getString("remarks"));
                        }
                    }
                    if (!exist){
                        Sobillentry sobillentry = new Sobillentry();
                        sobillentry.setSobillId(sobill);
                        sobillentry.setItemId(sobillEntryObject.getString("itemId"));
                        sobillentry.setAuxqty(Double.parseDouble(sobillEntryObject.get("auxqty").toString()));
                        sobillentry.setRowId(Integer.parseInt(sobillEntryObject.get("rowId").toString()));
                        sobillentry.setRemarks(sobillEntryObject.getString("remarks"));
                        sobillentry.setDelFlag("0");
                        // id设置为空字符串才会插入数据
                        sobillentry.setId("");
                        sobillentryList.add(sobillentry);
                    }
                }
            }
            sobillService.save(sobill);
        }
		aj.setSuccess(true);
		aj.setMsg("保存订单成功");
		return aj;
	}

    /**
     * 检查数据存在则删除
     * @param sobillentryList
     * @param jsonArray
     */
    public static void checkDelect(List<Sobillentry> sobillentryList, JSONArray jsonArray) {
        for (int i = 0; i < sobillentryList.size(); i++) {
            boolean delect = true;
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject sobillEntryObject = jsonArray.getJSONObject(j);
                if (sobillEntryObject.getString("itemId").equals(sobillentryList.get(i).getItemId())){
                    delect = false;
                }
            }
            if (delect) {
                sobillentryList.get(i).setDelFlag("1");
            }
        }
    }

    /**
	 * 删除订单
	 */
	@ResponseBody
	@RequiresPermissions("management:sobillandentry:sobill:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Sobill sobill) {
		AjaxJson j = new AjaxJson();
		sobillService.delete(sobill);
		j.setMsg("删除订单成功");
		return j;
	}
	
	/**
	 * 批量删除订单
	 */
	@ResponseBody
	@RequiresPermissions("management:sobillandentry:sobill:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sobillService.delete(sobillService.get(id));
		}
		j.setMsg("删除订单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:sobillandentry:sobill:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Sobill sobill, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Sobill> page = sobillService.findPage(new Page<Sobill>(request, response, -1), sobill);
    		new ExportExcel("订单", Sobill.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出订单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public Sobill detail(String id) {
		return sobillService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:sobillandentry:sobill:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Sobill> list = ei.getDataList(Sobill.class);
			for (Sobill sobill : list){
				try{
					sobillService.save(sobill);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条订单记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入订单失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入订单数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:sobillandentry:sobill:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单数据导入模板.xlsx";
    		List<Sobill> list = Lists.newArrayList(); 
    		new ExportExcel("订单数据", Sobill.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

	/**
	 * 审核订单
	 * @param id
	 * @param sobill
	 * @return
	 */
    @RequestMapping(value = "checkOrder")
	@ResponseBody
	public AjaxJson checkOrder(String id,Sobill sobill){
		AjaxJson aj = new AjaxJson();
		sobill = sobillService.get(id);
		if ("1".equals(sobill.getCheckStatus().toString())){
			aj.setSuccess(false);
			aj.setMsg("审核失败(请检查该订单是否已审核)!");
		} else {
			sobill.setCheckerId(UserUtils.getUser().getId());
			sobill.setCheckStatus(1);
			sobill.setCheckTime(new Date());
			sobillService.save(sobill);
			aj.setSuccess(true);
			aj.setMsg("审核成功!");
		}
		return aj;
	}

    /**
     * 订单明细列表
     * @param sobillentry
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "getSobillEntryListBySobillId")
    @ResponseBody
    public Map<String,Object> getSobillEntryListBySobillId(Sobillentry sobillentry, HttpServletRequest request, HttpServletResponse response){
        sobillentry.setDelFlag("0");
        if("".equals(sobillentry.getSobillId().getId().trim())){
            sobillentry.getSobillId().setId(null);
        }
        Page<Sobillentry> page = findPage(new Page<Sobillentry>(request, response), sobillentry);
        return getBootstrapData(page);
    }

    /**
     * 查询分页数据
     * @param page 分页对象
     * @param sobillentry
     * @return
     */
    private Page<Sobillentry> findPage(Page<Sobillentry> page, Sobillentry sobillentry) {
        sobillentry.setPage(page);
        if (sobillentry.getSobillId().getId() == null || "".equals(sobillentry.getSobillId().getId())){
            page.setList(null);
        } else {
            page.setList(sobillentryMapper.findList(sobillentry));
        }
        return page;
    }


}