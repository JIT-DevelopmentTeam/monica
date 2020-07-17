package com.jeeplus.modules.wechat.news;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.management.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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
    public AjaxJson data(News news, HttpServletRequest request) {
        AjaxJson aj = new AjaxJson();
        HttpSession session = request.getSession();
        Object qyUserId = session.getAttribute("qyUserId");
        if (qyUserId == null) {
            aj.setSuccess(false);
            aj.setErrorCode("403");
            aj.setMsg("您无权访问！");
            return aj;
        }
        List<News> newsList = newsService.findListWeChat(news);
        aj.setSuccess(true);
        aj.setErrorCode("200");
        aj.put("newsList", newsList);
        return aj;
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
    public AjaxJson formData(HttpServletRequest request) {
        AjaxJson aj = new AjaxJson();
        HttpSession session = request.getSession();
        Object qyUserId = session.getAttribute("qyUserId");
        if (qyUserId == null) {
            aj.setSuccess(false);
            aj.setErrorCode("403");
            aj.setMsg("您无权访问！");
            return aj;
        }
        String id = request.getParameter("id");
        News news = newsService.get(id);
        aj.put("news", news);
        return aj;
    }

    @RequestMapping(value = "moreList")
    public ModelAndView moreList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String type = request.getParameter("type");
        if ("headlines".equals(type)) {
            mv.addObject("head", "头条");
        } else {
            mv.addObject("head", "新闻");
        }
        String path = request.getContextPath();
        String filePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
        mv.addObject("path", filePath);
        mv.addObject("type", type);
        mv.setViewName("modules/wechat/news/newsMoreList");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "moreListData")
    public AjaxJson moreListData(HttpServletRequest request) {
        AjaxJson aj = new AjaxJson();
        HttpSession session = request.getSession();
        Object qyUserId = session.getAttribute("qyUserId");
        if (qyUserId == null) {
            aj.setSuccess(false);
            aj.setErrorCode("403");
            aj.setMsg("您无权访问！");
            return aj;
        }
        String type = request.getParameter("type");
        News news = new News();
        if ("headlines".equals(type)) {
            news.setHeadline(1);
        } else {
            news.setHeadline(0);
        }
        List<News> newsList = newsService.findListWeChat(news);
        aj.put("newsList", newsList);
        return aj;
    }

}
