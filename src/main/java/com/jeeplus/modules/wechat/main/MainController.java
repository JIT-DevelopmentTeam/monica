package com.jeeplus.modules.wechat.main;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.wxuser.entity.WxUser;
import com.jeeplus.modules.management.wxuser.service.WxUserService;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.user.JwUserAPI;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.WechatAPI;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.entity.JsConfig;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.entity.SNSUserInfo;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.entity.WebAuthAccessToken;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.exception.WebAuthAccessTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping(value = "${frontPath}/wechat/main")
public class MainController extends BaseController {

    @Autowired
    private WxUserService wxUserService;

    /**
     * 主页
     * @return
     */
    @RequestMapping(value = {"index", ""})
    public String index() {
        return "modules/wechat/main/index";
    }

    /**
     * 根据Code获取UserId
     * @param code
     * @return
     */
    @RequestMapping(value = "/getUserInfoByCode")
    @ResponseBody
    public AjaxJson getUserInfoByCode(@RequestParam("code") String code) {
        AjaxJson aj = new AjaxJson();
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        Map<String,Object> resultMap = JwUserAPI.getWxuserInfo(code);
        if (resultMap.get("UserId") == null) {
            aj.setSuccess(false);
            return aj;
        }
        body.put("userId",resultMap.get("UserId").toString());
        aj.setSuccess(true);
        aj.setBody(body);
        return aj;
    }













    /*================================================公众号================================================*/

    @ResponseBody
    @RequestMapping(value = "/getWXUserInfo", method = RequestMethod.GET)
    public AjaxJson getWXUserInfo(@RequestParam("code") String code) {
        AjaxJson aj = new AjaxJson();
        String appid = "";
        String appsecret = "";
        try {
            appid = PropertiesLoaderUtils.loadAllProperties(("wechat/config.properties")).getProperty("wechat_appid");
            appsecret = PropertiesLoaderUtils.loadAllProperties(("wechat/config.properties")).getProperty("wechat_appsecret");
        } catch (IOException e) {
            e.printStackTrace();
        }
        WechatAPI wxAPI = new WechatAPI(appid, appsecret);
        try {
            WebAuthAccessToken webAuthAccessToken = wxAPI.getWebAuthAccessToken(code);
            Boolean aBoolean = wxAPI.verifyToken(webAuthAccessToken.getAccessToken(), webAuthAccessToken.getOpenid());
            if (!aBoolean) {
                // token无效
                webAuthAccessToken = wxAPI.refreshToken(webAuthAccessToken.getRefreshToken());
            }
            WxUser byOpenId = wxUserService.getByOpenId(webAuthAccessToken.getOpenid());
            if (byOpenId == null) {
                SNSUserInfo snsUserInfo = wxAPI.getSNSUserInfo(webAuthAccessToken.getAccessToken(), webAuthAccessToken.getOpenid());
                WxUser wxUser = new WxUser();
                wxUser.setOpenId(snsUserInfo.getOpenId());
                wxUser.setNickName(snsUserInfo.getNickname());
                wxUser.setSex(String.valueOf(snsUserInfo.getSex()));
                wxUser.setCity(snsUserInfo.getCity());
                wxUser.setCountry(snsUserInfo.getCountry());
                wxUser.setProvince(snsUserInfo.getProvince());
                wxUser.setAccessToken(webAuthAccessToken.getAccessToken());
                wxUser.setAccessTokenExpireId(new Timestamp(System.currentTimeMillis() + webAuthAccessToken.getExpiresIn() * 1000));
                wxUser.setRefreshToken(webAuthAccessToken.getRefreshToken());
                wxUser.setRefreshTokenExpireId(new Timestamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30));
                wxUserService.save(wxUser);
                aj.setSuccess(true);
                aj.put("user", wxUser);
                return aj;
            }
            byOpenId.setAccessToken(webAuthAccessToken.getAccessToken());
            byOpenId.setAccessTokenExpireId(new Timestamp(System.currentTimeMillis() + webAuthAccessToken.getExpiresIn() * 1000));
            byOpenId.setRefreshToken(webAuthAccessToken.getRefreshToken());
            if (byOpenId.getRefreshTokenExpireId() == null) {
                byOpenId.setRefreshTokenExpireId(new Timestamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30));
            }
            wxUserService.save(byOpenId);
            aj.setSuccess(true);
            aj.put("user", byOpenId);
        } catch (WebAuthAccessTokenException e) {
            e.printStackTrace();
            aj.setMsg(e.getMessage());
            aj.setSuccess(false);
        }
        return aj;
    }

    @ResponseBody
    @RequestMapping(value = "/getJsApiTicket", method = RequestMethod.GET)
    public AjaxJson getJsApiTicket(String url, Boolean debug, String jsApiList) {
        AjaxJson aj = new AjaxJson();
        String appid = "";
        String appsecret = "";
        try {
            appid = PropertiesLoaderUtils.loadAllProperties(("wechat/config.properties")).getProperty("wechat_appid");
            appsecret = PropertiesLoaderUtils.loadAllProperties(("wechat/config.properties")).getProperty("wechat_appsecret");
        } catch (IOException e) {
            e.printStackTrace();
        }
        WechatAPI wxAPI = new WechatAPI(appid, appsecret);
        Map<String, Object> param = new HashMap<>();
        List<String> jsList = new ArrayList<>();
        String[] split = jsApiList.split(",");
        for (String js : split) {
            jsList.add(js);
        }
        param.put("url", url);
        param.put("debug", debug);
        param.put("jsApiList", jsList);
        JsConfig jsConfig = wxAPI.getJsConfig(param);
        aj.setSuccess(true);
        aj.put("jsConfig", jsConfig);
        return aj;
    }


}
