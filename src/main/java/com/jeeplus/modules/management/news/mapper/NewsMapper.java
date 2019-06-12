/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.news.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.news.entity.News;

/**
 * 新闻公告MAPPER接口
 * @author Vigny
 * @version 2019-06-10
 */
@MyBatisMapper
public interface NewsMapper extends BaseMapper<News> {

    /**
     * 点击更新阅读次数
     * @param id
     */
    void updateReadCount(String id);

}