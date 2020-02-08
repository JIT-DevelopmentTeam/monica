/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.icitemclass.entity.IcitemClass;

import java.util.List;

/**
 * 商品分类管理MAPPER接口
 * @author JiaChe
 * @version 2019-05-29
 */
@MyBatisMapper
public interface IcitemClassMapper extends TreeMapper<IcitemClass> {

    List<IcitemClass> findListForWechat(IcitemClass icitemClass);

    void deleteAllData();
	
}