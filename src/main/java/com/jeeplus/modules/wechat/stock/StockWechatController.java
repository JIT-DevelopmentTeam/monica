package com.jeeplus.modules.wechat.stock;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.apiurl.entity.ApiUrl;
import com.jeeplus.modules.management.apiurl.service.ApiUrlService;
import com.jeeplus.modules.management.warehouse.entity.Stock;
import com.jeeplus.modules.monitor.utils.Common;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "${frontPath}/wechat/stock")
public class StockWechatController extends BaseController {

    @Autowired
    private ApiUrlService apiUrlService;

    @RequestMapping(value = "list")
    public String list(HttpServletRequest request, Model model) {
        return "modules/wechat/stock/stock";
    }

    @RequestMapping(value = "detail")
    public String stockDetail(HttpServletRequest request, Model model) {
        String commodityNumber = request.getParameter("commodityNumber");
        model.addAttribute("commodityNumber", commodityNumber);
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
        JSONArray jsonarr =
                Common.executeInter(apiUrlList.getUrl() + "&currentPage=" + pageNo,"POST");
        JSONArray jsonarrTotal =
                Common.executeInter(apiUrlTotal.getUrl(),"POST");
        List<Stock> stockList = JSONArray.toList(jsonarr, stock, new JsonConfig());
        j.put("stockList", stockList);
        j.put("total", jsonarrTotal.get(0));
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "detailData")
    public AjaxJson detailData(HttpServletRequest request, Stock stock) {
        AjaxJson j = new AjaxJson();
        ApiUrl apiUrl = apiUrlService.getByUsefulness("7");
        if (apiUrl == null || StringUtils.isBlank(apiUrl.getUrl())) {
            j.setSuccess(false);
            j.setMsg("同步出错,请检查接口配置是否准确!");
            return j;
        }
        String commodityNumber = request.getParameter("commodityNumber");
        JSONArray jsonarr =
                Common.executeInter(apiUrl.getUrl() + "&commodityNumber=" + commodityNumber,"POST");
        j.put("data", jsonarr);
        return j;
    }

}
