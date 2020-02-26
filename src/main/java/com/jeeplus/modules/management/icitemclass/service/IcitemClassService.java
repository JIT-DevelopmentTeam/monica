/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.service;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.service.TreeService;
import com.jeeplus.modules.management.icitemclass.entity.IcitemClass;
import com.jeeplus.modules.management.icitemclass.mapper.IcitemClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品分类管理Service
 * @author JiaChe
 * @version 2019-05-29
 */
@Service
@Transactional(readOnly = true)
public class IcitemClassService extends TreeService<IcitemClassMapper, IcitemClass> {

	@Autowired
	private IcitemClassMapper icitemClassMapper;

	public IcitemClass get(String id) {
		return super.get(id);
	}
	
	public List<IcitemClass> findList(IcitemClass icitemClass) {
		if (StringUtils.isNotBlank(icitemClass.getParentIds())){
			icitemClass.setParentIds(","+icitemClass.getParentIds()+",");
		}
		return super.findList(icitemClass);
	}

	/**
	 * 查询列表中最大的modifytime
	 * @return
	 */
	public String findMaxModifyTime() {
		return icitemClassMapper.findMaxModifyTime();
	}
	
	@Transactional(readOnly = false)
	public void save(IcitemClass icitemClass) {
		super.save(icitemClass);
	}

	/**
	 * 批量插入数据
	 * @param list
	 */
	@Transactional(readOnly = false)
	public void batchInsert(List<IcitemClass> list) {
		icitemClassMapper.batchInsert(list);
	}
	
	@Transactional(readOnly = false)
	public void delete(IcitemClass icitemClass) {
		super.delete(icitemClass);
	}

	public List<IcitemClass> findListForWechat(IcitemClass icitemClass){
	    return mapper.findListForWechat(icitemClass);
    }

    @Transactional(readOnly = false)
    public void deleteAllData() { mapper.deleteAllData();}
	
}