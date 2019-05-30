/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.sobillandentry.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 子订单管理表Entity
 * @author KicoChan
 * @version 2019-05-30
 */
public class Sobillentry extends DataEntity<Sobillentry> {
	
	private static final long serialVersionUID = 1L;
	private Sobill sobillId;		// 父订单id 父类
	private String itemId;		// 商品id
	private String unit;		// 商品单位
	private String auxpropid;		// 辅助属性id
	private String batchNo;		// 批号
	private Double price;		// 单价
	private Double auxqty;		// 数量
	private Double amount;		// 总额
	private Integer rowId;		// 行序号
	private Integer synStatus;		// 订单同步状态
	private Date synTime;		// 订单同步时间
	private String custId;		// 客户id
	private String deptId;		// 订单归属部门
	private String emplId;		// 订单归属员工
	
	public Sobillentry() {
		super();
	}

	public Sobillentry(String id){
		super(id);
	}

	public Sobillentry(Sobill sobillId){
		this.sobillId = sobillId;
	}

	public Sobill getSobillId() {
		return sobillId;
	}

	public void setSobillId(Sobill sobillId) {
		this.sobillId = sobillId;
	}
	
	@ExcelField(title="商品id", align=2, sort=8)
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	@ExcelField(title="商品单位", align=2, sort=9)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@ExcelField(title="辅助属性id", align=2, sort=10)
	public String getAuxpropid() {
		return auxpropid;
	}

	public void setAuxpropid(String auxpropid) {
		this.auxpropid = auxpropid;
	}
	
	@ExcelField(title="批号", align=2, sort=11)
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	@NotNull(message="单价不能为空")
	@ExcelField(title="单价", align=2, sort=12)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=13)
	public Double getAuxqty() {
		return auxqty;
	}

	public void setAuxqty(Double auxqty) {
		this.auxqty = auxqty;
	}
	
	@NotNull(message="总额不能为空")
	@ExcelField(title="总额", align=2, sort=14)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@NotNull(message="行序号不能为空")
	@ExcelField(title="行序号", align=2, sort=15)
	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}
	
	@ExcelField(title="订单同步状态", dictType="", align=2, sort=16)
	public Integer getSynStatus() {
		return synStatus;
	}

	public void setSynStatus(Integer synStatus) {
		this.synStatus = synStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="订单同步时间", align=2, sort=17)
	public Date getSynTime() {
		return synTime;
	}

	public void setSynTime(Date synTime) {
		this.synTime = synTime;
	}
	
	@ExcelField(title="客户id", align=2, sort=18)
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}
	
	@ExcelField(title="订单归属部门", align=2, sort=19)
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	@ExcelField(title="订单归属员工", align=2, sort=20)
	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}
	
}