package com.jeeplus.modules.wechat.stock;

import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.JsapiTicket;
import com.jeeplus.modules.wxapi.utils.Sign;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "${frontPath}/wechat/stock")
public class StockWechatController extends BaseController {

    @RequestMapping(value = "list")
    public String list(HttpServletRequest request, Model model) {
        return "modules/wechat/stock/stock";
    }

    @RequestMapping(value = "detail")
    public String stockDetail(HttpServletRequest request, Model model) {
        return "modules/wechat/stock/stockDetail";
    }

}
