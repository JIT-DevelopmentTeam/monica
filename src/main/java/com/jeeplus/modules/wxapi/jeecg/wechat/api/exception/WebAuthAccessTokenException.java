package com.jeeplus.modules.wxapi.jeecg.wechat.api.exception;

public class WebAuthAccessTokenException extends Exception {

    public WebAuthAccessTokenException(String message){
        this(message, "");
    }

    public WebAuthAccessTokenException(String message, String respData){
        super(message + " " + respData);
    }

}
