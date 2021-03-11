package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import org.linlinjava.litemall.db.dao.LitemallProductionMapper;
import org.linlinjava.litemall.db.domain.LitemallProduction;
import org.linlinjava.litemall.db.domain.LitemallProductionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProductionService {
    @Autowired
    private LitemallProductionMapper litemallProductionMapper;

    @Autowired
    private LitemallAddressService litemallAddressService;


    /**
     * 查询 用户 作品列表
     *
     * @param userId
     * @return
     */
    public List<LitemallProduction> queryProductByUserId(Integer userId,int offset, int limit, String sort, String order) {
        LitemallProductionExample example = new LitemallProductionExample();
        example.createCriteria().andUserIdEqualTo(userId).andDeletedEqualTo(LitemallProduction.NOT_DELETED);
        example.setOrderByClause(sort + " " + order);
        PageHelper.startPage(offset, limit);
        return litemallProductionMapper.selectByExample(example);
    }

    /**
     * 获取单个作品
     * @param productId
     * @param userId
     * @return
     */
    public LitemallProduction getProductionByUserId(Integer productId,Integer userId){
        LitemallProductionExample example = new LitemallProductionExample();
        LitemallProductionExample.Criteria criteria = example.createCriteria();

        criteria.andIdEqualTo(productId);
        if (userId != null){
            criteria.andUserIdEqualTo(userId);
        }
        criteria.andDeletedEqualTo(LitemallProduction.NOT_DELETED);
        return litemallProductionMapper.selectOneByExampleWithBLOBs(example);
    }

    /**
     * 保存用户作品 包含 插入 更新
     *
     * @param litemallProduction
     */
   // @Transactional(propagation = Propagation.REQUIRED)
    public Integer saveProduction(LitemallProduction litemallProduction) {
        if (null == litemallProduction.getId()) {
            //插入
            litemallProduction.setAddTime(LocalDateTime.now());
            litemallProduction.setDeleted(LitemallProduction.NOT_DELETED);
            litemallProduction.setUserId(litemallProduction.getUserId());
            litemallProduction.setUpdateTime(LocalDateTime.now());
            litemallProductionMapper.insertSelective(litemallProduction);
        }else {
            //更新
            litemallProduction.setUpdateTime(LocalDateTime.now());
            litemallProduction.setUserId(litemallProduction.getUserId());

            litemallProductionMapper.updateByPrimaryKeySelective(litemallProduction);
        }
        return litemallProduction.getId();

    }

    /**
     * 删除作品
     *
     * @param productionId
     * @param userId
     */
    public void deleteProduction(Integer productionId, Integer userId) {
        LitemallProduction litemallProduction = new LitemallProduction();
        litemallProduction.setUpdateTime(LocalDateTime.now());
        litemallProduction.setDeleted(Boolean.TRUE);
        LitemallProductionExample example = new LitemallProductionExample();
        example.createCriteria().andIdEqualTo(productionId).andUserIdEqualTo(userId).andDeletedEqualTo(LitemallProduction.NOT_DELETED);
        litemallProductionMapper.updateByExampleSelective(litemallProduction, example);
    }


}
