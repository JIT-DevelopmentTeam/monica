/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.itemfile.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.utils.file.dwnload.DownloadFile;
import com.jeeplus.common.utils.file.upland.UploadFiles;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.itemfile.entity.ItemFile;
import com.jeeplus.modules.management.itemfile.service.ItemFileService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品图片附件管理Controller
 * @author MrLISH
 * @version 2019-08-14
 */
@Controller
@RequestMapping(value = "${adminPath}/management/itemfile/itemFile")
public class ItemFileController extends BaseController {

	@Autowired
	private ItemFileService itemFileService;
	
	@ModelAttribute
	public ItemFile get(@RequestParam(required=false) String id) {
		ItemFile entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = itemFileService.get(id);
		}
		if (entity == null){
			entity = new ItemFile();
		}
		return entity;
	}
	
	/**
	 * 上传商品图片列表页面
	 */
	@RequiresPermissions("management:itemfile:itemFile:list")
	@RequestMapping(value = {"list", ""})
	public String list(ItemFile itemFile, Model model) {
		model.addAttribute("itemFile", itemFile);
		return "modules/management/itemfile/itemFileList";
	}

	/**
	 * 跳转上传商品图片页面
	 */
	@RequestMapping(value = "toUpload/{itemId}")
	public String goUplaod(@PathVariable("itemId") String itemId, Model model) {
		model.addAttribute("itemId", itemId);
		return "modules/management/itemfile/upload";
	}

	@RequestMapping(value = {"upload/icItemPic", ""})
    @ResponseBody
	public AjaxJson uplaod(HttpServletRequest request, HttpServletResponse response) {
	    AjaxJson json=new AjaxJson();
		MultipartHttpServletRequest Murequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> files = Murequest.getFileMap();// 得到文件map对象
		String itemId = request.getParameter("itemId");
		System.out.println(itemId);
		/*String upaloadUrl = new File(this.getClass().getClassLoader().getResource("/").getPath())
				.getParentFile().getParentFile().getAbsolutePath()+ "/upload/";*/// 得到当前工程路径拼接上文件名
        String dir = "icitemPic";
		int counter = 0;
		ItemFile itemFile=null;
		for (MultipartFile file : files.values()) {
			counter++;
			if (!file.isEmpty()) {// 文件名不存在 则新建文件，并将文件复制到新建文件中
                LinkedHashMap<String, Object> uploadFile = UploadFiles.uploadFile(file, dir);
                itemFile = new ItemFile();
                for (Map.Entry<String, Object> entry : uploadFile.entrySet()) {
                    switch (entry.getKey()) {
                        case "originalName":
                            itemFile.setOriginalName(entry.getValue().toString());
                            continue;
                        case "name":
                            itemFile.setName(entry.getValue().toString());
                            continue;
                        case "size":
                            itemFile.setSize(Double.parseDouble(entry.getValue().toString()));
                            continue;
                        case "type":
                            itemFile.setType(entry.getValue().toString());
                            continue;
                        case "url":
                            itemFile.setUrl(entry.getValue().toString());
                            continue;
                        case "smallUrl":
                            itemFile.setSmallUrl(entry.getValue().toString());
                            continue;
                        default:
                            if ("true".equals(entry.getValue().toString())) {
                                itemFile.setServer(request.getServerName() + ":" + request.getServerPort());
                                itemFile.setItemId(itemId);   // 当前阶段Id
                                itemFile.setIsDown("1");
                                itemFile.setDownCount(0);
                                itemFileService.save(itemFile);
                                json.setSuccess(true);
                                json.setMsg("商品图片，上传成功！");
                            } else {
                                json.setSuccess(false);
                                json.setMsg("商品图片，上传失败！");
                            }
                            continue;
                    }
                }
			}
		}
		return json;
	}

		/**
	 * 上传商品图片列表数据
	 */
	@ResponseBody
	@RequiresPermissions("management:itemfile:itemFile:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ItemFile itemFile, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ItemFile> page = itemFileService.findPage(new Page<ItemFile>(request, response), itemFile); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑上传商品图片表单页面
	 */
	@RequiresPermissions(value={"management:itemfile:itemFile:view","management:itemfile:itemFile:add","management:itemfile:itemFile:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ItemFile itemFile, Model model) {
		model.addAttribute("itemFile", itemFile);
		return "modules/management/itemfile/itemFileForm";
	}

    /**
     * 查看图片
     * @param itemFile
     * @param model
     * @return
     */
    @RequiresPermissions(value={"management:itemfile:itemFile:view"},logical=Logical.OR)
    @RequestMapping(value = "showPic")
	public String showPic(ItemFile itemFile, Model model,HttpServletRequest request) {
       String path = Global.getClasspath()+itemFile.getUrl();
		model.addAttribute("itemFile", itemFile);
		return "modules/management/itemfile/showPicture";
	}

	/**
	 * 保存上传商品图片
	 */
	@ResponseBody
	@RequiresPermissions(value={"management:itemfile:itemFile:add","management:itemfile:itemFile:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ItemFile itemFile, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(itemFile);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		itemFileService.save(itemFile);//保存
		j.setSuccess(true);
		j.setMsg("保存上传商品图片成功");
		return j;
	}
	
	/**
	 * 删除上传商品图片
	 */
	@ResponseBody
	@RequiresPermissions("management:itemfile:itemFile:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ItemFile itemFile) {
		AjaxJson j = new AjaxJson();
		itemFileService.delete(itemFile);
		j.setMsg("删除上传商品图片成功");
		return j;
	}
	
	/**
	 * 批量删除上传商品图片
	 */
	@ResponseBody
	@RequiresPermissions("management:itemfile:itemFile:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		String path=Global.getClasspath();
		for(String id : idArray){
			ItemFile itemFile=itemFileService.get(id);
			if(itemFile.getUrl()!=null && !"".equals(itemFile.getUrl())){
				File file=new File(path+itemFile.getUrl());
				if(file.exists()){
					file.delete();
				}
			}
			if(itemFile.getSmallUrl()!=null && !"".equals(itemFile.getSmallUrl())){
				File filea=new File(path+itemFile.getSmallUrl());
				if(filea.exists()){
					filea.delete();
				}
			}
			itemFileService.delete(itemFile);
		}
		j.setMsg("删除上传商品图片成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("management:itemfile:itemFile:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ItemFile itemFile, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "上传商品图片"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ItemFile> page = itemFileService.findPage(new Page<ItemFile>(request, response, -1), itemFile);
    		new ExportExcel("上传商品图片", ItemFile.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出上传商品图片记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("management:itemfile:itemFile:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ItemFile> list = ei.getDataList(ItemFile.class);
			for (ItemFile itemFile : list){
				try{
					itemFileService.save(itemFile);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条上传商品图片记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条上传商品图片记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入上传商品图片失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入上传商品图片数据模板
	 */
	@ResponseBody
	@RequiresPermissions("management:itemfile:itemFile:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "上传商品图片数据导入模板.xlsx";
    		List<ItemFile> list = Lists.newArrayList(); 
    		new ExportExcel("上传商品图片数据", ItemFile.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

    /**
     * 网络下载企业资料附件模板
     */
    @ResponseBody
    @RequestMapping(value = "download/template/{id}")
    public void downloadFileTemplate(@PathVariable("id") String id,HttpServletResponse response,HttpServletRequest request) {
        try {
            ItemFile itemFile = itemFileService.get(id);
            if(itemFile!=null){
                itemFile.setDownCount(itemFile.getDownCount()+1);
                if(itemFileService.updateDonwload(itemFile)){
                    String path=itemFile.getUrl();
                    String newFileName=itemFile.getIcitem().getName()+"-"+(int)((Math.random()*9+1)*100)+"商品图片";
                    DownloadFile.downloadFile(path,newFileName,response,request);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}