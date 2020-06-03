package com.jeeplus.common.utils.token;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.http.HttpHelper;
import org.springframework.util.ObjectUtils;


/**
 * K/3Token工具
 *
 * @author KicoChan
 */
public class K3TokenUtils {

    // 莫尔卡K/3测试账套授权码 authorityCode
    private static final String MONICA_AUTHORITY_CODE = "2dc0c85f7faf448c204178117d18c288109df6a189e6dbce";

    private static final String GET_TOKEN_URL = "http://139.9.6.165:800/K3API/Token/Create?authorityCode=AUTHORITY_CODE";

    /**
     * 更新K/3Token
     */
    public static void updateK3Token() {
        String url = GET_TOKEN_URL.replace("AUTHORITY_CODE", MONICA_AUTHORITY_CODE);
        if (ObjectUtils.isEmpty(CacheUtils.get("k3Token"))) {
            JSONObject jsonObject = HttpHelper.httpGet(url);
            if (!ObjectUtils.isEmpty(jsonObject)) {
                if (!ObjectUtils.isEmpty(jsonObject.get("StatusCode")) && "200".equals(jsonObject.getString("StatusCode"))) {
                    JSONObject dataJson = jsonObject.getJSONObject("Data");
                    if (!ObjectUtils.isEmpty(dataJson.get("Token")) && !"".equals(dataJson.getString("Token"))) {
                        CacheUtils.put("k3Token", dataJson.getString("Token"));
                    }
                }
            }
        } else {
            JSONObject jsonObject = HttpHelper.httpGet(url);
            if (!ObjectUtils.isEmpty(jsonObject)) {
                if (!ObjectUtils.isEmpty(jsonObject.get("StatusCode")) && "200".equals(jsonObject.getString("StatusCode"))) {
                    JSONObject dataJson = jsonObject.getJSONObject("Data");
                    if (!ObjectUtils.isEmpty(dataJson.get("Token")) && !"".equals(dataJson.getString("Token"))) {
                        if (!CacheUtils.get("k3Token").toString().equals(dataJson.getString("Token"))) {
                            CacheUtils.remove("k3Token");
                            CacheUtils.put("k3Token",dataJson.getString("Token"));
                        }
                    }
                }
            }
        }
    }

}

