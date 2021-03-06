/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.warehouse.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.BaseEntity;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.apiurl.entity.ApiUrl;
import com.jeeplus.modules.management.apiurl.service.ApiUrlService;
import com.jeeplus.modules.management.warehouse.entity.Stock;
import com.jeeplus.modules.management.warehouse.service.StockService;
import com.jeeplus.modules.monitor.utils.Common;
import com.jeeplus.modules.sys.entity.DataRule;
import com.jeeplus.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
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
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 库存查询Controller
 * @author Vigny
 * @version 2019-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/management/warehouse/stock")
public class StockController extends BaseController {

	@Autowired
	private StockService stockService;

	@Autowired
	private ApiUrlService apiUrlService;
	
	@ModelAttribute
	public Stock get(@RequestParam(required=false) String id) {
		Stock entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = stockService.get(id);
		}
		if (entity == null){
			entity = new Stock();
		}
		return entity;
	}
	
	/**
	 * 库存查询列表页面
	 */
	@RequiresPermissions("management:warehouse:stock:list")
	@RequestMapping(value = {"list", ""})
	public String list(Stock stock, Model model) {
		model.addAttribute("stock", stock);
		return "modules/management/warehouse/stockList";
	}
	
		/**
	 * 库存查询列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:warehouse:stock:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Stock stock, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		ApiUrl apiUrlList = apiUrlService.getByUsefulness("5");
		if (apiUrlList == null || StringUtils.isBlank(apiUrlList.getUrl())) {
			throw new Exception("请检查库存列表查询接口配置是否准确!");
		}
		ApiUrl apiUrlTotal = apiUrlService.getByUsefulness("6");
		if (apiUrlTotal == null || StringUtils.isBlank(apiUrlTotal.getUrl())) {
			throw new Exception("请检查库存列表总量查询接口配置是否准确!");
		}
		String itemClassNumber = request.getParameter("itemClassNumber");
		String item = request.getParameter("item");
		String pageNo = request.getParameter("pageNo");
		JSONArray jsonarr =
				Common.executeInter(apiUrlList.getUrl() + "&itemClassNumber=" + itemClassNumber + "&item=" + item + "&batchNum=" + stock.getBatchNumber() + "&level=" + stock.getLevel() + "&colorNum=" + stock.getColorNumber() + "&warehouse=" + URLEncoder.encode(stock.getWarehouse(), "utf-8") + "&currentPage=" + pageNo,"POST");
		JSONArray jsonarrTotal =
				Common.executeInter(apiUrlTotal.getUrl() + "&itemClassNumber=" + itemClassNumber + "&item=" + item + "&batchNum=" + stock.getBatchNumber() + "&level=" + stock.getLevel() + "&colorNum=" + stock.getColorNumber() + "&warehouse=" + URLEncoder.encode(stock.getWarehouse(), "utf-8"),"POST");

		JSONObject jsonObject = new JSONObject();
		List<Stock> stockList = JSONArray.toList(jsonarr, stock, new JsonConfig());
        Page<Stock> page = findPage(new Page<Stock>(request, response), stock, stockList);
		for (int i = 0; i < jsonarrTotal.size(); i++) {
			page.setCount((Integer) jsonarrTotal.get(i));
		}
//		Page<Stock> page = stockService.findPage(new Page<Stock>(request, response), stock);
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
     * @param stock
     * @return
     */
    public Page<Stock> findPage(Page<Stock> page, Stock stock, List<Stock> list) {
        dataRuleFilter(stock);
        stock.setPage(page);
        page.setList(list);
        return page;
    }

	/**
	 * 查看，增加，编辑库存查询表单页面
	 */
	@RequiresPermissions(value={"management:warehouse:stock:view","management:warehouse:stock:add","management:warehouse:stock:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Stock stock, Model model) {
		model.addAttribute("stock", stock);
		return "modules/management/warehouse/stockForm";
	}

	/**
	 * 保存库存查询
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:warehouse:stock:add","management:warehouse:stock:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Stock stock, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(stock);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		stockService.save(stock);//保存
		j.setSuccess(true);
		j.setMsg("保存库存查询成功");
		return j;
	}
	
	/**
	 * 删除库存查询
	 */
	@ResponseBody
	@RequiresPermissions("management:warehouse:stock:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Stock stock) {
		AjaxJson j = new AjaxJson();
		stockService.delete(stock);
		j.setMsg("删除库存查询成功");
		return j;
	}
	
	/**
	 * 批量删除库存查询
	 */
	@ResponseBody
	@RequiresPermissions("management:warehouse:stock:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			stockService.delete(stockService.get(id));
		}
		j.setMsg("删除库存查询成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:warehouse:stock:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Stock stock, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		ApiUrl apiUrlList = apiUrlService.getByUsefulness("5");
		if (apiUrlList == null || StringUtils.isBlank(apiUrlList.getUrl())) {
			j.setSuccess(false);
			j.setMsg("请检查库存列表查询接口配置是否准确!");
			return j;
		}
		ApiUrl apiUrlTotal = apiUrlService.getByUsefulness("6");
		if (apiUrlTotal == null || StringUtils.isBlank(apiUrlTotal.getUrl())) {
			j.setSuccess(false);
			j.setMsg("请检查库存列表总量查询接口配置是否准确!");
			return j;
		}
		try {
            String fileName = "库存查询"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
			String itemClassNumber = request.getParameter("itemClassNumber");
			String item = request.getParameter("item");
			JSONArray jsonarrTotal =
					Common.executeInter(apiUrlTotal.getUrl() + "&itemClassNumber=" + itemClassNumber + "&item=" + item + "&batchNum=" + stock.getBatchNumber() + "&level=" + stock.getLevel() + "&colorNum=" + stock.getColorNumber() + "&warehouse=" + URLEncoder.encode(stock.getWarehouse() == null ? "" : stock.getWarehouse(), "utf-8"),"POST");
			JSONArray jsonarr =
					Common.executeInter(apiUrlList.getUrl() + "&itemClassNumber=" + itemClassNumber + "&item=" + item + "&batchNum=" + stock.getBatchNumber() + "&level=" + stock.getLevel() + "&colorNum=" + stock.getColorNumber() + "&warehouse=" + URLEncoder.encode(stock.getWarehouse() == null ? "" : stock.getWarehouse(), "utf-8") + "&showCount=" + jsonarrTotal.get(0),"POST");
			List<Stock> stockList = JSONArray.toList(jsonarr, stock, new JsonConfig());
			Page<Stock> page = findPage(new Page<Stock>(request, response), stock, stockList);
    		new ExportExcel("库存查询", Stock.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出库存查询记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:warehouse:stock:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Stock> list = ei.getDataList(Stock.class);
			for (Stock stock : list){
				try{
					stockService.save(stock);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条库存查询记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条库存查询记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入库存查询失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入库存查询数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:warehouse:stock:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "库存查询数据导入模板.xlsx";
    		List<Stock> list = Lists.newArrayList(); 
    		new ExportExcel("库存查询数据", Stock.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}