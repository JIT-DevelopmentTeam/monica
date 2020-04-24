/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 机构Service
 * @author jeeplus
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeMapper, Office> {

	@Autowired
	private OfficeMapper officeMapper;
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getOfficeAllList();
		}else{
			return UserUtils.getOfficeList();
		}
	}

	public List<Office> findByQyDeptParentId(int DEL_FLAG_NORMAL, int qyDeptParentId){
		return officeMapper.findByQyDeptParentId(DEL_FLAG_NORMAL, qyDeptParentId);
	}

	public List<Office> findByParentIds(int DEL_FLAG_NORMAL, String parentIds) {
		return officeMapper.findByParentIds(DEL_FLAG_NORMAL, parentIds);
	}
	
	@Transactional(readOnly = true)
	public List<Office> findList(Office office){
        return officeMapper.findList(office);
	}
	
	@Transactional(readOnly = true)
	public Office getByCode(String code){
		return officeMapper.getByCode(code);
	}

	@Transactional(readOnly = true)
	public Office getByName(String name){
		return officeMapper.getByName(name);
	}
	
	public List<Office> getChildren(String parentId){
		return officeMapper.getChildren(parentId);
	}
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

	@Transactional(readOnly = false)
	public void deleteByParentId(String id) {
		officeMapper.deleteByParentId(id);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

	@Transactional(readOnly = false)
	public void deleteLogical(Office office) {
		officeMapper.deleteLogical(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

    @Transactional(readOnly = true)
    public Office getEntity(Office office){
        return officeMapper.getEntity(office);
    }

	@Transactional(readOnly = false)
	public void updateERP(String id, String erpDeptNumber, String erpDeptName) {
		officeMapper.updateERP(id, erpDeptNumber, erpDeptName);
	}
	
}
