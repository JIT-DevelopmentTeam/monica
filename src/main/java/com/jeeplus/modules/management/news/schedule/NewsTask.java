package com.jeeplus.modules.management.news.schedule;

import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.management.news.web.Push;
import com.jeeplus.modules.monitor.utils.SystemInfo;
import com.jeeplus.modules.sys.entity.User;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Component
@DisallowConcurrentExecution
public class NewsTask  implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        News news = (News) context.getMergedJobDataMap().get("newsJob");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        if(news.getIsPush() != null && "1".equals(news.getIsPush().toString())) {
            // 装载推送对象信息List
            List<User> userList = (List<User>) context.getMergedJobDataMap().get("userList");
            // 循环获取推送对象数据
            if (userList.size() > 0) {
                String getObjId = "";
                for (int i = 0; i < userList.size(); i++) {
                    if (i == userList.size() - 1) {
                        getObjId += userList.get(i).getQyUserId();
                    } else
                        getObjId += userList.get(i).getQyUserId() + "|";
                }
                System.out.println(" 新闻推送 UserID : " + getObjId);
                // 新闻推送
                Map map=SystemInfo.SystemProperty();
                String ip="";
                String port="";
                try {
                    ip=map.get("hostIp").toString();
                    port=SystemInfo.getServerPort(false);
                } catch (AttributeNotFoundException e) {
                    e.printStackTrace();
                } catch (InstanceNotFoundException e) {
                    e.printStackTrace();
                } catch (MBeanException e) {
                    e.printStackTrace();
                } catch (ReflectionException e) {
                    e.printStackTrace();
                }
                Push.captainSendLoggingData(ip,port,news,getObjId);
            }

        }
        System.out.println("新闻 = [" + news.getTitle() + "]"+ " 在 " + dateFormat.format(news.getPush())+" 时运行");
    }
}
