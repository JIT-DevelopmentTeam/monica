package com.jeeplus.modules.wechat.news;

import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "${adminPath}/wechat/news")
public class NewsWechatController extends BaseController {

    @RequestMapping(value = "list")
    public ModelAndView list(Sobill sobill) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("modules/wechat/news/news");
        return mv;
    }

}
