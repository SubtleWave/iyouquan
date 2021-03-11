package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.context.AppContext;
import org.linlinjava.litemall.core.storage.StorageService;
import org.linlinjava.litemall.core.util.CharUtil;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallAddress;
import org.linlinjava.litemall.db.domain.LitemallProduction;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.service.LitemallStorageService;
import org.linlinjava.litemall.db.service.ProductionService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 作品功能模块
 */
@RestController
@RequestMapping("/wx/production")
@Validated
public class WxProductionController {
    private final Log logger = LogFactory.getLog(WxProductionController.class);

    @Autowired
    private ProductionService productionService;

    /**
     * 用户作品列表
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return 用户作品列表
     */
    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        if (AppContext.getUserId() == null) {
            return ResponseUtil.unlogin();
        }
        List<LitemallProduction> litemallProductions = productionService.queryProductByUserId(AppContext.getUserId(),page,limit,sort,order);
        return ResponseUtil.okList(litemallProductions);
    }


    /**
     * 获取单个 作品
     * @param productId
     * @return
     */
    @GetMapping("/getProduct/{productId}")
    public Object getProduct(@PathVariable("productId") Integer productId) {
        /*if (AppContext.getUserId() == null) {
            return ResponseUtil.unlogin();
        }*/
        LitemallProduction production = productionService.getProductionByUserId(productId, AppContext.getUserId());
        return ResponseUtil.ok(production);
    }

    /**
     * 保存作品
     * @param litemallProduction
     * @return
     */
    @PostMapping("/save")
    public Object save(@RequestBody LitemallProduction litemallProduction){
        if (AppContext.getUserId() == null) {
            return ResponseUtil.unlogin();
        }
        litemallProduction.setUserId(AppContext.getUserId());
        Integer id = productionService.saveProduction(litemallProduction);
        LitemallProduction litemallProduction1 = new LitemallProduction();
        litemallProduction1.setId(id);
        return ResponseUtil.ok(litemallProduction1);
    }

    /**
     * 删除 作品
     * @param productId
     * @return
     */
    @GetMapping("/delProduct/{productId}")
    public Object delProduct(@PathVariable("productId") Integer productId) {
        if (AppContext.getUserId() == null) {
            return ResponseUtil.unlogin();
        }
        productionService.deleteProduction(productId, AppContext.getUserId());
        return ResponseUtil.ok();
    }



}
