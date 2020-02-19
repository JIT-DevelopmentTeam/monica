/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.messagesend.service;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.modules.management.orderapprove.entity.OrderApprove;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.base.JwParamesAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common.AccessToken;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.JwMessageAPI;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.TextCard;
import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.TextCardEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.management.messagesend.entity.Messagesend;
import com.jeeplus.modules.management.messagesend.mapper.MessagesendMapper;

/**
 * 消息发送Service
 * @author MrLISH
 * @version 2019-08-14
 */
@Service
@Transactional(readOnly = true)
public class MessagesendService extends CrudService<MessagesendMapper, Messagesend> {

	@Autowired
	private UserMapper userMapper;

	public Messagesend get(String id) {
		return super.get(id);
	}
	
	public List<Messagesend> findList(Messagesend messagesend) {
		return super.findList(messagesend);
	}
	
	public Page<Messagesend> findPage(Page<Messagesend> page, Messagesend messagesend) {
		return super.findPage(page, messagesend);
	}
	
	@Transactional(readOnly = false)
	public void save(Messagesend messagesend) {
		super.save(messagesend);
	}
	
	@Transactional(readOnly = false)
	public void delete(Messagesend messagesend) {
		super.delete(messagesend);
	}


	/**
	 *
	 * @param fromUser  发送人Id
	 * @param titleCard 审核的信息标题
	 * @param toUser    接收人Id  （|）
	 * @param path      详情路径url
	 * @param isApproval 详情参数
	 */
	public void messageEend(String fromUser,String titleCard,String toUser,String path,String orderId,String isApproval){
		AccessToken accessToken = JwAccessTokenAPI.getAccessToken(JwParamesAPI.corpId, JwParamesAPI.monicaSecret);
		TextCard textCard = new TextCard();
		User user=new User(fromUser);
		User user1 = userMapper.get(user);
		String fromUserName=user1.getName();
		String title="审核信息推送" ;
		String date = DateUtils.get_yyy_MM_dd();  // 推送时间
		String description=
				"<div class=\"gray\">"+date+"</div>" +
				"<div class=\"normal\">有一条"+titleCard+"等待你审核申请</div>" +
				"<div class=\"highlight\">发送人:"+fromUserName+"</div>";
		String url= path+"/wechat/review/applicationDetail?id="+orderId+"&isApproval="+isApproval;   // 详情请求路径--url
		textCard.setTouser(toUser);     // 接收人
		textCard.setMsgtype("textcard");  // 消息类型
		textCard.setAgentid(JwParamesAPI.monicaAgentid);   // 企业微信的应用agentId
		TextCardEntity textCardEntity=new TextCardEntity(); //消息文本对象
		textCardEntity.setTitle(title);
		textCardEntity.setDescription(description);
		textCardEntity.setUrl(url);
		textCardEntity.setBtntxt("详情");
		textCard.setTextcard(textCardEntity);
		textCard.setEnable_id_trans("0");
		JSONObject jsonObject = JwMessageAPI.SendTextcardMessage(textCard, accessToken.getAccesstoken());
		System.out.println(jsonObject);
		return;
	}

	@Test
	public void test(){

	}
}