/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.changeversionandlog.entity;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;

/**
 * 变更记录Entity
 * @author KicoChan
 * @version 2020-03-09
 */
public class ChangeLog extends DataEntity<ChangeLog> {
	
	private static final long serialVersionUID = 1L;
	private Double originalQuantity; // 原数量
	private ChangeVersion changeVersion;		// 所属版本
    private Icitem item;
	
    public ChangeLog() {
        super();
    }

	public ChangeLog(String id){
		super(id);
	}

	@ExcelField(title="所属版本", align=2, sort=11)
	public ChangeVersion getChangeVersion() {
		return changeVersion;
	}

	public void setChangeVersion(ChangeVersion changeVersion) {
		this.changeVersion = changeVersion;
	}

    public Icitem getItem() {
        return item;
    }

    public void setItem(Icitem item) {
        this.item = item;
    }

    public Double getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(Double originalQuantity) {
        this.originalQuantity = originalQuantity;
    }
}