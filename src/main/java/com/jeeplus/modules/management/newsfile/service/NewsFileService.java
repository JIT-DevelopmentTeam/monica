/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.newsfile.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.newsfile.entity.NewsFile;
import com.jeeplus.modules.management.newsfile.mapper.NewsFileMapper;

/**
 * 新闻公告附件Service
 * @author KicoChan
 * @version 2019-08-24
 */
@Service
@Transactional(readOnly = true)
public class NewsFileService extends CrudService<NewsFileMapper, NewsFile> {

	public NewsFile get(String id) {
		return super.get(id);
	}
	
	public List<NewsFile> findList(NewsFile newsFile) {
		return super.findList(newsFile);
	}
	
	public Page<NewsFile> findPage(Page<NewsFile> page, NewsFile newsFile) {
		return super.findPage(page, newsFile);
	}
	
	@Transactional(readOnly = false)
	public void save(NewsFile newsFile) {
		super.save(newsFile);
	}
	
	@Transactional(readOnly = false)
	public void delete(NewsFile newsFile) {
		super.delete(newsFile);
	}
	
}