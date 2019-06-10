/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.enterprise.platform.web;

import com.jeeplus.core.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口管理Controller
 * @author 家成
 * @version 2019-05-30
 */
@Controller
@RequestMapping(value = "${frontPath}/management/enterprise/platform")
public class PlatformController extends BaseController {


	@ResponseBody
	@RequestMapping(value = "test")
	public Map<String,Object>  test(String parentId) throws Exception{
		Map<String,Object> json = new HashMap<>();
		System.out.println("================test================");
		return json;
	}


	@RequestMapping(value = "testjsp")
	public String testjsp(Model model) {
		return "modules/enterprise/test";
	}

	/**
	 * 主页
	 * @return
	 */
	@RequestMapping(value = {"index", ""})
	public String index() {
		return "modules/wechat/main/index";
	}

}