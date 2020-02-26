/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;
import com.jeeplus.modules.management.icitemclass.mapper.IcitemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品资料Service
 * @author JiaChe
 * @version 2019-05-29
 */
@Service
@Transactional(readOnly = true)
public class IcitemService extends CrudService<IcitemMapper, Icitem> {

	@Autowired
	private IcitemMapper icitemMapper;

	public Icitem get(String id) {
		return super.get(id);
	}
	
	public List<Icitem> findList(Icitem icitem) {
		return super.findList(icitem);
	}
	
	public Page<Icitem> findPage(Page<Icitem> page, Icitem icitem) {
		return super.findPage(page, icitem);
	}

	/**
	 * 根据产品编号查询产品信息
	 * @param DEL_FLAG_NORMAL
	 * @param number
	 * @return
	 */
	public Icitem findByNumber(String DEL_FLAG_NORMAL, String number) {
		return icitemMapper.findByNumber(DEL_FLAG_NORMAL, number);
	}

	/**
	 * 查询列表中最大的modifytime
	 * @return
	 */
	public String findMaxModifyTime() {
		return icitemMapper.findMaxModifyTime();
	}
	
	@Transactional(readOnly = false)
	public void save(Icitem icitem) {
		super.save(icitem);
	}

	/**
	 * 批量插入数据
	 * @param list
	 */
	@Transactional(readOnly = false)
	public void batchInsert(List<Icitem> list) {
		icitemMapper.batchInsert(list);
	}
	
	@Transactional(readOnly = false)
	public void delete(Icitem icitem) {
		super.delete(icitem);
	}

	@Transactional(readOnly = false)
    public void deleteAllData() {
	    mapper.deleteAllData();
    }

}