package com.jeeplus.modules.wxapi.jeecg.qywx.api.core.common;

public class JsapiTicket {
	private String ticket;
	private String expiresIn;
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String toString() {
		return "JsapiTicket [ticket=" + ticket + ", expiresIn="
				+ expiresIn + "]";
	}
}