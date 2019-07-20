package com.jeeplus.modules.wechat.barCode;

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

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "${frontPath}/wechat/barCode")
public class BarCodeWechatController extends BaseController {

    @RequestMapping(value = "list")
    public String list(HttpServletRequest request, Model model) {
        return "modules/wechat/barCode/barCode";
    }

    @ResponseBody
    @RequestMapping(value = "config")
    public Map<String, Object> config(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<>();
        AccessToken token = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.monicaSecret);
        JsapiTicket ticket = JwAccessTokenAPI.getJsapiTicket(token.getAccesstoken());
        String url = URLDecoder.decode(request.getParameter("url"), "gbk");
        System.out.println(url);
        Map<String, String> config = Sign.sign(ticket.getTicket(), url);
        result.put("config", config);
        return result;
    }

}
