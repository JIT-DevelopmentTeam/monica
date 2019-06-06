/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.plaurl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.plaurl.entity.PlaUrl;
import com.jeeplus.modules.management.plaurl.mapper.PlaUrlMapper;

/**
 * 平台链接管理Service
 * @author 家成
 * @version 2019-06-06
 */
@Service
@Transactional(readOnly = true)
public class PlaUrlService extends CrudService<PlaUrlMapper, PlaUrl> {

	public PlaUrl get(String id) {
		return super.get(id);
	}
	
	public List<PlaUrl> findList(PlaUrl plaUrl) {
		return super.findList(plaUrl);
	}
	
	public Page<PlaUrl> findPage(Page<PlaUrl> page, PlaUrl plaUrl) {
		return super.findPage(page, plaUrl);
	}
	
	@Transactional(readOnly = false)
	public void save(PlaUrl plaUrl) {
		super.save(plaUrl);
	}
	
	@Transactional(readOnly = false)
	public void delete(PlaUrl plaUrl) {
		super.delete(plaUrl);
	}
	
}