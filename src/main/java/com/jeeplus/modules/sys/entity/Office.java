/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;

import com.jeeplus.core.persistence.TreeEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 机构Entity
 * @author jeeplus
 * @version 2016-05-15
 */
public class Office extends TreeEntity<Office> {

	private static final long serialVersionUID = 1L;
	private Area area;		// 归属区域
	private String code; 	// 机构编码
	private String type; 	// 机构类型（1：公司；2：部门；3：小组）
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String master; 	// 负责人
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String email; 	// 邮箱
	private String useable;//是否可用
	private User primaryPerson;//主负责人
	private User deputyPerson;//副负责人
	private List<String> childDeptList;//快速添加子部门
	private int qyDeptParentId;	// 企业微信部门父级id
	private int qyDeptId;	// 企业微信部门id
	private int synStatus;	// 同步状态（0：未同步1：已同步2：待同步（修改过）3：删除）
	private int isSyntoent; // 是否需要同步（0：不要同步1：要同步）

	public Office(){
		super();
		this.type = "2";
	}

	public Office(String id){
		super(id);
	}

	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public User getPrimaryPerson() {
		return primaryPerson;
	}

	public void setPrimaryPerson(User primaryPerson) {
		this.primaryPerson = primaryPerson;
	}

	public User getDeputyPerson() {
		return deputyPerson;
	}

	public void setDeputyPerson(User deputyPerson) {
		this.deputyPerson = deputyPerson;
	}

	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	@NotNull
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=1, max=1)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=100)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min=0, max=100)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min=0, max=200)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Length(min=0, max=200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return name;
	}

	public int getQyDeptParentId() {
		return qyDeptParentId;
	}

	public void setQyDeptParentId(int qyDeptParentId) {
		this.qyDeptParentId = qyDeptParentId;
	}

	public int getQyDeptId() {
		return qyDeptId;
	}

	public void setQyDeptId(int qyDeptId) {
		this.qyDeptId = qyDeptId;
	}

	public int getSynStatus() {
		return synStatus;
	}

	public void setSynStatus(int synStatus) {
		this.synStatus = synStatus;
	}

	public int getIsSyntoent() {
		return isSyntoent;
	}

	public void setIsSyntoent(int isSyntoent) {
		this.isSyntoent = isSyntoent;
	}
}