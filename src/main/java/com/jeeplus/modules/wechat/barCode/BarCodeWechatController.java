package com.jeeplus.modules.wechat.barCode;

import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.utils.Sign;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "${frontPath}/wechat/barCode")
public class BarCodeWechatController extends BaseController {

    @RequestMapping(value = "list")
    public String list(HttpServletRequest request, Model model) {
        AccessToken token = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.secret);
        String jsapi_ticket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + token + "&type=jsapi";
        String url = request.getRequestURL().toString();
        Map<String, String> result = Sign.sign(jsapi_ticket, url);
        model.addAttribute("config", result);
        return "modules/wechat/barCode/barCode";
    }

}
