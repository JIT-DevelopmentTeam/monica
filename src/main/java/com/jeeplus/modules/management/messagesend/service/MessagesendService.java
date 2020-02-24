/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.messagesend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.messagesend.entity.Messagesend;
import com.jeeplus.modules.management.messagesend.mapper.MessagesendMapper;

/**
 * 消息发送Service
 * @author MrLISH
 * @version 2019-08-14
 */
@Service
@Transactional(readOnly = true)
public class MessagesendService extends CrudService<MessagesendMapper, Messagesend> {

	public Messagesend get(String id) {
		return super.get(id);
	}
	
	public List<Messagesend> findList(Messagesend messagesend) {
		return super.findList(messagesend);
	}
	
	public Page<Messagesend> findPage(Page<Messagesend> page, Messagesend messagesend) {
		return super.findPage(page, messagesend);
	}
	
	@Transactional(readOnly = false)
	public void save(Messagesend messagesend) {
		super.save(messagesend);
	}
	
	@Transactional(readOnly = false)
	public void delete(Messagesend messagesend) {
		super.delete(messagesend);
	}

}