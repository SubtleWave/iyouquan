package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.linlinjava.litemall.db.dao.LitemallGoodsMapper;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallGoods.Column;
import org.linlinjava.litemall.db.domain.LitemallGoodsExample;
import org.linlinjava.litemall.db.domain.SolrPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LitemallGoodsService{
    private final Log logger = LogFactory.getLog(LitemallGoodsService.class);

    Column[] columns = new Column[]{Column.id, Column.name, Column.brief, Column.picUrl, Column.isHot, Column.isNew, Column.counterPrice, Column.retailPrice};
    @Resource
    private LitemallGoodsMapper goodsMapper;

    @Autowired
    private SolrClient solrClient;

    /**
     * 获取热卖商品
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<LitemallGoods> queryByHot(int offset, int limit) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andIsHotEqualTo(true).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        example.setOrderByClause("add_time desc");
        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }

    public List<LitemallGoods> queryByHot() {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andDeletedEqualTo(false);
        example.setOrderByClause("add_time desc");

        return goodsMapper.selectByExampleSelective(example, columns);
    }

    /**
     * 获取新品上市
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<LitemallGoods> queryByNew(int offset, int limit) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andIsNewEqualTo(true).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        example.setOrderByClause("add_time desc");
        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }

    /**
     * 获取分类下的商品
     *
     * @param catList
     * @param offset
     * @param limit
     * @return
     */
    public List<LitemallGoods> queryByCategory(List<Integer> catList, int offset, int limit) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andCategoryIdIn(catList).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        example.setOrderByClause("add_time  desc");
        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }


    /**
     * 获取分类下的商品
     *
     * @param catId
     * @param offset
     * @param limit
     * @return
     */
    public List<LitemallGoods> queryByCategory(Integer catId, int offset, int limit) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andCategoryIdEqualTo(catId).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        example.setOrderByClause("add_time desc");
        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }


    public List<LitemallGoods> querySelective(Integer catId, Integer brandId, String keywords, Boolean isHot, Boolean isNew, Integer offset, Integer limit, String sort, String order) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        LitemallGoodsExample.Criteria criteria1 = example.or();
        LitemallGoodsExample.Criteria criteria2 = example.or();

        if (!StringUtils.isEmpty(catId) && catId != 0) {
            criteria1.andCategoryIdEqualTo(catId);
            criteria2.andCategoryIdEqualTo(catId);
        }
        if (!StringUtils.isEmpty(brandId)) {
            criteria1.andBrandIdEqualTo(brandId);
            criteria2.andBrandIdEqualTo(brandId);
        }
        if (!StringUtils.isEmpty(isNew)) {
            criteria1.andIsNewEqualTo(isNew);
            criteria2.andIsNewEqualTo(isNew);
        }
        if (!StringUtils.isEmpty(isHot)) {
            criteria1.andIsHotEqualTo(isHot);
            criteria2.andIsHotEqualTo(isHot);
        }
        if (!StringUtils.isEmpty(keywords)) {
            criteria1.andKeywordsLike("%" + keywords + "%");
            criteria2.andNameLike("%" + keywords + "%");
        }
        criteria1.andIsOnSaleEqualTo(true);
        criteria2.andIsOnSaleEqualTo(true);
        criteria1.andDeletedEqualTo(false);
        criteria2.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }

    public List<LitemallGoods> querySelective(String goodsSn, String name, Integer page, Integer size, String sort, String order) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        LitemallGoodsExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(goodsSn)) {
            criteria.andGoodsSnEqualTo(goodsSn);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return goodsMapper.selectByExampleWithBLOBs(example);
    }

    //用于搜索商品by solr，add by zhangran in 20190927
    public SolrPage querySelectiveBySolr(Integer catId, Integer brandId, String keywords, Boolean isHot, Boolean isNew, Integer offset, Integer limit, String sort, String order) throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        SolrPage solrPage = new SolrPage();
        //添加查询条件
        // TODO: 2019/9/27 查询条件及排序还需根据实际需求进行增加
        //修改搜索逻辑
        StringBuilder params = new StringBuilder("(deleted:false AND isOnSale:true)");
        if (!StringUtils.isEmpty(keywords)) {
            params.append(" AND (keywords:" + keywords);
            params.append(" OR name:" + keywords + ")");
        }
        //判断是否有分类筛选
        if (!StringUtils.isEmpty(catId) && catId != 0) {
            params.append(" AND categoryId:" + catId);
        }

        logger.info("solr 搜索参数：{}"+ params.toString());

        query.setQuery(params.toString());
        query.addSort("sortOrder", SolrQuery.ORDER.desc);
        //开始页
        query.setStart((offset-1)*offset);
        solrPage.setCurrent(offset);
        //一页显示多少条
        query.setRows(limit);
        solrPage.setSize(limit);

        QueryResponse queryResponse = null;
        List<LitemallGoods> products = null;
        try{
            queryResponse = solrClient.query("litemall_goods",query);

            SolrDocumentList docs = queryResponse.getResults();
            for (SolrDocument doc : docs) {
                //将查询结果中id从字符串转为所需的整型
                doc.setField("id",Integer.parseInt((String) doc.get("id")));
                //将查询结果中sortOrder从Integer转为所需的Short
                Integer sortOrder = (Integer) doc.get("sortOrder");
                doc.setField("sortOrder",sortOrder.shortValue());
                //将查询结果中counterPrice从Double转为所需的BigDecimal
                doc.setField("counterPrice", BigDecimal.valueOf((Double) doc.get("counterPrice")));
                //将查询结果中retailPrice从Double转为所需的BigDecimal
                doc.setField("retailPrice", BigDecimal.valueOf((Double) doc.get("retailPrice")));
                //将查询结果中addTime从String转为所需的日期型
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String addTime= (String) doc.get("addTime");
                doc.setField("addTime", LocalDateTime.parse(addTime, dtf));
                //将查询结果中updateTime从String转为所需的日期型
                String updateTime= (String) doc.get("updateTime");
                doc.setField("updateTime", LocalDateTime.parse(updateTime, dtf));
                //将查询结果中detail为text_general即ArrayList,目前暂时不需要所以清空
                doc.setField("detail", "");
                // 将查询结果中的所需要的原布尔型转回去（因为在solr中的布尔我们做的是string的体现）
                doc.setField("deleted", Boolean.valueOf((String) doc.get("deleted")));
                doc.setField("isOnSale", Boolean.valueOf((String) doc.get("isOnSale")));
                doc.setField("isNew", Boolean.valueOf((String) doc.get("isNew")));
                doc.setField("isHot", Boolean.valueOf((String) doc.get("isHot")));
            }

            products = queryResponse.getBeans(LitemallGoods.class);

            solrPage.setDatas(products);
            solrPage.setCount(docs.getNumFound());
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getCause());
            return null;
        }
        //logger.info("商品列表为：" + products);

        return solrPage;
    }


    /**
     * 获取某个商品信息,包含完整信息
     *
     * @param id
     * @return
     */
    public LitemallGoods findById(Integer id) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return goodsMapper.selectOneByExampleWithBLOBs(example);
    }

    /**
     * 获取某个商品信息，仅展示相关内容
     *
     * @param id
     * @return
     */
    public LitemallGoods findByIdVO(Integer id) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andIdEqualTo(id).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return goodsMapper.selectOneByExampleSelective(example, columns);
    }


    /**
     * 获取所有在售物品总数
     *
     * @return
     */
    public Integer queryOnSale() {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return (int) goodsMapper.countByExample(example);
    }

    public int updateById(LitemallGoods goods) {
        goods.setUpdateTime(LocalDateTime.now());
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }

    //add by zhangran for updating goods when DB updated by solr, 20191019
    public int updateByIdInSolr(LitemallGoods goods) throws IOException, SolrServerException {

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
        doc.addField("detail",goods.getDetail());

        int solrResStatus = -1;
        try{
            solrClient.add("litemall_goods",doc);
            solrResStatus = solrClient.commit("litemall_goods").getStatus();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getCause());
        }

        return solrResStatus;
    }

    public void deleteById(Integer id) {
        goodsMapper.logicalDeleteByPrimaryKey(id);
    }

    //add by zhangran in 20191022,在Solr中按照id删除商品
    public void deleteByIdInSolr(Integer id) throws IOException, SolrServerException {
        try {
            solrClient.deleteById(String.valueOf(id));
            solrClient.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getCause());
        }
    }

    public void add(LitemallGoods goods) {
        goods.setAddTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());
        goodsMapper.insertSelective(goods);
    }

    //add by zhangran for adding goods when DB inserted by solr, 20191019
    public void addIntoSolr(LitemallGoods goods) throws IOException, SolrServerException {

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
        doc.addField("detail",goods.getDetail());

        try {
            solrClient.add("litemall_goods",doc);
            solrClient.commit("litemall_goods");
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getCause());
        }
    }

    /**
     * 获取所有物品总数，包括在售的和下架的，但是不包括已删除的商品
     *
     * @return
     */
    public int count() {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andDeletedEqualTo(false);
        return (int) goodsMapper.countByExample(example);
    }

    public List<Integer> getCatIds(Integer brandId, String keywords, Boolean isHot, Boolean isNew) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        LitemallGoodsExample.Criteria criteria1 = example.or();
        LitemallGoodsExample.Criteria criteria2 = example.or();

        if (!StringUtils.isEmpty(brandId)) {
            criteria1.andBrandIdEqualTo(brandId);
            criteria2.andBrandIdEqualTo(brandId);
        }
        if (!StringUtils.isEmpty(isNew)) {
            criteria1.andIsNewEqualTo(isNew);
            criteria2.andIsNewEqualTo(isNew);
        }
        if (!StringUtils.isEmpty(isHot)) {
            criteria1.andIsHotEqualTo(isHot);
            criteria2.andIsHotEqualTo(isHot);
        }
        if (!StringUtils.isEmpty(keywords)) {
            criteria1.andKeywordsLike("%" + keywords + "%");
            criteria2.andNameLike("%" + keywords + "%");
        }
        criteria1.andIsOnSaleEqualTo(true);
        criteria2.andIsOnSaleEqualTo(true);
        criteria1.andDeletedEqualTo(false);
        criteria2.andDeletedEqualTo(false);

        List<LitemallGoods> goodsList = goodsMapper.selectByExampleSelective(example, Column.categoryId);
        List<Integer> cats = new ArrayList<Integer>();
        for (LitemallGoods goods : goodsList) {
            cats.add(goods.getCategoryId());
        }
        return cats;
    }

    //用于查询商品类目列表by solr，add by zhangran in 20190929
    public List<Integer> getCatIdsFromGoodList(List<LitemallGoods> goodsList) {
        if (goodsList == null || goodsList.isEmpty()){
            return new ArrayList<>(0);
        }

        List<Integer> cats = new ArrayList<Integer>();
        for (LitemallGoods goods : goodsList) {
            cats.add(goods.getCategoryId());
        }

        logger.info(cats);

        return cats;
    }

    public boolean checkExistByName(String name) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andNameEqualTo(name).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return goodsMapper.countByExample(example) != 0;
    }

    public List<LitemallGoods> queryByIds(Integer[] ids) {
        LitemallGoodsExample example = new LitemallGoodsExample();
        example.or().andIdIn(Arrays.asList(ids)).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return goodsMapper.selectByExampleSelective(example, columns);
    }

    public void updatePicurl(Integer id, String picUrl) {
        LitemallGoods litemallGoods = new LitemallGoods();
        litemallGoods.setPicUrl(picUrl);
        litemallGoods.setId(id);
        goodsMapper.updateByPrimaryKeySelective(litemallGoods);
    }
}
