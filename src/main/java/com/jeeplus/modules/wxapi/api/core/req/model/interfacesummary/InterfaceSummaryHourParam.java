package com.jeeplus.modules.wxapi.api.core.req.model.interfacesummary;

import com.jeeplus.modules.wxapi.api.core.annotation.ReqType;
import com.jeeplus.modules.wxapi.api.core.req.model.WeixinReqParam;

/**
 * 参数类--获取接口分析分时数据
 * @author qimiao
 *
 * 2015年1月27日
 */
@ReqType("getinterfacesummaryhour")
public class InterfaceSummaryHourParam extends WeixinReqParam {
	
	// 开始时间
	private String begin_date = null;
	
	// 结束时间
	private String end_date = null;

	public String getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	
}
