package org.linlinjava.litemall.wx.web;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.context.AppContext;
import org.linlinjava.litemall.core.psd.TemplateService;
import org.linlinjava.litemall.core.storage.StorageService;
import org.linlinjava.litemall.core.util.CharUtil;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.service.LitemallStorageService;
import org.linlinjava.litemall.db.service.LitemallWXService;
import org.linlinjava.litemall.db.service.TraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 对象存储服务
 */
//todo 上传功能依靠前端通过流给后端 由后端上传到文件服务器上，和在蚂蚁的流程不一致，是否需要修改
@RestController
@RequestMapping("/wx/storage")
@Validated
public class WxStorageController {

    public static List<String> bglist = Lists.newArrayList(
           "#7dadd0",
                "#63bcfa",
                "#e0dacb",
                "#fbdfbc",
                "#bdcee5",
                "#dbac84",
                "#7aa7cf",
                "#2f9553",
                "#8fb357",
                "#eec6a4",
                "#6a8f38",
                "#fdc52d",
                "#af7a3a",
                "#6097df",
                "#fae866",
                "#b2b79a",
                "#dfe0eb",
                "#685845",
                "#b39e43",
                "#939787",
                "#2f2929",
                "#b5271a",
                "#e5cb34",
                "#f4dca6",
                "#f2bea7",
                "#030606",
                "#27224a",
                "#c9cde2",
                "#3f5ca6",
                "#dac763",
                "#6a90b4");
    private final Log logger = LogFactory.getLog(WxStorageController.class);


    public static String getBg(){
        int size = bglist.size();
        Random r = new Random();
        int i = r.nextInt(size);
        return bglist.get(i);
    }
    @Autowired
    private StorageService storageService;
    @Autowired
    private LitemallStorageService litemallStorageService;
    @Autowired
    private LitemallWXService litemallWXService;

    @Autowired
    private TraceService traceService;

    @Autowired
    private TemplateService templateService;


    private String generateKey(String originalFilename) {
        int index = originalFilename.lastIndexOf('.');
        String suffix = originalFilename.substring(index);

        String key = null;
        LitemallStorage storageInfo = null;

        do {
            key = System.currentTimeMillis() + CharUtil.getRandomString(20) + suffix;
            storageInfo = litemallStorageService.findByKey(key);
        }
        while (storageInfo != null);

        return key;
    }

    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws IOException {
        Resource resource = file.getResource();
        InputStream inputStream = resource.getInputStream();
        BufferedImage sourceImg = ImageIO.read(inputStream);
        //System.out.println(sourceImg.getWidth());   // 源图宽度
        //System.out.println(sourceImg.getHeight());   // 源图高度

        String originalFilename = file.getOriginalFilename();
        LitemallStorage litemallStorage = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(), originalFilename);
        //图片上传到微信服务器检测安全信息
        String imgcheckId = litemallWXService.uploadWXCheck(litemallStorage.getUrl());
        if (imgcheckId!=null){
            //保存微信检测第三方表 便于异步校验结果
            traceService.saveTrace(imgcheckId,litemallStorage.getId());
        }
        litemallStorage.setUrl(litemallStorage.getUrl()+"?width="+sourceImg.getWidth()+"&height="+sourceImg.getHeight()+"&color="+getBg());

        return ResponseUtil.ok(litemallStorage);
    }

    @PostMapping("/uploadpsd")
    public Object uploadpsd(@RequestParam("file") MultipartFile file) throws IOException {
        if (AppContext.getUserId() == null) {
            return ResponseUtil.unlogin();
        }
        Resource resource = file.getResource();


        String originalFilename = file.getOriginalFilename();
        LitemallStorage litemallStorage = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(), originalFilename);
        //litemallStorage.setUrl(litemallStorage.getUrl()+"?width="+sourceImg.getWidth()+"&height="+sourceImg.getHeight()+"&color="+getBg());
        templateService.parsePsd(litemallStorage.getKey(),AppContext.getUserId());


        return ResponseUtil.ok();
    }

    /**
     * 访问存储对象
     *
     * @param key 存储对象key
     * @return
     */
    @GetMapping("/fetch/{key:.+}")
    public ResponseEntity<Resource> fetch(@PathVariable String key) {
        LitemallStorage litemallStorage = litemallStorageService.findByKey(key);
        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        if (key.contains("../")) {
            return ResponseEntity.badRequest().build();
        }
        String type = litemallStorage.getType();
        MediaType mediaType = MediaType.parseMediaType(type);

        Resource file = storageService.loadAsResource(key);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(mediaType).body(file);
    }

    /**
     * 访问存储对象
     *
     * @param key 存储对象key
     * @return
     */
    @GetMapping("/download/{key:.+}")
    public ResponseEntity<Resource> download(@PathVariable String key) {
        LitemallStorage litemallStorage = litemallStorageService.findByKey(key);
        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        if (key.contains("../")) {
            return ResponseEntity.badRequest().build();
        }
        String type = litemallStorage.getType();
        MediaType mediaType = MediaType.parseMediaType(type);

        Resource file = storageService.loadAsResource(key);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
