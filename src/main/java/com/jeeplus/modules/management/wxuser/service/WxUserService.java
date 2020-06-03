/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.wxuser.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.wxuser.entity.WxUser;
import com.jeeplus.modules.management.wxuser.mapper.WxUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信用户Service
 * @author Vigny
 * @version 2020-05-25
 */
@Service
@Transactional(readOnly = true)
public class WxUserService extends CrudService<WxUserMapper, WxUser> {

	@Autowired
	private WxUserMapper wxUserMapper;

	public WxUser get(String id) {
		return super.get(id);
	}
	
	public List<WxUser> findList(WxUser wxUser) {
		return super.findList(wxUser);
	}
	
	public Page<WxUser> findPage(Page<WxUser> page, WxUser wxUser) {
		return super.findPage(page, wxUser);
	}

	public WxUser getByOpenId(String openId) {
		return wxUserMapper.getByOpenId(openId);
	}
	
	@Transactional(readOnly = false)
	public void save(WxUser wxUser) {
		super.save(wxUser);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxUser wxUser) {
		super.delete(wxUser);
	}
	
}