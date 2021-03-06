/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.changeversionandlog.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.changeversionandlog.entity.ChangeLog;

/**
 * 变更记录MAPPER接口
 * @author KicoChan
 * @version 2020-03-09
 */
@MyBatisMapper
public interface ChangeLogMapper extends BaseMapper<ChangeLog> {
	
}