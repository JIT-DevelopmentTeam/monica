/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.newsto.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.newsto.entity.NewsTo;
import com.jeeplus.modules.management.newsto.mapper.NewsToMapper;

/**
 * 公告推送人Service
 * @author KicoChan
 * @version 2019-08-24
 */
@Service
@Transactional(readOnly = true)
public class NewsToService extends CrudService<NewsToMapper, NewsTo> {

	public NewsTo get(String id) {
		return super.get(id);
	}
	
	public List<NewsTo> findList(NewsTo newsTo) {
		return super.findList(newsTo);
	}
	
	public Page<NewsTo> findPage(Page<NewsTo> page, NewsTo newsTo) {
		return super.findPage(page, newsTo);
	}
	
	@Transactional(readOnly = false)
	public void save(NewsTo newsTo) {
		super.save(newsTo);
	}
	
	@Transactional(readOnly = false)
	public void delete(NewsTo newsTo) {
		super.delete(newsTo);
	}
	
}