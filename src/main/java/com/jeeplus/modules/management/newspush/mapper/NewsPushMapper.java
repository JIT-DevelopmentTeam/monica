/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.newspush.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.newspush.entity.NewsPush;

/**
 * 微信推送新闻对象MAPPER接口
 * @author commit
 * @version 2019-06-18
 */
@MyBatisMapper
public interface NewsPushMapper extends BaseMapper<NewsPush> {
	
}