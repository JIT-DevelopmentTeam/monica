/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.sobillandentry.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;

import java.util.List;

/**
 * 订单模块MAPPER接口
 * @author KicoChan
 * @version 2019-05-30
 */
@MyBatisMapper
public interface SobillMapper extends BaseMapper<Sobill> {

    /**
     * 获取已提交审核订单数据
     * @param sobill
     * @return
     */
    List<Sobill> findSubmittedList(Sobill sobill);

}