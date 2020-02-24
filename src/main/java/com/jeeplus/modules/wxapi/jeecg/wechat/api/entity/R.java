package com.jeeplus.modules.wxapi.jeecg.wechat.api.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * 
 * @author xlf
 * @email xlfbe696@gmail.com
 * @date 2017年4月19日 上午11:58:56
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static final String SUCCESS = "success";
    
    public static final String EXCEPTION = "exception";
    
    public static final Integer SUCCESSCODE = 0;
        
    public static final Integer EXCEPTIONCODE = 500;
    

    public R() {
        put("errCode", 0);
        put("msg", SUCCESS);
    }
    
    public R(int code, String msg){
           put("errCode", code);
            put("msg", msg);
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("errCode", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}