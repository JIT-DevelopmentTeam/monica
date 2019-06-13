package com.jeeplus.modules.wxapi.api.wxbase.wxmedia.model;

import com.jeeplus.modules.wxapi.api.wxsendmsg.model.WxArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * 获得媒体信息
 * @author lihongxuan
 *
 */
public class WxArticlesRespponseByMaterial {
	List<WxArticle> news_item = new ArrayList<WxArticle>();

	

	public List<WxArticle> getNews_item() {
		return news_item;
	}



	public void setNews_item(List<WxArticle> news_item) {
		this.news_item = news_item;
	}



	@Override
	public String toString() {
		return "WxArticlesRequest [news_item=" + news_item + "]";
	}

}
