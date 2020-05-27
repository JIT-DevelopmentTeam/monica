/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.jurisdiction.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.jurisdiction.entity.Jurisdiction;
import com.jeeplus.modules.management.jurisdiction.mapper.JurisdictionMapper;

/**
 * 库存数据权限Service
 * @author Vigny
 * @version 2020-05-27
 */
@Service
@Transactional(readOnly = true)
public class JurisdictionService extends CrudService<JurisdictionMapper, Jurisdiction> {

	public Jurisdiction get(String id) {
		return super.get(id);
	}
	
	public List<Jurisdiction> findList(Jurisdiction jurisdiction) {
		return super.findList(jurisdiction);
	}
	
	public Page<Jurisdiction> findPage(Page<Jurisdiction> page, Jurisdiction jurisdiction) {
		return super.findPage(page, jurisdiction);
	}
	
	@Transactional(readOnly = false)
	public void save(Jurisdiction jurisdiction) {
		super.save(jurisdiction);
	}
	
	@Transactional(readOnly = false)
	public void delete(Jurisdiction jurisdiction) {
		super.delete(jurisdiction);
	}
	
}