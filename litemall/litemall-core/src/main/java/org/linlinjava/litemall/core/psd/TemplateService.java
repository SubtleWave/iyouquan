package org.linlinjava.litemall.core.psd;

import com.alibaba.fastjson.JSON;
import org.linlinjava.litemall.core.storage.LocalStorage;
import org.linlinjava.litemall.core.storage.StorageService;
import org.linlinjava.litemall.db.domain.LitemallProduction;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import psd.Layer;
import psd.Psd;
import psd.parser.layer.Text;
import template.Content;
import template.Element;
import template.Global;
import template.Layout;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;


@Component
public class TemplateService {
    @Autowired
    private StorageService storageService;

    @Autowired
    private ProductionService productionService;
    @Value("${litemall.storage.local.storagePath:storage/}")
    private String pathStorage;

    //解析psd 并保存
    @Async
    public void parsePsd(String padPath, Integer userId) throws IOException {
        String pathString = pathStorage + "/" + padPath;
        LitemallProduction litemallProduction = comparePsd(pathString);
        litemallProduction.setUserId(userId);
        //保存到我的作品
        productionService.saveProduction(litemallProduction);
        //保存到 商品表 未审批
        //todo
    }

    //解析psd
    private LitemallProduction comparePsd(String psdPath) throws IOException {
        File file = new File(psdPath);
        Psd psd = new Psd(file);
        Layer baseLayer = psd.getBaseLayer();
        BufferedImage image = baseLayer.getImage();
        //ImageIO.write(image, "png", new File("F:\\work\\study\\java-psd-library\\" +"1"+ ".png"));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        LitemallStorage store = storageService.store(input, 0L, "image/png", "index.png");


        Content content = new Content();
        Global global = new Global();
        global.setEffectImage(store.getUrl());
        content.setGlobal(global);
        Layout layout = new Layout();
        //layout.setBackgroundImage(store.getUrl());
        layout.setTop(baseLayer.getY());
        layout.setWidth(baseLayer.getWidth());
        layout.setHeight(baseLayer.getHeight());
        content.setLayout(layout);

        for (Layer layer : psd.getLayers()) {
            parseLayer(layer, layout);
        }
        LitemallProduction production = new LitemallProduction();
        production.setDetail(JSON.toJSONString(content));
        production.setPicUrl(store.getUrl());
        return production;


    }

    public void parseLayer(Layer layer, Layout layout) throws IOException {
        Element element = new Element();
        if (layer.getImage() != null) {
            layout.getElements().add(element);
            BufferedImage image = layer.getImage();
//            String filePath = "F:\\work\\study\\java-psd-library\\" +atomicInteger.incrementAndGet()+ ".png";
//            ImageIO.write(image, "png", new File(filePath));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            LitemallStorage store = storageService.store(input, 0L, "image/png", "xxx.png");
            element.setType("image");
            element.setWidth(layer.getWidth());
            element.setTop(layer.getY());
            element.setHeight(layer.getHeight());
            element.setLeft(layer.getX());
            element.setUrl(store.getUrl());
        }
        Text layerText = layer.getText();
        if (layerText != null) {
            element.setType("text");
            element.setFontFamily(layerText.getFontFamily());
            element.setContent(layerText.getContent());
            element.setFontSize(layerText.getFontSize());
            element.setColor(layerText.getColor());
            element.setWritingMode(layerText.getWritingDirection());
        }
        List<Layer> layers = layer.getLayers();
        for (Layer layer1 : layers) {
            parseLayer(layer1, layout);
        }
    }


}
