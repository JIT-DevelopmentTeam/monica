package com.jeeplus.modules.wechat.barCode;

import com.jeeplus.core.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "${frontPath}/wechat/barCode")
public class BarCodeWechatController extends BaseController {

    @RequestMapping(value = "list")
    public String list(HttpServletRequest request, Model model) {
        return "modules/wechat/barCode/barCode";
    }

}
