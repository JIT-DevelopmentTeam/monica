package com.jeeplus.common.utils.k3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.http.HttpHelper;
import com.jeeplus.common.utils.token.K3TokenUtils;
import org.springframework.util.ObjectUtils;

import java.util.Map;


/**
 * k/3销售订单工具类
 * @author KicoChan
 */
public class K3SOUtils {

    private static final String SO_SAVE_URL = "http://139.9.6.165:800/K3API/SO/Save?token=TOKEN";

    private static final String SO_EDIT_URL = "http://139.9.6.165:800/K3API/SO/Update?token=TOKEN";

    public static JSONObject saveSO(Map<String,Object> dataMap){
        if (ObjectUtils.isEmpty(CacheUtils.get("k3Token"))) {
            K3TokenUtils.updateK3Token();
        }
        String token = CacheUtils.get("k3Token").toString();
        String url;
        if (ObjectUtils.isEmpty(dataMap.get("FBillNo"))) {
            url = SO_SAVE_URL;
        } else {
            url = SO_EDIT_URL;
        }
        System.out.println(JSON.toJSONString(dataMap));
        JSONObject resultObject = HttpHelper.httpPost(url.replace("TOKEN", token),dataMap);
        System.out.println(resultObject);
        return resultObject;
    }

}
