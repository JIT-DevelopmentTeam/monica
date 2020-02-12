/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.management.news.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.management.news.entity.News;
import com.jeeplus.modules.management.news.service.NewsService;
import com.jeeplus.modules.management.newspush.entity.NewsPush;
import com.jeeplus.modules.management.newspush.service.NewsPushService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.service.OfficeService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 新闻公告Controller
 *
 * @author Vigny
 * @version 2019-06-10
 */
@Controller
@RequestMapping(value = "${adminPath}/management/news/news")
public class NewsController extends BaseController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private NewsPushService newsPushService;

    @ModelAttribute
    public News get(@RequestParam(required = false) String id) {
        News entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = newsService.get(id);
        }
        if (entity == null) {
            entity = new News();
        }
        return entity;
    }

    /**
     * 新闻公告列表页面
     */
    @RequiresPermissions("management:news:news:list")
    @RequestMapping(value = {"list", ""})
    public String list(News news, Model model) {
        model.addAttribute("news", news);
        return "modules/management/news/newsList";
    }

    /**
     * 新闻公告列表数据
     */
    @ResponseBody
    @RequiresPermissions("management:news:news:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(News news, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<News> page = newsService.findPage(new Page<News>(request, response), news);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑新闻公告表单页面
     */
    @RequiresPermissions(value = {"management:news:news:view", "management:news:news:add", "management:news:news:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(News news, Model model) {
        NewsPush newsPush=new NewsPush();
        newsPush.setDelFlag("0");
        newsPush.setNewsId(news.getId());
        List<NewsPush> newsPushList=newsPushService.findAllList(newsPush);
        if(newsPushList.size() > 0) {
            String getObjId = "";
            for (int i = 0; i < newsPushList.size(); i++) {
                if(newsPushList.size() == newsPushList.size()-1){
                    getObjId = newsPushList.get(i).getObjId();
                }else
                getObjId = newsPushList.get(i).getObjId() + ",";
            }
            news.setObjId(getObjId);
        }
        if (news.getId() == null) {
            news.setReadCount(0);
            model.addAttribute("checked", "true");
        }
        model.addAttribute("news", news);
        return "modules/management/news/newsForm";
    }

    @RequestMapping(value = "newsUserList")
    public ModelAndView userList(ModelAndView modelAndView){
        modelAndView.setViewName("modules/management/news/userList");
        return modelAndView;
    }

    /**
     * 查看新闻图片封面跳转页面
     *
     * @return
     */
    @RequestMapping(value = "picMainpic")
    public ModelAndView newsMainpic(News news) {
        String path = Global.getClasspath()+news.getMainpic();
        ModelAndView view = new ModelAndView();
        view.addObject("news",news);
        view.setViewName("modules/management/news/newsMainpic");
        return view;
    }

    /**
     * 新闻图片封面上传跳转页面
     *
     * @return
     */
    @RequestMapping(value = "uploadFile")
    public ModelAndView uploadFile() {
        ModelAndView view = new ModelAndView();
        view.setViewName("modules/management/news/upload");
        return view;
    }

    /**
     * 保存新闻公告
     */
    @ResponseBody
    @RequiresPermissions(value = {"management:news:news:add", "management:news:news:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(News news,HttpServletRequest request,Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(news);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        try {
            //新增或编辑表单保存
            newsService.save(news);//保存
            String objIds = news.getObjId();
            if(objIds != null){
                String[] objIdArr = objIds.split(",");
                /*System.out.println("--->:"+objIds);*/
                NewsPush newsPush = null;
                NewsPush del = new NewsPush();
                del.setNewsId(news.getId());
                newsPushService.delete(del);
                for (int i = 0; i < objIdArr.length; i++) {
                    newsPush = new NewsPush();
                    newsPush.setNewsId(news.getId());
                    newsPush.setObjId(objIdArr[i]);
                    newsPushService.save(newsPush);  // 保存推送对象表信息
                }
                if(news.getIsPush() != null && news.getIsPush() == 1){
                    if (!news.getIsNewRecord()) {
                        JobKey key = new JobKey(news.getTitle(), news.getPushrule());
                        try {
                            scheduler.deleteJob(key);
                        } catch (SchedulerException e) {
                            e.printStackTrace();
                            System.out.println("出现异常");
                        }
                    }
                    newsService.task(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        j.setSuccess(true);
        j.setMsg("保存新闻公告成功");
        return j;
    }

    /**
     * 删除新闻公告
     */
    @ResponseBody
    @RequiresPermissions("management:news:news:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(News news) {
        AjaxJson j = new AjaxJson();
        newsService.delete(news);
        j.setMsg("删除新闻公告成功");
        return j;
    }

    /**
     * 批量删除新闻公告
     */
    @ResponseBody
    @RequiresPermissions("management:news:news:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        List<News> newsList = Lists.newArrayList();
        News news = null;
        for (String id : idArray) {
            news = newsService.get(id);
            if (news != null) {
                String realPath = Global.getClasspath() + news.getMainpic();
                System.out.println(realPath);
                FileUtils.deleteFile(realPath);
                newsList.add(news);
            }
        }
        newsService.deleteAllByLogic(newsList);
        j.setMsg("删除新闻公告成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("management:news:news:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(News news, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "新闻公告" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<News> page = newsService.findPage(new Page<News>(request, response, -1), news);
            new ExportExcel("新闻公告", News.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出新闻公告记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("management:news:news:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<News> list = ei.getDataList(News.class);
            for (News news : list) {
                try {
                    newsService.save(news);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条新闻公告记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条新闻公告记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入新闻公告失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入新闻公告数据模板
     */
    @ResponseBody
    @RequiresPermissions("management:news:news:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "新闻公告数据导入模板.xlsx";
            List<News> list = Lists.newArrayList();
            new ExportExcel("新闻公告数据", News.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 上传图片封面
     *
     *
     * @param request
     * @param myFile
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Map<String, Object> updatePhoto(HttpServletRequest request, @RequestParam("file") MultipartFile myFile) {
        Map<String, Object> json = new HashMap<>();
        try {
            json = file(request, myFile, "/upload/news/images");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    /**
     * 上传图文
     *
     * @param request
     * @param mf
     * @return
     */
    @RequestMapping("/uploadEditorPic")

    @ResponseBody
    public Map<String, String> selectJpgUrl(@RequestParam(value = "fileName") MultipartFile mf, HttpServletRequest request)
            throws IOException {
        String path = request.getContextPath();
        String filePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        String realPath = request.getSession().getServletContext().getRealPath("/upload/news/editor");
        // 获取源文件
        String filename = mf.getOriginalFilename();
        String[] names = filename.split("\\.");//
        String tempNum = (int) (Math.random() * 100000) + "";
        String prefix=tempNum + System.currentTimeMillis();  // 文件前缀
        String suffix=names[names.length - 1];      //文件后缀
        String uploadFileName = prefix + "." + suffix;  //上传文件名称
        File targetFile = new File(realPath, uploadFileName);// 目标文件
        // 开始从源文件拷贝到目标文件
        // 传图片一步到位
        String newUrl="";
        String oldUrl="";
        try {
            mf.transferTo(targetFile);
            newUrl=realPath+"/"+prefix;       // 压缩图片文件新路径，不包含文件的后缀，如：".jpg"
            oldUrl=realPath+"/"+uploadFileName; //上传图片文件的原路径
            Thumbnails.of(oldUrl).scale(1f).outputFormat(suffix).toFile(newUrl);  //进行压缩图片
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("errno", "0");
        map.put("data", filePath+"upload/news/editor/" + uploadFileName);// 这里应该是项目路径
        map.put("url", filePath+"upload/news/editor/" + uploadFileName);
        return map;// 将图片地址返回
    }

    /**
     * 上传文件公共方法
     * @param request  请求对象
     * @param myFile   上传文件对象
     * @param savePath 自定义保存文件路径
     * @return
     */
    public Map<String, Object> file(HttpServletRequest request, MultipartFile myFile, String savePath) {
        Map<String, Object> json = new HashMap<>();
        try {
            //输出文件后缀名称
            System.out.println(myFile.getOriginalFilename());
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            //图片名称
            String name = df.format(new Date());

            Random r = new Random();
            for (int i = 0; i < 3; i++) {
                name += r.nextInt(10);
            }
            //获取后缀
            String ext = FilenameUtils.getExtension(myFile.getOriginalFilename());
            //保存图片       File位置 （全路径）   /upload/xxxx/xxxx/fileName.jpg
            String url = request.getSession().getServletContext().getRealPath(savePath);
            //相对路径
            String path = name + "." + ext;
            //System.out.println(url+"++++++++++----------------"+path);
            File file = new File(url);
            if (!file.exists()) {
                file.mkdirs();
            }
            myFile.transferTo(new File(url + "/" + path));
            json.put("success", savePath + "/" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    /**
     * 上传封面
     * @Description:保存图片并且生成缩略图
     * @param imageFile 图片文件
     * @param request 请求对象
     * @return
     */
    @RequestMapping(value = "uploadFileAndCreateThumbnail")
    @ResponseBody
    public LinkedHashMap<String, Object> uploadFileAndCreateThumbnail(@RequestParam("file") MultipartFile imageFile,HttpServletRequest request) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        if(imageFile == null ){
            map.put("success",false);
            map.put("msg","请选择文件，不能为空");
            return map;
        }
        if (imageFile.getSize() >= 10*1024*1024){
            map.put("success",false);
            map.put("msg","文件不能大于10M");
            return map;
        }
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        // 图片名称
        String uuid = df.format(new Date());
        // 文件根目录
        String fileDirectory = "images";
        // 拼接后台文件名称
        String pathName = fileDirectory + "/" + uuid + "." + FilenameUtils.getExtension(imageFile.getOriginalFilename());
        // 构建保存文件路劲
        // 2016-5-6 yangkang 修改上传路径为服务器上
        String realPath = request.getServletContext().getRealPath("/upload/news");
        //获取服务器绝对路径 linux 服务器地址  获取当前使用的配置文件配置
        //String urlString=PropertiesUtil.getInstance().getSysPro("uploadPath");
        //拼接文件路劲
        String filePathName = realPath + "/" + pathName;
        //判断文件保存是否存在
        File file = new File(filePathName);
        if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
            //创建文件
            file.getParentFile().mkdirs();
        }
        // 输入流
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = imageFile.getInputStream();
            fileOutputStream = new FileOutputStream(file);
            // 写出文件
            // 2016-05-12 yangkang 改为增加缓存
            // IOUtils.copy(inputStream, fileOutputStream);
            byte[] buffer = new byte[2048];
            IOUtils.copyLarge(inputStream, fileOutputStream, buffer);
            buffer = null;

        } catch (IOException e) {
            e.printStackTrace();
            filePathName = null;
            map.put("success",false);
            map.put("msg","上传失败"+e.getMessage());
            return map;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                filePathName = null;
                e.printStackTrace();
                map.put("success",false);
                map.put("msg","上传失败"+e.getMessage());
                return map;
            }
        }
        //String fileId = FastDFSClient.uploadFile(file, filePathName);
        /**
         * 缩略图begin
         */
        //拼接后台文件名称
        String thumbnailPathName = fileDirectory + "/" + uuid + "small."+ FilenameUtils.getExtension(imageFile.getOriginalFilename());
        //added by yangkang 2016-3-30 去掉后缀中包含的.png字符串
        if(thumbnailPathName.contains(".png")){
            thumbnailPathName = thumbnailPathName.replace(".png", ".jpg");
        }
        long size = imageFile.getSize();
        double scale = 1.0d ;
        if(size >= 200*1024){
            if(size > 0){
                scale = (200*1024f) / size  ;
            }
        }
        //拼接文件路劲
        String thumbnailFilePathName = realPath + "/" + thumbnailPathName;
        try {
            //added by chenshun 2016-3-22 注释掉之前长宽的方式，改用大小
            Thumbnails.of(filePathName).size(150, 150).toFile(thumbnailFilePathName);
            if(size < 200*1024){
                Thumbnails.of(filePathName).scale(0.2f).outputFormat("jpg").toFile(thumbnailFilePathName);
            }else{
                Thumbnails.of(filePathName).scale(0.2f).outputQuality(scale).outputFormat("jpg").toFile(thumbnailFilePathName);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            map.put("success",false);
            map.put("msg","上传失败"+e1.getMessage());
            return map;
        }
        /**
         * 缩略图end
         */
        //原图地址
        map.put("originalUrl", "/upload/news/"+pathName);
        //缩略图地址
        map.put("thumbnailUrl","/upload/news/"+thumbnailPathName);
        map.put("success",true);
        map.put("msg","上传成功");
        return map;
    }

    @RequestMapping(value = "userOrOffice")
    @ResponseBody
    public Map<String,Object> userOrOffice(@RequestParam(name = "pushrule") String pushrule){
        Map<String,Object> result =new HashMap<>();
        //判断推送规则 1:人员推送，2:部门推送，0:全部推送
        if("1".equals(pushrule)){
            User user=new User();
            user.setDelFlag("0");
            List<User> userList=userMapper.findAllList(user);
            result.put("userListInfo",userList);
        }else if("2".equals(pushrule)){
            Office office=new Office();
            office.setDelFlag("0");
            List<Office> officeList=officeService.findAllList(office);
            result.put("officeListInfo",officeList);
        }else{
            return result;
        }
        return result;
    }

    @Autowired
    private Scheduler scheduler;

    @RequestMapping("testNews")
    public void test(){
        News news= newsService.get("ffd9e79272d74d91a2518b1ef2acf320");
        if (!news.getIsNewRecord()) {
            JobKey key = new JobKey(news.getTitle(), news.getPushrule());
            try {
                scheduler.deleteJob(key);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        newsService.task(news);
    }

}