package com.jeeplus.modules.wxapi.api.wxuser;

import com.jeeplus.modules.wxapi.api.core.exception.WexinReqException;
import com.jeeplus.modules.wxapi.api.wxuser.user.JwUserAPI;

public class Test {

	public static void main(String[] args) {
		try {
			String accesstoken = "_R_E8aQV0KRn0TQLCRrpCwEK0GBvnK81BiBrfY0FTuiq1qDPj6kigbRhwLG4S0TTC_mNcnFjiN5dQsgx3slbdJELpoO21uCqnj_P2I-e18ISNGjCJALGZ";
			String user_openid = "oGCDRjkyK52FaBz8zCnG8mR-c92Q";
			JwUserAPI.getWxuser(accesstoken, user_openid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
