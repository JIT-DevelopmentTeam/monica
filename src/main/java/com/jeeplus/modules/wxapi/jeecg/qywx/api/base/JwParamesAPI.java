package com.jeeplus.modules.wxapi.jeecg.qywx.api.base;
/**
 * @author zhoujf
 * @date 2016-04-05
 * 参数API类
 */
public class JwParamesAPI {
	// token
	public static String token = "jeewx";  
	// 随机戳
	public static String encodingAESKey = "b2rxXq7GMymOskwpkMnwKPctb6ySWnmDQVIu7q0lKOW";  
	 //你的企业号ID
	public static String corpId = "ww49c384af1f4dac63";
	// 管理组的凭证密钥，每个secret代表了对应用、通讯录、接口的不同权限；不同的管理组拥有不同的secret
	public static String secret = "GCB1HLm-Ii0lfACOjoZmW9qMdjPIKV0pOyBda-DPMec";

	public static String contactSecret = "gdV_8uxQ_rl9yjQf2Kp0zTHxXbiO8zi2bszC6tOyCmI";
	 //应用id
	public static String agentid = "4";
	
	// OAuth2 回调地址
	public static String REDIRECT_URI = "";
}
