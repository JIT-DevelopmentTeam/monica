/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.icitemclass.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 商品资料Entity
 * @author JiaChe
 * @version 2019-05-29
 */
public class Icitem extends DataEntity<Icitem> {
	
	private static final long serialVersionUID = 1L;
	private String erpId;		// erp端id
	private IcitemClass classId;		// 分类id 父类
	private String number;		// 编号
	private String name;		// 商品名称
	private String model;		// 商品型号
	private String unit;		// 商品计算单位
	private String erpclassId;		// erp端分类id
	private String modifyTime;		// 同步时间戳
	private String erpNote;		// erp备注
	private String status;		// 状态
	private String salePrice;	// 销售价格
	private String describe;	// 商品描述
	private String itemClassName;	// 分类名称
	private Integer startPage;	// 起始分页
	private Integer endPage;	// 结束分页
    private String ids;         // ids
    private Integer isSelect;   // 是否为选择数据
	private String info;	// 商品信息（用途：条件搜索）

	public Icitem() {
		super();
	}

	public Icitem(String id){
		super(id);
	}

	public Icitem(IcitemClass classId){
		this.classId = classId;
	}

	@ExcelField(title="erp端id", align=2, sort=1)
	public String getErpId() {
		return erpId;
	}

	public void setErpId(String erpId) {
		this.erpId = erpId;
	}
	
	public IcitemClass getClassId() {
		return classId;
	}

	public void setClassId(IcitemClass classId) {
		this.classId = classId;
	}
	
	@ExcelField(title="编号", align=2, sort=3)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	@ExcelField(title="商品名称", align=2, sort=4)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="商品型号", align=2, sort=5)
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	@ExcelField(title="商品计算单位", align=2, sort=6)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@ExcelField(title="erp端分类id", align=2, sort=7)
	public String getErpclassId() {
		return erpclassId;
	}

	public void setErpclassId(String erpclassId) {
		this.erpclassId = erpclassId;
	}
	
	@ExcelField(title="同步时间戳", align=2, sort=8)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@ExcelField(title="erp备注", align=2, sort=9)
	public String getErpNote() {
		return erpNote;
	}

	public void setErpNote(String erpNote) {
		this.erpNote = erpNote;
	}
	
	@ExcelField(title="状态", align=2, sort=10)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getItemClassName() {
		return itemClassName;
	}

	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Integer getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;
    }

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}