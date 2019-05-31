/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.sobillandentry.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.management.sobillandentry.mapper.SobillMapper;
import com.jeeplus.modules.management.sobillandentry.entity.Sobillentry;
import com.jeeplus.modules.management.sobillandentry.mapper.SobillentryMapper;

/**
 * 订单模块Service
 * @author KicoChan
 * @version 2019-05-30
 */
@Service
@Transactional(readOnly = true)
public class SobillService extends CrudService<SobillMapper, Sobill> {

	@Autowired
	private SobillentryMapper sobillentryMapper;

	@Autowired
	private SobillMapper sobillMapper;
	
	public Sobill get(String id) {
		Sobill sobill = super.get(id);
		sobill.setSobillentryList(sobillentryMapper.findList(new Sobillentry(sobill)));
		return sobill;
	}
	
	public List<Sobill> findList(Sobill sobill) {
		return super.findList(sobill);
	}
	
	public Page<Sobill> findPage(Page<Sobill> page, Sobill sobill) {
		return super.findPage(page, sobill);
	}
	
	@Transactional(readOnly = false)
	public void save(Sobill sobill) {
		super.save(sobill);
		for (Sobillentry sobillentry : sobill.getSobillentryList()){
			if (sobillentry.getId() == null){
				continue;
			}
			if (Sobillentry.DEL_FLAG_NORMAL.equals(sobillentry.getDelFlag())){
				if (StringUtils.isBlank(sobillentry.getId())){
					sobillentry.setSobillId(sobill);
					sobillentry.preInsert();
					sobillentryMapper.insert(sobillentry);
				}else{
					sobillentry.preUpdate();
					sobillentryMapper.update(sobillentry);
				}
			}else{
				sobillentryMapper.delete(sobillentry);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Sobill sobill) {
		super.delete(sobill);
		sobillentryMapper.delete(new Sobillentry(sobill));
	}

	public void checkOrder(Sobill sobill) {
		sobillMapper.checkOrder(sobill);
	}
	
}