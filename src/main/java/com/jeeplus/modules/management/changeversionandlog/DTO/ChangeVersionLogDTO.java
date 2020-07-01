package com.jeeplus.modules.management.changeversionandlog.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;
import com.jeeplus.modules.sys.entity.User;

import java.util.Date;

public class ChangeVersionLogDTO extends DataEntity<ChangeVersionLogDTO> {

    private User user;		// 变更人
    private Date date;		// 变更日期
    private Integer version;		// 变更版本
    private Double originalQuantity; // 原数量
    private Icitem item;
    private Sobill sobill;		// 所属订单

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Double getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(Double originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public Icitem getItem() {
        return item;
    }

    public void setItem(Icitem item) {
        this.item = item;
    }

    public Sobill getSobill() {
        return sobill;
    }

    public void setSobill(Sobill sobill) {
        this.sobill = sobill;
    }
}
