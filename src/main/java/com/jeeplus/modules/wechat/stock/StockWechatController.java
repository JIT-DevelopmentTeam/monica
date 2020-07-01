package com.jeeplus.modules.wechat.stock;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.apiurl.entity.ApiUrl;
import com.jeeplus.modules.management.apiurl.service.ApiUrlService;
import com.jeeplus.modules.management.jurisdiction.service.JurisdictionService;
import com.jeeplus.modules.management.warehouse.entity.Stock;
import com.jeeplus.modules.management.warehouse.entity.Warehouse;
import com.jeeplus.modules.management.wxuser.entity.WxUser;
import com.jeeplus.modules.management.wxuser.service.WxUserService;
import com.jeeplus.modules.monitor.utils.Common;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping(value = "${frontPath}/wechat/stock")
public class StockWechatController extends BaseController {

    @Autowired
    private ApiUrlService apiUrlService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private JurisdictionService jurisdictionService;

    @RequestMapping(value = "list")
    public String list(HttpServletRequest request, Model model) {
        return "modules/wechat/stock/stock";
    }

    @RequestMapping(value = "detail")
    public String stockDetail(HttpServletRequest request, Model model) {
        String commodityNumber = request.getParameter("commodityNumber");
        String batchNumber = request.getParameter("batchNumber");
        String warehouse = request.getParameter("warehouse");
        model.addAttribute("commodityNumber", commodityNumber);
        model.addAttribute("batchNumber", batchNumber);
        model.addAttribute("warehouse", warehouse);
        return "modules/wechat/stock/stockDetail";
    }

    @ResponseBody
    @RequestMapping(value = "listData")
    public AjaxJson listData(HttpServletRequest request, Stock stock) {
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
        String pageNo = request.getParameter("pageNo");
        String item = request.getParameter("item");
        item = item.replace("%", "%25");
        String[] levelArr = request.getParameterValues("levelArr[]");
        String color = request.getParameter("color");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String level = "";
        if (levelArr != null) {
            level = levelArr[0];
        }
        JSONArray jsonarr =
                Common.executeInter(apiUrlList.getUrl() + "&item=" + item + "&level=" + level + "&colorNum=" + color + "&currentPage=" + pageNo,"POST");
        JSONArray jsonarrTotal =
                Common.executeInter(apiUrlTotal.getUrl() + "&item=" + item + "&level=" + level + "&colorNum=" + color,"POST");
        List<Stock> stockList = JSONArray.toList(jsonarr, stock, new JsonConfig());
        j.put("stockList", stockList);
        j.put("total", jsonarrTotal.get(0));
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "detailData")
    public AjaxJson detailData(HttpServletRequest request, Stock stock) throws UnsupportedEncodingException {
        AjaxJson j = new AjaxJson();
        ApiUrl apiUrl = apiUrlService.getByUsefulness("7");
        if (apiUrl == null || StringUtils.isBlank(apiUrl.getUrl())) {
            j.setSuccess(false);
            j.setMsg("同步出错,请检查接口配置是否准确!");
            return j;
        }
        String commodityNumber = request.getParameter("commodityNumber");
        String batchNumber = request.getParameter("batchNumber");
        String warehouse = request.getParameter("warehouse");
        JSONArray jsonarr =
                Common.executeInter(apiUrl.getUrl() + "&commodityNumber=" + commodityNumber + "&batchNum=" + batchNumber + "&warehouse=" + URLEncoder.encode(warehouse, "utf-8"), apiUrl.getProtocol());
        j.put("data", jsonarr);
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getLeval")
    public AjaxJson getLeval() {
        AjaxJson j = new AjaxJson();
        ApiUrl apiUrl = apiUrlService.getByUsefulness("8");
        if (apiUrl == null || StringUtils.isBlank(apiUrl.getUrl())) {
            j.setSuccess(false);
            j.setMsg("同步出错,请检查接口配置是否准确!");
            return j;
        }
        JSONArray jsonarr =
                Common.executeInter(apiUrl.getUrl(),"POST");
        j.put("data", jsonarr);
        return j;
    }



    /* =================================== 微信服务号 =================================== */
    @ResponseBody
    @RequestMapping(value = "/war_jur", method = RequestMethod.GET)
    public AjaxJson war_jur(String openId) {
        AjaxJson j = new AjaxJson();
        WxUser wxuser = wxUserService.getByOpenId(openId);
        List<Warehouse> jurWarehouseList = jurisdictionService.findJurByClientId(wxuser.getClient().getId());
        j.setSuccess(true);
        j.put("jurWarehouseList", jurWarehouseList);
        return j;
    }

}
