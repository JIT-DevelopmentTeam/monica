/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.apiurl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.apiurl.entity.ApiUrl;
import com.jeeplus.modules.management.apiurl.mapper.ApiUrlMapper;

/**
 * 接口管理Service
 * @author 家成
 * @version 2019-05-30
 */
@Service
@Transactional(readOnly = true)
public class ApiUrlService extends CrudService<ApiUrlMapper, ApiUrl> {

	public ApiUrl get(String id) {
		return super.get(id);
	}
	
	public List<ApiUrl> findList(ApiUrl apiUrl) {
		return super.findList(apiUrl);
	}
	
	public Page<ApiUrl> findPage(Page<ApiUrl> page, ApiUrl apiUrl) {
		return super.findPage(page, apiUrl);
	}
	
	@Transactional(readOnly = false)
	public void save(ApiUrl apiUrl) {
		super.save(apiUrl);
	}
	
	@Transactional(readOnly = false)
	public void delete(ApiUrl apiUrl) {
		super.delete(apiUrl);
	}
	
}