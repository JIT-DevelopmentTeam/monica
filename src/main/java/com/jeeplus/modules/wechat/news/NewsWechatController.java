package com.jeeplus.modules.wechat.news;

import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.management.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${frontPath}/wechat/news")
public class NewsWechatController extends BaseController {

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "list")
    public ModelAndView list(News news) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("modules/wechat/news/news");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(News news) {
        Map<String, Object> map = new HashMap<>();
        List<News> newsList = newsService.findList(news);
        map.put("newsList", newsList);
        return map;
    }

    @RequestMapping(value = "form")
    public String form(News news) {
        return "modules/wechat/news/newsForm";
    }

}
