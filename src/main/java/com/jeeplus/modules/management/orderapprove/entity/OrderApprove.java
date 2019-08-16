/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.orderapprove.entity;

import javax.validation.constraints.NotNull;
import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.management.sobillandentry.entity.Sobill;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单流程绑定Entity
 * @author KicoChan
 * @version 2019-08-15
 */
public class OrderApprove extends DataEntity<OrderApprove> {
	
	private static final long serialVersionUID = 1L;
	private Integer type;		// 流程类型
	private User approvalEmplId;		// 流程节点用户id
	private Integer index;		// 排序
	private String name;		// 节点名称
	private Integer status;		// 审批状态(0:未审核,1:通过，2:拒绝)
	private Date date;		// 审核时间
	private String remark;		// 审核评议
	private Integer isToapp;		// 是否进入待审核状态
	private Integer isLast;		// 是否为最后节点
	private Sobill sobillId;		// 订单id
    private Integer unprocessed;    // 是否为待处理数据
    private Integer processed;      // 是否为已处理数据
    private String createDateStr;       // 字符串格式创建时间
    private String cusName;     // 客户名称
    private Integer startPage;		// 起始分页
    private Integer endPage;	// 结束分页
	
	public OrderApprove() {
		super();
	}

	public OrderApprove(String id){
		super(id);
	}

	@NotNull(message="流程类型不能为空")
	@ExcelField(title="流程类型", dictType="order_Approve_Type", align=2, sort=7)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@NotNull(message="流程节点用户id不能为空")
	@ExcelField(title="流程节点用户id", fieldType=User.class, value="", align=2, sort=8)
	public User getApprovalEmplId() {
		return approvalEmplId;
	}

	public void setApprovalEmplId(User approvalEmplId) {
		this.approvalEmplId = approvalEmplId;
	}
	
	@NotNull(message="排序不能为空")
	@ExcelField(title="排序", align=2, sort=9)
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	@ExcelField(title="节点名称", align=2, sort=10)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="审批状态(0:未审核,1:通过，2:拒绝)", dictType="order_Approve_Status", align=2, sort=11)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核时间", align=2, sort=12)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@ExcelField(title="审核评议", align=2, sort=13)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ExcelField(title="是否进入待审核状态", dictType="order_Approve_IsToapp", align=2, sort=14)
	public Integer getIsToapp() {
		return isToapp;
	}

	public void setIsToapp(Integer isToapp) {
		this.isToapp = isToapp;
	}
	
	@ExcelField(title="是否为最后节点", dictType="order_Approve_IsLast", align=2, sort=15)
	public Integer getIsLast() {
		return isLast;
	}

	public void setIsLast(Integer isLast) {
		this.isLast = isLast;
	}
	
	@NotNull(message="订单id不能为空")
	@ExcelField(title="订单id", fieldType=Sobill.class, value="", align=2, sort=16)
	public Sobill getSobillId() {
		return sobillId;
	}

	public void setSobillId(Sobill sobillId) {
		this.sobillId = sobillId;
	}

    public Integer getUnprocessed() {
        return unprocessed;
    }

    public void setUnprocessed(Integer unprocessed) {
        this.unprocessed = unprocessed;
    }

    public Integer getProcessed() {
        return processed;
    }

    public void setProcessed(Integer processed) {
        this.processed = processed;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getEndPage() {
        return endPage;
    }

    public void setEndPage(Integer endPage) {
        this.endPage = endPage;
    }
}