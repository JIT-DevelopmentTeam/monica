/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.news.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.management.news.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

	public News get(String id) {
		return super.get(id);
	}
	
	public List<News> findList(News news) {
		return super.findList(news);
	}
	
	public Page<News> findPage(Page<News> page, News news) {
		return super.findPage(page, news);
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
	
}