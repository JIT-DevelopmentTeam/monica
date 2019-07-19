/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.newspush.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.newspush.entity.NewsPush;
import com.jeeplus.modules.management.newspush.mapper.NewsPushMapper;

/**
 * 微信推送新闻对象Service
 * @author commit
 * @version 2019-06-18
 */
@Service
@Transactional(readOnly = true)
public class NewsPushService extends CrudService<NewsPushMapper, NewsPush> {

	public NewsPush get(String id) {
		return super.get(id);
	}
	
	public List<NewsPush> findList(NewsPush newsPush) {
		return super.findList(newsPush);
	}
	
	public Page<NewsPush> findPage(Page<NewsPush> page, NewsPush newsPush) {
		return super.findPage(page, newsPush);
	}
	
	@Transactional(readOnly = false)
	public void save(NewsPush newsPush) {
		super.save(newsPush);
	}
	
	@Transactional(readOnly = false)
	public void delete(NewsPush newsPush) {
		super.delete(newsPush);
	}
	
}