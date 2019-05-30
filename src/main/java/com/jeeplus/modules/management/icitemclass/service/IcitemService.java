/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;
import com.jeeplus.modules.management.icitemclass.mapper.IcitemMapper;

/**
 * 商品资料Service
 * @author JiaChe
 * @version 2019-05-29
 */
@Service
@Transactional(readOnly = true)
public class IcitemService extends CrudService<IcitemMapper, Icitem> {

	public Icitem get(String id) {
		return super.get(id);
	}
	
	public List<Icitem> findList(Icitem icitem) {
		return super.findList(icitem);
	}
	
	public Page<Icitem> findPage(Page<Icitem> page, Icitem icitem) {
		return super.findPage(page, icitem);
	}
	
	@Transactional(readOnly = false)
	public void save(Icitem icitem) {
		super.save(icitem);
	}
	
	@Transactional(readOnly = false)
	public void delete(Icitem icitem) {
		super.delete(icitem);
	}
	
}