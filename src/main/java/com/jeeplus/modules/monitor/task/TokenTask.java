package com.jeeplus.modules.monitor.task;

import com.jeeplus.common.utils.token.AccessTokenUtils;
import com.jeeplus.common.utils.token.K3TokenUtils;
import com.jeeplus.modules.monitor.entity.Task;

/**
 * token定时任务
 */
public class TokenTask extends Task {

    @Override
    public void run() {
        AccessTokenUtils.updateAgentToken();
        K3TokenUtils.updateK3Token();
    }

}
