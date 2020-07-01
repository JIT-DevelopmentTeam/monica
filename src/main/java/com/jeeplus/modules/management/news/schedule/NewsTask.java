package com.jeeplus.modules.management.news.schedule;

import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.management.news.web.Push;
import com.jeeplus.modules.management.news.web.WxServiceSend;
import com.jeeplus.modules.management.wxuser.entity.WxUser;
import com.jeeplus.modules.sys.entity.User;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;


@Component
@DisallowConcurrentExecution
public class NewsTask  implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        News news = (News) context.getMergedJobDataMap().get("newsJob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        if(news.getIsPush() != null && "1".equals(news.getIsPush().toString())) {
            if ("0".equals(news.getSendType())) {
                List<WxUser> wxUserList = (List<WxUser>) context.getMergedJobDataMap().get("wxUserList");
                if (wxUserList != null && wxUserList.size() > 0) {
                    for (WxUser wxUser : wxUserList) {
                        WxServiceSend.send(news, wxUser.getOpenId());
                    }
                }
            } else {
                // 装载推送对象信息List
                List<User> userList = (List<User>) context.getMergedJobDataMap().get("userList");
                // 循环获取推送对象数据
                if (userList != null && userList.size() > 0) {
                    String getObjId = "";
                    for (int i = 0; i < userList.size(); i++) {
                        if (i == userList.size() - 1) {
                            getObjId += userList.get(i).getQyUserId();
                        } else
                            getObjId += userList.get(i).getQyUserId() + "|";
                    }
                    Push.captainSendLoggingData(news,getObjId);
                }
            }

        }
        System.out.println("新闻 = [" + news.getTitle() + "]"+ " 在 " + dateFormat.format(news.getPush())+" 时运行");
    }
}
