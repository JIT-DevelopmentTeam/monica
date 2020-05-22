/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.customer.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.customer.entity.Customer;
import com.jeeplus.modules.management.customer.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * 客户管理Service
 * @author commit
 * @version 2019-05-30
 */
@Service
@Transactional(readOnly = true)
public class CustomerService extends CrudService<CustomerMapper, Customer> {

	@Autowired
	private CustomerMapper customerMapper;

	public Customer get(String id) {
		return super.get(id);
	}
	
	public List<Customer> findList(Customer customer) {
		return super.findList(customer);
	}
	
	public Page<Customer> findPage(Page<Customer> page, Customer customer) {
		return super.findPage(page, customer);
	}

	/**
	 * 查询列表中最大的modifytime
	 * @return
	 */
	public Long findMaxModifyTime() {
		return customerMapper.findMaxModifyTime();
	}
	
	@Transactional(readOnly = false)
	public void save(Customer customer) {
		super.save(customer);
	}
	
	@Transactional(readOnly = false)
	public void delete(Customer customer) {
		super.delete(customer);
	}

	@Transactional(readOnly = false)
    public void deleteAllData() {
	    mapper.deleteAllData();
    }
	
}