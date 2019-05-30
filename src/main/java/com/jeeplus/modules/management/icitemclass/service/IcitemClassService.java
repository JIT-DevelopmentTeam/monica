/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.management.icitemclass.entity.IcitemClass;
import com.jeeplus.modules.management.icitemclass.mapper.IcitemClassMapper;

/**
 * 商品分类管理Service
 * @author JiaChe
 * @version 2019-05-29
 */
@Service
@Transactional(readOnly = true)
public class IcitemClassService extends TreeService<IcitemClassMapper, IcitemClass> {

	public IcitemClass get(String id) {
		return super.get(id);
	}
	
	public List<IcitemClass> findList(IcitemClass icitemClass) {
		if (StringUtils.isNotBlank(icitemClass.getParentIds())){
			icitemClass.setParentIds(","+icitemClass.getParentIds()+",");
		}
		return super.findList(icitemClass);
	}
	
	@Transactional(readOnly = false)
	public void save(IcitemClass icitemClass) {
		super.save(icitemClass);
	}
	
	@Transactional(readOnly = false)
	public void delete(IcitemClass icitemClass) {
		super.delete(icitemClass);
	}
	
}