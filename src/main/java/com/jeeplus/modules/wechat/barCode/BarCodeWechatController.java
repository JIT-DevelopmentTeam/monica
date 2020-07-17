package com.jeeplus.modules.wechat.barCode;

import com.jeeplus.common.json.AjaxJson;
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
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    public AjaxJson config(HttpServletRequest request) throws UnsupportedEncodingException {
        AjaxJson aj = new AjaxJson();
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("qyUserId");
        if (userId == null) {
            aj.setSuccess(false);
            aj.setErrorCode("403");
            aj.setMsg("您无权访问！");
            return aj;
        }
        AccessToken token = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.monicaSecret);
        JsapiTicket ticket = JwAccessTokenAPI.getJsapiTicket(token.getAccesstoken());
        String url = URLDecoder.decode(request.getParameter("url"), "gbk");
        System.out.println(url);
        Map<String, String> config = Sign.sign(ticket.getTicket(), url);
        aj.put("config", config);
        return aj;
    }

    /**
     * 扫描二维码后通过返回的数据查询数据库匹配的信息返回
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "scanQRCode")
    public AjaxJson scanQRCode(HttpServletRequest request) {
        AjaxJson aj = new AjaxJson();
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("qyUserId");
        if (userId == null) {
            aj.setSuccess(false);
            aj.setErrorCode("403");
            aj.setMsg("您无权访问！");
            return aj;
        }
        String idCode = request.getParameter("idCode");
        ApiUrl apiUrl = apiUrlService.getByUsefulness("12");
        JSONArray jsonArray = Common.executeInter(apiUrl.getUrl() + "&idCode=" + idCode, apiUrl.getProtocol());
        aj.put("result", jsonArray);
        return aj;
    }

    /**
     * 根据输入的code查询匹配的信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "searchCode")
    public AjaxJson searchCode(HttpServletRequest request) {
        AjaxJson aj = new AjaxJson();
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("qyUserId");
        if (userId == null) {
            aj.setSuccess(false);
            aj.setErrorCode("403");
            aj.setMsg("您无权访问！");
            return aj;
        }
        String itemId = request.getParameter("QRCode");
        Icitem icitem = icitemService.findByNumber("0", itemId);
        aj.put("result", icitem);
        return aj;
    }

}
