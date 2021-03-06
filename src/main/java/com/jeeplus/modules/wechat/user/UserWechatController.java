package com.jeeplus.modules.wechat.user;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 系统用户
 * @author KicoChan
 */
@Controller
@RequestMapping(value = "${frontPath}/wechat/user")
public class UserWechatController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/getUserList")
    @ResponseBody
    public AjaxJson getUserList(User user, HttpServletRequest request) {
        AjaxJson aj = new AjaxJson();
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("qyUserId");
        if (userId == null) {
            aj.setSuccess(false);
            aj.setErrorCode("403");
            aj.setMsg("您无权访问！");
            return aj;
        }
        // 过滤超级管理员
        User filter = new User();
        filter.setId("0");
        user.setCurrentUser(filter);
        List<User> userList = userMapper.findList(user);
        if(userList.isEmpty()) {
            aj.setSuccess(false);
        }
        aj.put("userList",userList);
        return aj;
    }

    @RequestMapping("/getUserById")
    @ResponseBody
    public AjaxJson getUserById(@RequestParam("id") String id, HttpServletRequest request) {
        AjaxJson aj = new AjaxJson();
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("qyUserId");
        if (userId == null) {
            aj.setSuccess(false);
            aj.setErrorCode("403");
            aj.setMsg("您无权访问！");
            return aj;
        }
        aj.put("user",userMapper.get(id));
        return aj;
    }

}
