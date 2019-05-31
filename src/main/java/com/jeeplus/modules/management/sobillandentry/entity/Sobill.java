/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.sobillandentry.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单模块Entity
 * @author KicoChan
 * @version 2019-05-30
 */
public class Sobill extends DataEntity<Sobill> {
	
	private static final long serialVersionUID = 1L;
	private String erpId;		// erpid
	private Integer type;		// 订单类型
	private String billNo;		// 订单编码
	private Integer synStatus;		// 订单同步状态
	private Date synTime;		// 订单同步时间
	private String custId;		// 客户id
	private String emplId;		// 订单归属员工
	private Integer currencyId;		// 订单币别
	private Date needTime;		// 订单发货时间
	private Integer status;		// 订单状态
	private Integer cancellation;		// 订单是否已经取消
	private String checkerId;		// 订单审核人
	private Date checkTime;		// 订单审核时间
	private Integer checkStatus;		// 订单审核状态
	private Double amount;		// 订单总金额
	private Date beginNeedTime;		// 开始 订单发货时间
	private Date endNeedTime;		// 结束 订单发货时间
	private List<Sobillentry> sobillentryList = Lists.newArrayList();		// 子表列表
	private String emplName;	// 员工名称
	private String checkerName;	// 审核人
	private String deptName;	// 部门名称
	
	public Sobill() {
		super();
	}

	public Sobill(String id){
		super(id);
	}

	@ExcelField(title="erpid", align=2, sort=7)
	public String getErpId() {
		return erpId;
	}

	public void setErpId(String erpId) {
		this.erpId = erpId;
	}
	
	@NotNull(message="订单类型不能为空")
	@ExcelField(title="订单类型", dictType="", align=2, sort=8)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@ExcelField(title="订单编码", align=2, sort=9)
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	@ExcelField(title="订单同步状态", dictType="", align=2, sort=10)
	public Integer getSynStatus() {
		return synStatus;
	}

	public void setSynStatus(Integer synStatus) {
		this.synStatus = synStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="订单同步时间", align=2, sort=11)
	public Date getSynTime() {
		return synTime;
	}

	public void setSynTime(Date synTime) {
		this.synTime = synTime;
	}
	
	@ExcelField(title="客户id", align=2, sort=12)
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}
	
	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}
	
	@ExcelField(title="订单币别", align=2, sort=15)
	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="订单发货时间不能为空")
	@ExcelField(title="订单发货时间", align=2, sort=16)
	public Date getNeedTime() {
		return needTime;
	}

	public void setNeedTime(Date needTime) {
		this.needTime = needTime;
	}
	
	@ExcelField(title="订单状态", align=2, sort=17)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ExcelField(title="订单是否已经取消", dictType="", align=2, sort=18)
	public Integer getCancellation() {
		return cancellation;
	}

	public void setCancellation(Integer cancellation) {
		this.cancellation = cancellation;
	}
	
	public String getCheckerId() {
		return checkerId;
	}

	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="订单审核时间", align=2, sort=20)
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	
	@ExcelField(title="订单审核状态", dictType="", align=2, sort=21)
	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	@NotNull(message="订单总金额不能为空")
	@ExcelField(title="订单总金额", align=2, sort=22)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Date getBeginNeedTime() {
		return beginNeedTime;
	}

	public void setBeginNeedTime(Date beginNeedTime) {
		this.beginNeedTime = beginNeedTime;
	}
	
	public Date getEndNeedTime() {
		return endNeedTime;
	}

	public void setEndNeedTime(Date endNeedTime) {
		this.endNeedTime = endNeedTime;
	}
		
	public List<Sobillentry> getSobillentryList() {
		return sobillentryList;
	}

	public void setSobillentryList(List<Sobillentry> sobillentryList) {
		this.sobillentryList = sobillentryList;
	}

	@ExcelField(title="订单归属员工", align=2, sort=14)
	public String getEmplName() {
		return emplName;
	}

	public void setEmplName(String emplName) {
		this.emplName = emplName;
	}

	@ExcelField(title="订单审核人", align=2, sort=19)
	public String getCheckerName() {
		return checkerName;
	}

	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}

	@ExcelField(title="订单归属部门", align=2, sort=13)
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}