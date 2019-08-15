package com.jeeplus.common.utils.file.upland;

import com.jeeplus.common.config.Global;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 模块化上传附件--细分文件夹
 * @author Mr
 * @version 2019-07-12
 */
public class UploadFiles {

    /**
     * 分模块上传附件
     * @param files 上传附件对象数组
     * @param dis   模块名称
     * @return
     */
    public static LinkedHashMap<String, Object> uploadFile(MultipartFile files, String dis){
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        String path = Global.getClasspath()+"upload/";
        //判断file数组不能为空并且长度大于0
        /*if(files!=null&&files.length>0){*/
            //循环获取file数组中得文件
            /*for(int i = 0;i<files.length;i++){*/
                MultipartFile file = files/*[i]*/;
                // 判断文件是否为空
                if (!file.isEmpty()) {
                    try {
                        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                        // 图片名称
                        String uuid = df.format(new Date()) +""+(int)((Math.random()*9+1)*1000);
                        // 文件后缀
                        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
                        // 文件保存路径
                        String savePath = "";
                        // 保存数据库url路径
                        String fileUrl= "";
                        if("jpg".equals(suffix) || "png".equals(suffix)){
                            path += dis + "/jpg/";
                            savePath = path + dis + uuid +"."+suffix;
                            fileUrl ="/upload/"+ dis + "/jpg/"+dis + uuid +"."+suffix;
                        }else if("pdf".equals(suffix)){
                            path += dis + "/pdf/";
                            savePath = path + dis + uuid +"."+suffix;
                            fileUrl ="/upload/"+ dis + "/pdf/"+ dis + uuid +"."+suffix;
                        }else if("xlsx".equals(suffix) ||  "xls".equals(suffix)){
                            path += dis + "/xlsx/";
                            savePath = path +  dis + uuid +"."+suffix;
                            fileUrl ="/upload/"+ dis + "/xlsx/"+ dis + uuid +"."+suffix;
                        }else if("docx".equals(suffix) ||  "doc".equals(suffix)){
                            path += dis + "/docx/";
                            savePath = path + dis + uuid +"."+suffix;
                            fileUrl ="/upload/"+ dis + "/docx/"+ dis + uuid +"."+suffix;
                        }else{
                            path += dis + "/other/";
                            savePath = path + dis + uuid +"."+suffix;
                            fileUrl ="/upload/"+ dis + "/other/"+ dis + uuid +"."+suffix;
                        }
                        File filepath = new File(path);
                        if (!filepath.exists())
                            filepath.mkdirs();
                        // 转存文件
                        file.transferTo(new File(savePath));
                        // 返回参数
                        params.put("originalName",file.getOriginalFilename());
                        params.put("name",dis + uuid);
                        params.put("size",file.getSize() / 1024);
                        params.put("type",suffix);
                        params.put("url",fileUrl);
                        //压缩
                        if("jpg".equals(suffix) || "png".equals(suffix)){
                            path += "/small/";
                            File fileSmall = new File(path);
                            if (!fileSmall.exists())
                                fileSmall.mkdirs();
                            String thumbnailFilePathName = path + dis + uuid +"small."+suffix;
                            String smallUrl= "/upload/"+ dis + "/jpg/small/"+ dis + uuid +"small."+suffix;
                            Thumbnails.of(savePath).scale(0.2f).outputFormat("jpg").toFile(thumbnailFilePathName);
                            params.put("smallUrl",smallUrl);
                        }
                        params.put("success",true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        params.put("error",false);
                    }
                }
           /* }
        }*/
        return params;
    }
}
