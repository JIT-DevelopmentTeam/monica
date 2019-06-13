package com.jeeplus.modules.wechat.news;

import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.management.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${frontPath}/wechat/news")
public class NewsWechatController extends BaseController {

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "list")
    public ModelAndView list(News news, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String path = request.getContextPath();
        String filePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
        mv.addObject("path", filePath);
        mv.setViewName("modules/wechat/news/news");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(News news) {
        Map<String, Object> map = new HashMap<>();
        List<News> newsList = newsService.findListWeChat(news);
        map.put("newsList", newsList);
        return map;
    }

    @RequestMapping(value = "form")
    public ModelAndView form(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String id = request.getParameter("id");
        newsService.updateReadCount(id);
        mv.addObject("id", id);
        mv.setViewName("modules/wechat/news/newsForm");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "formData")
    public Map<String, Object> formData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        String id = request.getParameter("id");
        News news = newsService.get(id);
        map.put("news", news);
        return map;
    }

}
