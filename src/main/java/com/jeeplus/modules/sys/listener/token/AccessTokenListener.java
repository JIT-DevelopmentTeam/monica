package com.jeeplus.modules.sys.listener.token;



import com.jeeplus.common.utils.token.AccessTokenUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Token监听器
 * @author KicoChan
 */
public class AccessTokenListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 服务器启动监听器执行强制更新token

        //  应用AccessToken更新
        AccessTokenUtils.updateAgentToken();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
