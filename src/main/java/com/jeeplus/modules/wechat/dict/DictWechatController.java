package com.jeeplus.modules.wechat.dict;

import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.DictType;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${frontPath}/wechat/sys/dict")
public class DictWechatController extends BaseController {

    @Autowired
    private DictTypeService dictTypeService;

    @ResponseBody
    @RequestMapping(value = "listData")
    public List<DictType> listData(@RequestParam(required=false) String type) {
        DictType dictType = new DictType();
        dictType.setType(type);
        return dictTypeService.findList(dictType);
    }

}
