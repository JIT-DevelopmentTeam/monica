package com.jeeplus.modules.wechat.barCode;

import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.apiurl.entity.ApiUrl;
import com.jeeplus.modules.management.apiurl.service.ApiUrlService;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;
import com.jeeplus.modules.management.icitemclass.service.IcitemService;
import com.jeeplus.modules.monitor.utils.Common;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.JsapiTicket;
import com.jeeplus.modules.wxapi.utils.Sign;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IcitemService icitemService;

    @Autowired
    private ApiUrlService apiUrlService;

    /**
     * 条码追溯列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "list")
    public String list(HttpServletRequest request, Model model) {
        return "modules/wechat/barCode/barCode";
    }

    /**
     * 微信二维码扫描配置
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
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

    /**
     * 扫描二维码后通过返回的数据查询数据库匹配的信息返回
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "scanQRCode")
    public Map<String, Object> scanQRCode(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String idCode = request.getParameter("idCode");
        ApiUrl apiUrl = apiUrlService.getByUsefulness("12");
        JSONArray jsonArray = Common.executeInter(apiUrl.getUrl() + "&idCode=" + idCode, apiUrl.getProtocol());
        result.put("result", jsonArray);
        return result;
    }

    /**
     * 根据输入的code查询匹配的信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "searchCode")
    public Map<String, Object> searchCode(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String itemId = request.getParameter("QRCode");
        Icitem icitem = icitemService.findByNumber("0", itemId);
        result.put("result", icitem);
        return result;
    }

}
