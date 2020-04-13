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
	private String name;		// 字段名称
	private String beforeChange;		// 变更前
	private String afterChange;		// 变更后
	private String type;		// 变更类型
	private ChangeVersion changeVersion;		// 所属版本
    private Icitem item;
	
    public ChangeLog() {
        super();
    }

	public ChangeLog(String id){
		super(id);
	}

	@ExcelField(title="字段名称", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="变更前", align=2, sort=8)
	public String getBeforeChange() {
		return beforeChange;
	}

	public void setBeforeChange(String beforeChange) {
		this.beforeChange = beforeChange;
	}
	
	@ExcelField(title="变更后", align=2, sort=9)
	public String getAfterChange() {
		return afterChange;
	}

	public void setAfterChange(String afterChange) {
		this.afterChange = afterChange;
	}
	
	@ExcelField(title="变更类型", dictType="change_log_type", align=2, sort=10)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
}