package com.jeeplus.modules.wxapi.jeecg.wechat.api.util;

public class StringUtils {

    private StringUtils(){}

    public static boolean notEmpty (String str){
        return str != null && !"".equals(str);
    }


}
