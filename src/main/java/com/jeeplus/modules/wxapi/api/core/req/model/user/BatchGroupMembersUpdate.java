package com.jeeplus.modules.wxapi.api.core.req.model.user;

import com.jeeplus.modules.wxapi.api.core.annotation.ReqType;
import com.jeeplus.modules.wxapi.api.core.req.model.WeixinReqParam;

import java.util.List;

/**
 * 
 * 批量更新用户
 * @author sfli.sir
 * 
 */
@ReqType("batchGroupMembersUpdate")
public class BatchGroupMembersUpdate extends WeixinReqParam {
	
	private List<String> openid_list;
	
	private String to_groupid;


	public List<String> getOpenid_list() {
		return openid_list;
	}

	public void setOpenid_list(List<String> openid_list) {
		this.openid_list = openid_list;
	}

	public String getTo_groupid() {
		return to_groupid;
	}

	public void setTo_groupid(String to_groupid) {
		this.to_groupid = to_groupid;
	}

	
}
