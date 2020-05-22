/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品资料MAPPER接口
 * @author JiaChe
 * @version 2019-05-29
 */
@MyBatisMapper
public interface IcitemMapper extends BaseMapper<Icitem> {

    /**
     * 根据产品编号查询产品信息
     * @param DEL_FLAG_NORMAL
     * @param number
     * @return
     */
    Icitem findByNumber(@Param("DEL_FLAG_NORMAL") String DEL_FLAG_NORMAL, @Param("number") String number);

    void deleteAllData();

    /**
     * 查询列表中最大的modifytime
     * @return
     */
    Long findMaxModifyTime();

    /**
     * 批量插入数据
     * @param list
     */
    void batchInsert(List<Icitem> list);

}