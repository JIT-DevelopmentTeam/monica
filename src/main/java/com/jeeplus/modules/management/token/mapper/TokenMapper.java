/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.token.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.token.entity.Token;

/**
 * Token表MAPPER接口
 * @author KicoChan
 * @version 2019-08-24
 */
@MyBatisMapper
public interface TokenMapper extends BaseMapper<Token> {
	
}