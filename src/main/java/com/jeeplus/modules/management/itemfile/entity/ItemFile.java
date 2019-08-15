/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.itemfile.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.management.icitemclass.entity.Icitem;

/**
 * 商品图片附件管理Entity
 * @author MrLISH
 * @version 2019-08-14
 */
public class ItemFile extends DataEntity<ItemFile> {
	
	private static final long serialVersionUID = 1L;
	private String originalName;		// 文件原名称
	private String name;		// 上传编码名称
	private Double size;		// 文件大小
	private String type;		// 文件类型
	private String isDown;		// 文件是否允许下载
	private String url;		// 文件路径
	private String smallUrl;		// 文件预览图路径
	private String server;		// 文件服务器地址
	private String itemId;		// 商品id
	private Integer downCount;		// 下载次数

    private Icitem icitem;     // 商品对象属性
	
	public ItemFile() {
		super();
	}

	public ItemFile(String id){
		super(id);
	}

	@ExcelField(title="文件原名称", align=2, sort=1)
	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	@ExcelField(title="上传编码名称", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="文件大小", align=2, sort=3)
	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}
	
	@ExcelField(title="文件类型", align=2, sort=4)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="文件是否允许下载", align=2, sort=5)
	public String getIsDown() {
		return isDown;
	}

	public void setIsDown(String isDown) {
		this.isDown = isDown;
	}
	
	@ExcelField(title="文件路径", align=2, sort=6)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ExcelField(title="文件预览图路径", align=2, sort=7)
	public String getSmallUrl() {
		return smallUrl;
	}

	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}
	
	@ExcelField(title="文件服务器地址", align=2, sort=8)
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
	
	@ExcelField(title="商品id", align=2, sort=9)
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	@ExcelField(title="下载次数", align=2, sort=10)
	public Integer getDownCount() {
		return downCount;
	}

	public void setDownCount(Integer downCount) {
		this.downCount = downCount;
	}

    public Icitem getIcitem() {
        return icitem;
    }

    public void setIcitem(Icitem icitem) {
        this.icitem = icitem;
    }
}