/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.itemfile.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.itemfile.entity.ItemFile;

/**
 * 商品图片附件管理MAPPER接口
 * @author MrLISH
 * @version 2019-08-14
 */
@MyBatisMapper
public interface ItemFileMapper extends BaseMapper<ItemFile> {
	int updateDonwload(ItemFile itemFile);
}