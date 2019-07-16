package com.jeeplus.modules.qywxutils.qyutil;
/*package com.fh.qyutil;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jit.wx.info.WeixinInfo;
import com.jit.wx.menu.Button;
import com.jit.wx.menu.CommonButton;
import com.jit.wx.menu.ComplexButton;
import com.jit.wx.menu.Menu;
import com.jit.wx.menu.ViewButton;
import com.jit.wx.pojo.AccessToken;


*//**
* 类名: MenuManager </br>
* 包名： com.souvc.weixin.main
* 描述:菜单管理器类 </br>
* 发布版本：V1.0  </br>
 *//*

public class MenuManager {
	//private static Logger log = LoggerFactory.getLogger(MenuManager.class);

    public static void main(String[] args) {
        // 第三方用户唯一凭证
        String appId = WeixinInfo.AppID;
        // 第三方用户唯一凭证密钥
        String appSecret = WeixinInfo.AppSecret;

        // 调用接口获取access_token
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);
        System.out.println(at.getToken());
        if (null != at) {
            // 调用接口创建菜单
            int result = WeixinUtil.createMenu(getMenu(), at.getToken());

            // 判断菜单创建结果
            if (0 == result)
                System.out.println("菜单创建成功！");
            else
            	System.out.println("菜单创建失败，错误码：" + result);
        }
    }
    
    @Test
    public void createMenu(){
    	 // 第三方用户唯一凭证
        String appId = WeixinInfo.AppID;
        // 第三方用户唯一凭证密钥
        String appSecret = WeixinInfo.AppSecret;

        // 调用接口获取access_token
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);
        System.out.println(at.getToken());
        if (null != at) {
            // 调用接口创建菜单
            int result = WeixinUtil.createMenu(getMenu(), at.getToken());

            // 判断菜单创建结果
            if (0 == result)
                System.out.println("菜单创建成功！");
            else
            	System.out.println("菜单创建失败，错误码：" + result);
        }
    }
    
    @Test
    public void delete() {
    	// 第三方用户唯一凭证
        String appId = WeixinInfo.AppID;
        // 第三方用户唯一凭证密钥
        String appSecret = WeixinInfo.AppSecret;
		try {
			AccessToken token = WeixinUtil.getAccessToken(appId, appSecret);
			System.out.println("票据"+token.getToken());
			System.out.println("有效时间"+token.getExpiresIn());
			
			//String path = "D:/imooc.jpg";
			//String mediaId = WeixinUtil.upload(path, token.getToken(), "thumb");
			//System.out.println(mediaId);
			
			//String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
			//String menu = JSONObject.fromObjec
			int result = WeixinUtil.deleteMenu(token.getToken());
			if(result == 0){
				System.out.println("删除菜单成功！！");
			}else {
				System.out.println("错误码："+result);
			}
			
			//String result = WeixinUtil.translate("my name is laobi");
			//String result = WeixinUtil.translateFull("");
			//System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    *//**
     * 组装菜单数据
     * 
     * @return
     *//*
    private static Menu getMenu() {
        CommonButton btn11 = new CommonButton();
        btn11.setName("天气预报");
        btn11.setType("click");
        btn11.setKey("11");

        CommonButton btn12 = new CommonButton();
        btn12.setName("公交查询");
        btn12.setType("click");
        btn12.setKey("12");

        CommonButton btn13 = new CommonButton();
        btn13.setName("周边搜索");
        btn13.setType("click");
        btn13.setKey("13");

        CommonButton btn14 = new CommonButton();
        btn14.setName("天下足球");
        btn14.setType("click");
        btn14.setKey("14");

        CommonButton btn21 = new CommonButton();
        btn21.setName("英语翻译");
        btn21.setType("click");
        btn21.setKey("21");

        CommonButton btn22 = new CommonButton();
        btn22.setName("百度一下");
        btn22.setType("click");
        btn22.setKey("22");

        CommonButton btn23 = new CommonButton();
        btn23.setName("电影频道");
        btn23.setType("click");
        btn23.setKey("23");

        CommonButton btn24 = new CommonButton();
        btn24.setName("人脸识别");
        btn24.setType("click");
        btn24.setKey("24");

        CommonButton btn25 = new CommonButton();
        btn25.setName("聊天唠嗑");
        btn25.setType("click");
        btn25.setKey("25");

        CommonButton btn31 = new CommonButton();
        btn31.setName("朋友圈");
        btn31.setType("click");
        btn31.setKey("31");

        CommonButton btn32 = new CommonButton();
        btn32.setName("电影排行榜");
        btn32.setType("click");
        btn32.setKey("32");
        
        ViewButton btn32 = new ViewButton();
        btn32.setName("view菜单");
        btn32.setType("view");
        btn32.setUrl("http://jtwx.s1.natapp.cc/souvc/jsp/gps.jsp");
        
        CommonButton btn33 = new CommonButton();
        btn33.setName("幽默笑话");
        btn33.setType("click");
        btn33.setKey("33");

        
        *//**
         * 微信：  mainBtn1,mainBtn2,mainBtn3底部的三个一级菜单。
         *//*
        
        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("生活助手");
        //一级下有4个子菜单
        mainBtn1.setSub_button(new CommonButton[] { btn11, btn12, btn13, btn14 });

        
        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("休闲驿站");
        mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23, btn24, btn25 });

        
        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("更多体验");
        mainBtn3.setSub_button(new Button[] { btn31,btn32, btn33 });
    	
    	 CommonButton btn21 = new CommonButton();
         btn21.setName("英语翻译");
         btn21.setType("click");
         btn21.setKey("21");

         CommonButton btn22 = new CommonButton();
         btn22.setName("百度一下");
         btn22.setType("click");
         btn22.setKey("22");

         CommonButton btn23 = new CommonButton();
         btn23.setName("电影频道");
         btn23.setType("click");
         btn23.setKey("23");
         ComplexButton mainBtn2 = new ComplexButton();
         mainBtn2.setName("更多体验");
         mainBtn2.setSub_button(new Button[] { btn21,btn22, btn23 });
    	ViewButton vb2 = new ViewButton();
    	vb2.setName("预览开发");
    	vb2.setType("view");
    	vb2.setUrl("http://www.baidu.com");
    	ViewButton vb11 = new ViewButton();
    	vb11.setName("微门店");
    	vb11.setType("view");
    	vb11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf297fc64f92b7f99&redirect_uri=http%3a%2f%2fjittest.s1.natapp.cc%2fjmweb%2fshop%2ftoShop&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
    	
    	ViewButton vb12 = new ViewButton();
    	vb12.setName("微订货");
    	vb12.setType("view");
    	vb12.setUrl("http://www.baidu.com");
    	
    	ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("微系统");
        mainBtn1.setSub_button(new Button[] { vb11,vb12});
    	
        
    	ViewButton vb21 = new ViewButton();
    	vb21.setName("查看库存");
    	vb21.setType("view");
    	vb21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf297fc64f92b7f99&redirect_uri=http%3a%2f%2fjittest.s1.natapp.cc%2fjmweb%2ficltem%2fshowIcltem&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
    	
    	
    	ViewButton vb22 = new ViewButton();
    	vb22.setName("订单管理");
    	vb22.setType("view");
    	vb22.setUrl("http://www.baidu.com");
    	
    	ViewButton vb23 = new ViewButton();
    	vb23.setName("来往对账");
    	vb23.setType("view");
    	vb23.setUrl("http://www.baidu.com");
    	
    	ViewButton vb24 = new ViewButton();
    	vb24.setName("报表查询");
    	vb24.setType("view");
    	vb24.setUrl("http://www.baidu.com");
    	
    	ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("业务管理");
        mainBtn2.setSub_button(new Button[] { vb21,vb22,vb23,vb24});
        
        ViewButton vb31 = new ViewButton();
    	vb31.setName("身份认证");
    	vb31.setType("view");
    	vb31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf297fc64f92b7f99&redirect_uri=http%3a%2f%2fjittest.s1.natapp.cc%2fjmweb%2fwxuserRegister%2fauthentication&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
    	
    	ViewButton vb32 = new ViewButton();
    	vb32.setName("系统教程");
    	vb32.setType("view");
    	vb32.setUrl("http://www.baidu.com");
    	
    	ViewButton vb33 = new ViewButton();
    	vb33.setName("更多新闻资讯");
    	vb33.setType("view");
    	vb33.setUrl("http://www.baidu.com");
    	
        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("更多");
        mainBtn3.setSub_button(new Button[] { vb31,vb32,vb33});
    	*//**
         * 封装整个菜单
         *//*
    	Button button = new Button();
		button.setName("菜单");
		button.setSub_button(new Button[]{button31,button32});
		
		menu.setButton(new Button[]{button11,button21,button});
		return menu;
        Menu menu = new Menu();
        menu.setButton(new Button[] {mainBtn1,mainBtn2,mainBtn3});

        return menu;
    }
}
*/