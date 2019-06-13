package com.jeeplus.modules.wxapi.jeecg.weibo.util;

import com.jeeplus.modules.wxapi.jeecg.weibo.dto.WeiboFollowersDto;
import com.jeeplus.modules.wxapi.jeecg.weibo.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

public class WeiboFollowersUtil {
	/*
	 * 
	 * 获取@当前用户的最新微博的请求必填参数验证
	 * 
	 */
	public static void getFollowersParmValidate(WeiboFollowersDto followers){
		if(StringUtils.isEmpty(followers.getAccess_token())){
			throw new BusinessException("access_token不能为空");
		}
		if(StringUtils.isEmpty(followers.getUid())&& StringUtils.isEmpty(followers.getScreen_name())){
			throw new BusinessException("uid与screen_name二者不能全为空");
		}
	}
	/*
	 * 
	 * 获取@当前用户的最新微博的请求路径
	 */
	public static String getFollowersUrl (String interUrl,WeiboFollowersDto followers){
		StringBuilder requestUrl=new StringBuilder();
		requestUrl.append(interUrl);
		if(StringUtils.isNotEmpty(followers.getAccess_token())){
			requestUrl.append("&access_token="+followers.getAccess_token());
		}
		if(StringUtils.isNotEmpty(followers.getUid())){
			requestUrl.append("&uid="+followers.getUid());
		}
		if(StringUtils.isNotEmpty(followers.getScreen_name())){
			requestUrl.append("&screen_name="+followers.getScreen_name());
		}
		if(StringUtils.isNotEmpty(followers.getCount())){
			requestUrl.append("&count="+followers.getCount());
		}
		if(StringUtils.isNotEmpty(followers.getCursor())){
			requestUrl.append("&cursor="+followers.getCursor());
		}
		if(StringUtils.isNotEmpty(followers.getTrim_status())){
			requestUrl.append("&trim_status="+followers.getTrim_status());
		}
		return requestUrl.toString();
	}
	/*
	 * 
	 * 获取用户粉丝UID列表的请求必填参数验证
	 * 
	 */
	public static void getFollowersIdsParmValidate(WeiboFollowersDto followers){
		if(StringUtils.isEmpty(followers.getAccess_token())){
			throw new BusinessException("access_token不能为空");
		}
	}
	/*
	 * 
	 * 获取用户粉丝UID列表的请求路径
	 */
	public static String getFollowersIdsUrl(String interUrl,WeiboFollowersDto followers){
		StringBuilder requestUrl=new StringBuilder();
		requestUrl.append(interUrl);
		if(StringUtils.isNotEmpty(followers.getAccess_token())){
			requestUrl.append("&access_token="+followers.getAccess_token());
		}
		if(StringUtils.isNotEmpty(followers.getUid())){
			requestUrl.append("&uid="+followers.getUid());
		}
		if(StringUtils.isNotEmpty(followers.getScreen_name())){
			requestUrl.append("&screen_name="+followers.getScreen_name());
		}
		if(StringUtils.isNotEmpty(followers.getCount())){
			requestUrl.append("&count="+followers.getCount());
		}
		if(StringUtils.isNotEmpty(followers.getCursor())){
			requestUrl.append("&cursor="+followers.getCursor());
		}
		return requestUrl.toString();
	}
}
