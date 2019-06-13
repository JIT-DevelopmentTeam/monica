package com.jeeplus.modules.wxapi.api.core.req.model.user;

import com.jeeplus.modules.wxapi.api.core.annotation.ReqType;
import com.jeeplus.modules.wxapi.api.core.req.model.WeixinReqParam;

/**
 * 分组删除
 * 
 * @author sfli.sir
 * 
 */
@ReqType("groupDelete")
public class GroupDelete extends WeixinReqParam {

	private Group group;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
}
