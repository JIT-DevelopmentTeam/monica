package com.jeeplus.modules.sys.listener.token;



import com.jeeplus.common.utils.token.AccessTokenUtils;
import com.jeeplus.common.utils.token.K3TokenUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Token监听器
 * @author KicoChan
 */
public class TokenListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 服务器启动监听器执行强制更新token
        //  应用AccessToken更新
        AccessTokenUtils.updateAgentToken();
        // K/3Token更新
        K3TokenUtils.updateK3Token();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
