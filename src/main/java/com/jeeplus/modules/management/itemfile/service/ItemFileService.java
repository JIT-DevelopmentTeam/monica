/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.itemfile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.itemfile.entity.ItemFile;
import com.jeeplus.modules.management.itemfile.mapper.ItemFileMapper;

/**
 * 商品图片附件管理Service
 * @author MrLISH
 * @version 2019-08-14
 */
@Service
@Transactional(readOnly = true)
public class ItemFileService extends CrudService<ItemFileMapper, ItemFile> {

	@Autowired
	private ItemFileMapper itemFileMapper;

	public ItemFile get(String id) {
		return super.get(id);
	}
	
	public List<ItemFile> findList(ItemFile itemFile) {
		return super.findList(itemFile);
	}
	
	public Page<ItemFile> findPage(Page<ItemFile> page, ItemFile itemFile) {
		return super.findPage(page, itemFile);
	}
	
	@Transactional(readOnly = false)
	public void save(ItemFile itemFile) {
		super.save(itemFile);
	}
	
	@Transactional(readOnly = false)
	public void delete(ItemFile itemFile) {
		super.delete(itemFile);
	}

	@Transactional(readOnly = false)
	public boolean updateDonwload(ItemFile itemFile){
		if(itemFileMapper.updateDonwload(itemFile)>0){
			return true;
		}else{
			return false;
		}
	}
}