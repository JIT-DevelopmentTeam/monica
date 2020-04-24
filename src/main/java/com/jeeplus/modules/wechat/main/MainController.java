package com.jeeplus.modules.wechat.main;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.ObjectUtils;
import com.jeeplus.common.utils.base.ObjectUtil;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.wxapi.api.core.exception.WexinReqException;
import com.jeeplus.modules.wxapi.jeecg.alipay.api.base.JWPromotionalSupportAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.user.JwUserAPI;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "${frontPath}/wechat/main")
public class MainController extends BaseController {

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

}
