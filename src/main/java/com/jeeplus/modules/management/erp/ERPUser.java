package com.jeeplus.modules.management.erp;

import com.jeeplus.core.persistence.DataEntity;

public class ERPUser extends DataEntity<ERPUser> {

    private String number; // 编码
    private String name;   // 名称

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
