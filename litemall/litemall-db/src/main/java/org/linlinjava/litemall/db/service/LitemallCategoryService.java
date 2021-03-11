package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.linlinjava.litemall.db.dao.LitemallCategoryMapper;
import org.linlinjava.litemall.db.domain.LitemallCategory;
import org.linlinjava.litemall.db.domain.LitemallCategoryExample;
import org.linlinjava.litemall.db.util.SolrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class LitemallCategoryService {
    private final Log logger = LogFactory.getLog(LitemallCategoryService.class);

    @Resource
    private LitemallCategoryMapper categoryMapper;
    @Resource
    private LitemallSystemConfigService systemConfigService;
    private LitemallCategory.Column[] CHANNEL = {LitemallCategory.Column.id, LitemallCategory.Column.name, LitemallCategory.Column.iconUrl};

    @Autowired
    private SolrClient solrClient;

    @Autowired
    private SolrUtil solrUtil;

    public List<LitemallCategory> queryL1WithoutRecommend(int offset, int limit) {
        LitemallCategoryExample example = new LitemallCategoryExample();
        example.or().andLevelEqualTo("L1").andNameNotEqualTo("推荐").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    public List<LitemallCategory> queryL1(int offset, int limit) {
        LitemallCategoryExample example = new LitemallCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    public List<LitemallCategory> queryL1() {
        LitemallCategoryExample example = new LitemallCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public List<LitemallCategory> queryByPid(Integer pid) {
        LitemallCategoryExample example = new LitemallCategoryExample();
        example.or().andPidEqualTo(pid).andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public List<LitemallCategory> queryL2ByIds(List<Integer> ids) {
        LitemallCategoryExample example = new LitemallCategoryExample();
        example.or().andIdIn(ids).andLevelEqualTo("L2").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }
    // search category list by id list from solr, zhangran 20191004
    public List<LitemallCategory> queryL2ByIdsBySolr(List<Integer> ids) throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        //添加查询条件
        // TODO: 2019/10/04 查询条件及排序还需根据实际需求进行增加
        StringBuilder params = new StringBuilder("level:L2 AND id:");
        params.append(solrUtil.parseforSolrIn(ids));

        logger.info(params.toString());

        query.setQuery(params.toString());
        QueryResponse queryResponse = null;
        List<LitemallCategory> categories = null;
        try {
            queryResponse = solrClient.query("litemall_category", query);

            //将查询结果中id从字符串转为所需的整型
            for (SolrDocument doc : queryResponse.getResults()) {
                //将查询结果中id从字符串转为所需的整型
                doc.setField("id", Integer.parseInt((String) doc.get("id")));
                //将查询结果中sortOrder从Integer转为所需的Byte
                Integer sortOrder = (Integer) doc.get("sortOrder");
                doc.setField("sortOrder",sortOrder.byteValue());
                //将查询结果中addTime从String转为所需的日期型
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String addTime= (String) doc.get("addTime");
                doc.setField("addTime", LocalDateTime.parse(addTime, dtf));
                //将查询结果中updateTime从String转为所需的日期型
                String updateTime= (String) doc.get("updateTime");
                doc.setField("updateTime", LocalDateTime.parse(updateTime, dtf));
            }

            categories = queryResponse.getBeans(LitemallCategory.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getCause());
            return null;
        }

        logger.info("商品目录列表为：" + categories);

        return categories;
    }

    public LitemallCategory findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    public List<LitemallCategory> querySelective(String id, String name, Integer page, Integer size, String sort, String order) {
        LitemallCategoryExample example = new LitemallCategoryExample();
        LitemallCategoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(id)) {
            criteria.andIdEqualTo(Integer.valueOf(id));
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return categoryMapper.selectByExample(example);
    }

    public int updateById(LitemallCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        return categoryMapper.updateByPrimaryKeySelective(category);
    }
    //add by zhangran in 20191217
    public int updateByIdInSolr(LitemallCategory category) throws IOException, SolrServerException {
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

        int solrResStatus = -1;
        try {
            solrClient.add("litemall_category",doc);
            solrResStatus = solrClient.commit("litemall_category").getStatus();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getCause());
        }

        return solrResStatus;
    }

    public void deleteById(Integer id) {
        categoryMapper.logicalDeleteByPrimaryKey(id);
    }

    //add by zhangran in 20191217
    public void deleteByIdInSolr(Integer id) {
        try {
            solrClient.deleteById(String.valueOf(id));
            solrClient.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getCause());
        }
    }

    public void add(LitemallCategory category) {
        category.setAddTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.insertSelective(category);
    }
    //add by zhangran in 20191217 for solr adding
    public void addIntoSolr(LitemallCategory category) {
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

        try {
            solrClient.add("litemall_category",doc);
            solrClient.commit("litemall_category");
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getCause());
        }
    }

    public List<LitemallCategory> queryChannel() {
        LitemallCategoryExample example = new LitemallCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExampleSelective(example, CHANNEL);
    }



    public List<LitemallCategory> queryHomeCategory() {
        String choiceness = systemConfigService.getValue("litemall_home_category");
        String[] split = choiceness.split(",");
        Integer[] aftIdArray = (Integer[]) ConvertUtils.convert(split, Integer.class);
        //查询列表数据
        LitemallCategoryExample example = new LitemallCategoryExample();
        example.or().andIdIn(Arrays.asList(aftIdArray)).andDeletedEqualTo(false);
        return categoryMapper.selectByExampleSelective(example);
    }
}
