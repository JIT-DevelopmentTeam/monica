/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.jurisdiction.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.jurisdiction.entity.Jurisdiction;
import com.jeeplus.modules.management.warehouse.entity.Warehouse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存数据权限MAPPER接口
 * @author Vigny
 * @version 2020-05-27
 */
@MyBatisMapper
public interface JurisdictionMapper extends BaseMapper<Jurisdiction> {

    List<Warehouse> findJurByClientId(@Param("clientId") String clientId);

}