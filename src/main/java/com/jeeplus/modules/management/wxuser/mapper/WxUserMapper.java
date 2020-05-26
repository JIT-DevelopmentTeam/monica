/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.wxuser.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.management.wxuser.entity.WxUser;
import org.apache.ibatis.annotations.Param;

/**
 * 微信用户MAPPER接口
 * @author Vigny
 * @version 2020-05-25
 */
@MyBatisMapper
public interface WxUserMapper extends BaseMapper<WxUser> {

    WxUser getByOpenId(@Param("openId") String openId);
}