/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.orderapprove.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.orderapprove.entity.OrderApprove;
import com.jeeplus.modules.management.orderapprove.mapper.OrderApproveMapper;

/**
 * 订单流程绑定Service
 * @author KicoChan
 * @version 2019-08-14
 */
@Service
@Transactional(readOnly = true)
public class OrderApproveService extends CrudService<OrderApproveMapper, OrderApprove> {

	public OrderApprove get(String id) {
		return super.get(id);
	}
	
	public List<OrderApprove> findList(OrderApprove orderApprove) {
		return super.findList(orderApprove);
	}
	
	public Page<OrderApprove> findPage(Page<OrderApprove> page, OrderApprove orderApprove) {
		return super.findPage(page, orderApprove);
	}
	
	@Transactional(readOnly = false)
	public void save(OrderApprove orderApprove) {
		super.save(orderApprove);
	}
	
	@Transactional(readOnly = false)
	public void delete(OrderApprove orderApprove) {
		super.delete(orderApprove);
	}
	
}