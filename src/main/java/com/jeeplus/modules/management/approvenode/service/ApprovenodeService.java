/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.approvenode.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.approvenode.entity.Approvenode;
import com.jeeplus.modules.management.approvenode.mapper.ApprovenodeMapper;

/**
 * 流程节点Service
 * @author Vigny
 * @version 2019-08-14
 */
@Service
@Transactional(readOnly = true)
public class ApprovenodeService extends CrudService<ApprovenodeMapper, Approvenode> {

	public Approvenode get(String id) {
		return super.get(id);
	}
	
	public List<Approvenode> findList(Approvenode approvenode) {
		return super.findList(approvenode);
	}
	
	public Page<Approvenode> findPage(Page<Approvenode> page, Approvenode approvenode) {
		return super.findPage(page, approvenode);
	}
	
	@Transactional(readOnly = false)
	public void save(Approvenode approvenode) {
		super.save(approvenode);
	}
	
	@Transactional(readOnly = false)
	public void delete(Approvenode approvenode) {
		super.delete(approvenode);
	}
	
}