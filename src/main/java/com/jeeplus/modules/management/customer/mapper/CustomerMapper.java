/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.customer.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.customer.entity.Customer;

/**
 * 客户管理MAPPER接口
 * @author commit
 * @version 2019-05-30
 */
@MyBatisMapper
public interface CustomerMapper extends BaseMapper<Customer> {

    void deleteAllData();

    /**
     * 查询列表中最大的modifytime
     * @return
     */
    String findMaxModifyTime();
	
}