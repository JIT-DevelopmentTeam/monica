/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.news.service;

import com.google.common.collect.Lists;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.management.news.mapper.NewsMapper;
import com.jeeplus.modules.management.news.schedule.NewsTask;
import com.jeeplus.modules.management.newspush.entity.NewsPush;
import com.jeeplus.modules.management.newspush.service.NewsPushService;
import com.jeeplus.modules.management.wxuser.entity.WxUser;
import com.jeeplus.modules.management.wxuser.service.WxUserService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 新闻公告Service
 * @author Vigny
 * @version 2019-06-10
 */
@Service
@Transactional(readOnly = true)
public class NewsService extends CrudService<NewsMapper, News> {

	@Autowired
	private NewsMapper newsMapper;

	// 注入一个定时器对象
	@Autowired
	private Scheduler scheduler;

	@Autowired
	private NewsPushService newsPushService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private WxUserService wxUserService;

	public News get(String id) {
		return super.get(id);
	}
	
	public List<News> findList(News news) {
		return super.findList(news);
	}
	
	public Page<News> findPage(Page<News> page, News news) {
		return super.findPage(page, news);
	}

	public List<News> findListWeChat(News news) {
		return newsMapper.findListWeChat(news);
	}
	
	@Transactional(readOnly = false)
	public void save(News news) {
		super.save(news);

	}
	
	@Transactional(readOnly = false)
	public void delete(News news) {
		super.delete(news);
	}

	@Transactional(readOnly = false)
	public void updateReadCount(String id) {
		newsMapper.updateReadCount(id);
	}

	//
	@Transactional(readOnly = false)
	public void task(News news){
		JobDetail jobDetail = JobBuilder.newJob(NewsTask.class).withIdentity(news.getTitle(), news.getPushrule()).build();
		jobDetail.getJobDataMap().put("newsJob", news);
		if ("0".equals(news.getSendType())) {// 服务号
			List<WxUser> wxUserList = wxList(news);
			jobDetail.getJobDataMap().put("wxUserList", wxUserList);
		} else {// 企业微信
			List<User> userList = list(news);
			jobDetail.getJobDataMap().put("userList", userList);
		}

		SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity(news.getTitle(), news.getPushrule())
				.startAt(news.getPush()).forJob(news.getTitle(), news.getPushrule()).build();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			JobKey key = new JobKey(news.getTitle(), news.getPushrule());
			if(news.getIsPush().equals("0")){
				scheduler.pauseJob(key);
			}else{
				scheduler.resumeJob(key);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public List<WxUser> wxList(News news) {
		List<WxUser> wxUserList = Lists.newArrayList();
		NewsPush push = new NewsPush();
		push.setNewsId(news.getId());
		List<NewsPush> newsPushList=newsPushService.findAllList(push);
		for (NewsPush newsPush : newsPushList) {
			WxUser wxUser = wxUserService.get(newsPush.getObjId());
			if (wxUser != null) {
				wxUserList.add(wxUser);
			}
		}
		return wxUserList;
	}

	public List<User> list(News news){
		// 装载推送对象信息List
		List<User> userList = Lists.newArrayList();
		User userParam = null;
		// 推送对象Id
		NewsPush push = new NewsPush();
		push.setNewsId(news.getId());
		List<NewsPush> newsPushList=newsPushService.findAllList(push);
		for (int i = 0; i < newsPushList.size(); i++) {
			if ("1".equals(news.getPushrule())) {     // 个人推送消息
				User user = userMapper.get(newsPushList.get(i).getObjId());
				System.out.println(newsPushList.get(i).getObjId());
				if (user != null) {
					userList.add(user);
				}
			} else {
				userParam = new User();
				if ("2".equals(news.getPushrule())) {
					Office office =new Office();
					office.setId(newsPushList.get(i).getObjId());
					userParam.setOffice(office);// 部门推送
				} else {
					Office company = new Office();
					company.setId(newsPushList.get(i).getObjId());
					userParam.setCompany(company);// 企业下的所有部门的员工，全部推送
				}
				List<User> list = userMapper.findListByUserOfficeList(userParam);
				if (list.size() > 0) {
					userList.addAll(list);
				}
			}
		}
		return userList;
	}
}


