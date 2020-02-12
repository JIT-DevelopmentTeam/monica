package com.jeeplus.modules.wechat.stock;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.warehouse.entity.Stock;
import com.jeeplus.modules.monitor.utils.Common;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "${frontPath}/wechat/stock")
public class StockWechatController extends BaseController {

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
        String pageNo = request.getParameter("pageNo");
        JSONArray jsonarr =
                Common.executeInter("http://120.77.40.245:8080/interface_monica/erp_get/erp_warehouse_stock?token_value=20190603&currentPage=" + pageNo,"POST");
        JSONArray jsonarrTotal =
                Common.executeInter("http://120.77.40.245:8080/interface_monica/erp_get/erp_warehouse_stock_total?token_value=20190603","POST");
        List<Stock> stockList = JSONArray.toList(jsonarr, stock, new JsonConfig());
        j.put("stockList", stockList);
        j.put("total", jsonarrTotal.get(0));
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "detailData")
    public AjaxJson detailData(HttpServletRequest request, Stock stock) {
        AjaxJson j = new AjaxJson();
        String commodityNumber = request.getParameter("commodityNumber");
        JSONArray jsonarr =
                Common.executeInter("http://120.77.40.245:8080/interface_monica/erp_get/erp_warehouse_stock_detail?token_value=20190603&commodityNumber=" + commodityNumber,"POST");
        j.put("data", jsonarr);
        return j;
    }

}
