package org.linlinjava.litemall.db.util;

import net.sf.jsqlparser.expression.StringValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.linlinjava.litemall.db.dao.LitemallCategoryMapper;
import org.linlinjava.litemall.db.dao.LitemallGoodsMapper;
import org.linlinjava.litemall.db.domain.LitemallCategory;
import org.linlinjava.litemall.db.domain.LitemallCategoryExample;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallGoodsExample;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * 数据库数据批量导入Solr索引库
 * by zhangran in 20190917
 */
@Component
public class SolrUtil {
    private final Log logger = LogFactory.getLog(SolrUtil.class);

    @Autowired
    private LitemallGoodsMapper goodsMapper;
    @Autowired
    private LitemallCategoryMapper categoryMapper;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private SolrClient solrClient;

    LitemallGoods.Column[] goodsColumns = new LitemallGoods.Column[]{
            LitemallGoods.Column.id, LitemallGoods.Column.goodsSn,
            LitemallGoods.Column.name, LitemallGoods.Column.categoryId,
            LitemallGoods.Column.brandId, LitemallGoods.Column.gallery,
            LitemallGoods.Column.keywords, LitemallGoods.Column.brief,
            LitemallGoods.Column.isOnSale, LitemallGoods.Column.sortOrder,
            LitemallGoods.Column.picUrl, LitemallGoods.Column.shareUrl,
            LitemallGoods.Column.isNew, LitemallGoods.Column.isHot,
            LitemallGoods.Column.unit, LitemallGoods.Column.counterPrice,
            LitemallGoods.Column.retailPrice, LitemallGoods.Column.addTime,
            LitemallGoods.Column.updateTime, LitemallGoods.Column.deleted,
            LitemallGoods.Column.detail};

    LitemallCategory.Column[] categoryColumns = new LitemallCategory.Column[]{
            LitemallCategory.Column.id, LitemallCategory.Column.name,
            LitemallCategory.Column.keywords, LitemallCategory.Column.desc,
            LitemallCategory.Column.pid, LitemallCategory.Column.iconUrl,
            LitemallCategory.Column.picUrl, LitemallCategory.Column.level,
            LitemallCategory.Column.sortOrder,  LitemallCategory.Column.addTime,
            LitemallCategory.Column.updateTime, LitemallCategory.Column.deleted};

    /**
     * 实现将数据库中的数据批量导入到Solr索引库中, 商品表
     */
    public void importGoodsDataBySolrJ() {
        List<LitemallGoods> list = goodsMapper.selectByExampleSelective(new LitemallGoodsExample(), goodsColumns);
        //创建索引文档对象集合
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();

        logger.info("====商品列表====");
        for (LitemallGoods goods : list) {
            //创建索引文档对象
            SolrInputDocument doc = new SolrInputDocument();

            doc.addField("id",String.valueOf(goods.getId()));
            doc.addField("goodsSn",goods.getGoodsSn());
            doc.addField("name",goods.getName());
            doc.addField("categoryId",goods.getCategoryId());
            doc.addField("brandId",goods.getBrandId());
            doc.addField("gallery",goods.getGallery());
            doc.addField("keywords",goods.getKeywords());
            doc.addField("brief",goods.getBrief());
            doc.addField("isOnSale",goods.getIsOnSale());
            doc.addField("sortOrder",goods.getSortOrder());
            doc.addField("picUrl",goods.getPicUrl());
            doc.addField("shareUrl",goods.getShareUrl());
            doc.addField("isNew",goods.getIsNew());
            doc.addField("isHot",goods.getIsHot());
            doc.addField("unit",goods.getUnit());
            doc.addField("counterPrice",goods.getCounterPrice().doubleValue());
            doc.addField("retailPrice",goods.getRetailPrice().doubleValue());

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            doc.addField("addTime",dtf.format(goods.getAddTime()));
            doc.addField("updateTime", dtf.format(goods.getUpdateTime()));

            doc.addField("deleted",goods.getDeleted());
            //doc.addField("detail",goods.getDetail());

            docs.add(doc);

            logger.info(goods.getName());
        }

        try{
            solrClient.add("litemall_goods",docs);
            solrClient.commit("litemall_goods");
        } catch (Exception e){
            logger.error(e.getMessage());
            logger.error(e.getCause());
        }


        logger.info("====结束====");
    }

    /**
     * 实现将数据库中的数据批量导入到Solr索引库中, 目录表
     */
    public void importCategoryDataBySolrJ() {
        List<LitemallCategory> list = categoryMapper.selectByExampleSelective(new LitemallCategoryExample(), categoryColumns);

        //创建索引文档对象集合
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();

        logger.info("====目录列表====");
        for (LitemallCategory category : list) {
            //创建索引文档对象
            SolrInputDocument doc = new SolrInputDocument();

            doc.addField("id",String.valueOf(category.getId()));
            doc.addField("name",category.getName());
            doc.addField("keywords",category.getKeywords());
            doc.addField("desc",category.getDesc());
            doc.addField("pid",category.getPid());
            doc.addField("iconUrl",category.getIconUrl());
            doc.addField("picUrl",category.getPicUrl());
            doc.addField("level",category.getLevel());
            doc.addField("sortOrder",category.getSortOrder());

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            doc.addField("addTime",dtf.format(category.getAddTime()));
            doc.addField("updateTime", dtf.format(category.getUpdateTime()));

            doc.addField("deleted",category.getDeleted());

            docs.add(doc);

            logger.info(category.getName());
        }

        try{
            solrClient.add("litemall_category",docs);
            solrClient.commit("litemall_category");
        } catch (Exception e){
            logger.error(e.getMessage());
            logger.error(e.getCause());
        }

        logger.info("====结束====");
    }

    public String parseforSolrIn(List<Integer> list){
        if(list == null || list.isEmpty()){
            return "";
        }

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        return list.toString()
                .replaceAll("\\[","(")
                .replaceAll("\\]",")");
    }
}
