package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.dao.LitemallTypefaceConfigMapper;
import org.linlinjava.litemall.db.domain.LitemallTypefaceConfig;
import org.linlinjava.litemall.db.domain.LitemallTypefaceConfig.Column;
import org.linlinjava.litemall.db.domain.LitemallTypefaceConfigExample;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class LitemallResourceService {
    private final Log logger = LogFactory.getLog(LitemallResourceService.class);

    Column[] columns = new Column[]{Column.id, Column.name, Column.brief,Column.typefacePath};
    @Resource
    private LitemallTypefaceConfigMapper typefaceConfigMapper;




    public List<LitemallTypefaceConfig> querySelective(String type, Integer offset, Integer limit, String sort, String order) {
        LitemallTypefaceConfigExample example = new LitemallTypefaceConfigExample();
        LitemallTypefaceConfigExample.Criteria criteria1 = example.or();
        criteria1.andTypeEqualTo(type);

        criteria1.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(offset, limit);

        return typefaceConfigMapper.selectByExampleSelective(example,columns);
    }


    /**
     * 获取某个商品信息,包含完整信息
     *
     * @param id
     * @return
     */
    public LitemallTypefaceConfig findById(Integer id) {
        LitemallTypefaceConfigExample example = new LitemallTypefaceConfigExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return typefaceConfigMapper.selectOneByExampleWithBLOBs(example);
    }



    /**
     * 获取所有在售物品总数
     *
     * @return
     */
    public Integer queryOnSale() {
        LitemallTypefaceConfigExample example = new LitemallTypefaceConfigExample();
        example.or().andDeletedEqualTo(false);
        return (int) typefaceConfigMapper.countByExample(example);
    }

    public int updateById(LitemallTypefaceConfig typefaceConfig) {
        typefaceConfig.setUpdateTime(LocalDateTime.now());
        return typefaceConfigMapper.updateByPrimaryKeySelective(typefaceConfig);
    }

    public void deleteById(Integer id) {
        typefaceConfigMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LitemallTypefaceConfig typefaceConfig) {
        typefaceConfig.setAddTime(LocalDateTime.now());
        typefaceConfig.setUpdateTime(LocalDateTime.now());
        typefaceConfigMapper.insertSelective(typefaceConfig);
    }

    /**
     * 获取所有物品总数，包括在售的和下架的，但是不包括已删除的商品
     *
     * @return
     */
    public int count() {
        LitemallTypefaceConfigExample example = new LitemallTypefaceConfigExample();
        example.or().andDeletedEqualTo(false);
        return (int) typefaceConfigMapper.countByExample(example);
    }

    public boolean checkExistByName(String name) {
        LitemallTypefaceConfigExample example = new LitemallTypefaceConfigExample();
        example.or().andNameEqualTo(name).andDeletedEqualTo(false);
        return typefaceConfigMapper.countByExample(example) != 0;
    }

    public List<LitemallTypefaceConfig> queryByIds(Integer[] ids) {
        LitemallTypefaceConfigExample example = new LitemallTypefaceConfigExample();
        example.or().andIdIn(Arrays.asList(ids)).andDeletedEqualTo(false);
        return typefaceConfigMapper.selectByExampleSelective(example, columns);
    }

    public LitemallTypefaceConfig getFontByName(String name) {
        LitemallTypefaceConfigExample example = new LitemallTypefaceConfigExample();
        example.or().andNameEqualTo(name).andDeletedEqualTo(false).andTypeEqualTo("font");
        List<LitemallTypefaceConfig> typefaceConfigs = typefaceConfigMapper.selectByExampleWithBLOBs(example);
        if (CollectionUtils.isEmpty(typefaceConfigs)){
            return null;
        }
        return typefaceConfigs.get(0);
    }
}
