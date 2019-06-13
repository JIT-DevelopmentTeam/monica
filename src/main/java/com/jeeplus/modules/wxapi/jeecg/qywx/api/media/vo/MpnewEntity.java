package com.jeeplus.modules.wxapi.jeecg.qywx.api.media.vo;

import com.jeeplus.modules.wxapi.jeecg.qywx.api.message.vo.MpnewsArticles;

public class MpnewEntity {
private MpnewArticles[] articles;//	图文消息，一个图文消息支持1到10个图文

public MpnewArticles[] getArticles() {
	return articles;
}

public void setArticles(MpnewArticles[] articles) {
	this.articles = articles;
}

public void setArticles(MpnewsArticles[] mpnewsArticles) {
	// TODO Auto-generated method stub
	
}

public void setMpnews(MpnewEntity mpnews) {
	// TODO Auto-generated method stub
	
}

}
