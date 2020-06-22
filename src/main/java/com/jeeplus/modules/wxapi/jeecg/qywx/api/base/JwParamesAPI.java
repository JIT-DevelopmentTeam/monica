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
	public static String corpId = "ww2855e7a6c692c93b";
	// 管理组的凭证密钥，每个secret代表了对应用、通讯录、接口的不同权限；不同的管理组拥有不同的secret
	public static String secret = "GCB1HLm-Ii0lfACOjoZmW9qMdjPIKV0pOyBda-DPMec";

	//管理莫尔卡应用的凭证密钥
	public static String monicaSecret="CGuqFlfBvK0CCHpQVz-DqIpNFqX5vdBWcD7J41A4YPU";
	//企业应用的idt
	public static int monicaAgentid=1000002;

	//通讯录应用
	public static String contactSecret = "ScREORvyXumlN1YYbzoC7mp-fE0QB30ZXnkkvJ7xGGc";
	 //应用id
	public static String agentid = "4";
	
	// OAuth2 回调地址
	public static String REDIRECT_URI = "";
}
