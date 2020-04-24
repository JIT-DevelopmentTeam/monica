package com.jeeplus.modules.sys.dto;

import com.jeeplus.core.persistence.DataEntity;

public class ErpDeptDTO extends DataEntity<ErpDeptDTO> {

    private String erpNumber;
    private String erpName;

    public String getErpNumber() {
        return erpNumber;
    }

    public void setErpNumber(String erpNumber) {
        this.erpNumber = erpNumber;
    }

    public String getErpName() {
        return erpName;
    }

    public void setErpName(String erpName) {
        this.erpName = erpName;
    }
}
