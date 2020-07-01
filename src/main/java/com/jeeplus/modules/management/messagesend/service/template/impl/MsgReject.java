package com.jeeplus.modules.management.messagesend.service.template.impl;

import com.jeeplus.modules.management.messagesend.service.template.MessageTemplate;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @title 消息驳回
 * @author
 */
@Service
public class MsgReject extends MessageTemplate {

    @Autowired
    private UserMapper userMapper;

    public String fromUser(String fromUser){
        User user = userMapper.get(fromUser);
        String fromUserName=user.getName();
        return fromUserName;
    }

    public String description(String fromUserName,String date,String titleCard){
        String description=
                "<div class=\"gray\">"+date+"</div>" +
                        "<div class=\"normal\">"+fromUserName+"驳回了你的"+titleCard+"申请</div>" +
                        "<div class=\"highlight\">驳回人:"+fromUserName+"</div>";
        return description;
    }
}
