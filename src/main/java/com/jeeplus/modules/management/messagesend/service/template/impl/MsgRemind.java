package com.jeeplus.modules.management.messagesend.service.template.impl;

import com.jeeplus.modules.management.messagesend.service.template.MessageTemplate;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @title 消息提醒
 * @author
 *
 */

@Service
public class MsgRemind extends MessageTemplate {

    @Autowired
    private UserMapper userMapper;

    public String fromUser(String fromUser){
        User userInfo=new User(fromUser);
        User user = userMapper.get(userInfo);
        String fromUserName=user.getName();
        return fromUserName;
    }

    public String description(String fromUserName,String date,String titleCard){
        String description=
                "<div class=\"gray\">"+date+"</div>" +
                        "<div class=\"normal\">有一条"+titleCard+"等待你审核申请</div>" +
                        "<div class=\"highlight\">发送人:"+fromUserName+"</div>";
        return description;
    }
}
