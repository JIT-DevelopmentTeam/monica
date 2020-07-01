/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.changeversionandlog.service;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.changeversionandlog.DTO.ChangeVersionLogDTO;
import com.jeeplus.modules.management.changeversionandlog.entity.ChangeLog;
import com.jeeplus.modules.management.changeversionandlog.entity.ChangeVersion;
import com.jeeplus.modules.management.changeversionandlog.mapper.ChangeLogMapper;
import com.jeeplus.modules.management.changeversionandlog.mapper.ChangeVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

	@Autowired
	private ChangeVersionMapper changeVersionMapper;
	
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

	public Page<ChangeVersionLogDTO> findVersionLogList(Page<ChangeVersionLogDTO> page, ChangeVersionLogDTO changeVersionLogDTO) {
		dataRuleFilter(changeVersionLogDTO);
		changeVersionLogDTO.setPage(page);
		page.setList(changeVersionMapper.findVersionLogList(changeVersionLogDTO));
		return page;
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