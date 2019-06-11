package com.jeeplus.modules.wechat.main;

import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "${adminPath}/wechat/main")
public class MainController extends BaseController {

    /**
     * 主页
     * @return
     */
    @RequestMapping(value = {"index", ""})
    public String index() {
        return "modules/wechat/main/index";
    }

}
