/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.usedrecord.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.usedrecord.entity.UsedRecord;
import com.jeeplus.modules.management.usedrecord.mapper.UsedRecordMapper;

/**
 * token范围Service
 * @author KicoChan
 * @version 2019-08-24
 */
@Service
@Transactional(readOnly = true)
public class UsedRecordService extends CrudService<UsedRecordMapper, UsedRecord> {

	public UsedRecord get(String id) {
		return super.get(id);
	}
	
	public List<UsedRecord> findList(UsedRecord usedRecord) {
		return super.findList(usedRecord);
	}
	
	public Page<UsedRecord> findPage(Page<UsedRecord> page, UsedRecord usedRecord) {
		return super.findPage(page, usedRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(UsedRecord usedRecord) {
		super.save(usedRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(UsedRecord usedRecord) {
		super.delete(usedRecord);
	}
	
}