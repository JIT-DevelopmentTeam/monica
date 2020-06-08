/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.changeversionandlog.service;

import java.util.List;

import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.management.changeversionandlog.entity.ChangeVersion;
import com.jeeplus.modules.management.changeversionandlog.mapper.ChangeVersionMapper;
import com.jeeplus.modules.management.changeversionandlog.entity.ChangeLog;
import com.jeeplus.modules.management.changeversionandlog.mapper.ChangeLogMapper;

/**
 * 变更版本和记录Service
 * @author KicoChan
 * @version 2020-03-09
 */
@Service
@Transactional(readOnly = true)
public class ChangeVersionService extends CrudService<ChangeVersionMapper, ChangeVersion> {

	@Autowired
	private ChangeLogMapper changeLogMapper;
	
	public ChangeVersion get(String id) {
		ChangeVersion changeVersion = super.get(id);
		changeVersion.setChangeLogList(changeLogMapper.findList(new ChangeLog()));
		return changeVersion;
	}
	
	public List<ChangeVersion> findList(ChangeVersion changeVersion) {
		return super.findList(changeVersion);
	}
	
	public Page<ChangeVersion> findPage(Page<ChangeVersion> page, ChangeVersion changeVersion) {
		return super.findPage(page, changeVersion);
	}
	
	@Transactional(readOnly = false)
	public void save(ChangeVersion changeVersion) {
		super.save(changeVersion);
		for (ChangeLog changeLog : changeVersion.getChangeLogList()){
			if (changeLog.getId() == null){
				continue;
			}
			if (ChangeLog.DEL_FLAG_NORMAL.equals(changeLog.getDelFlag())){
				if (StringUtils.isBlank(changeLog.getId())){
					changeLog.preInsert();
					changeLogMapper.insert(changeLog);
				}else{
					changeLog.preUpdate();
					changeLogMapper.update(changeLog);
				}
			}else{
				changeLogMapper.delete(changeLog);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ChangeVersion changeVersion) {
		super.delete(changeVersion);
		changeLogMapper.delete(new ChangeLog());
	}

	public Integer maxVersionBySobill(ChangeVersion changeVersion) {
	    return mapper.maxVersionBySobill(changeVersion);
    }
	
}