package org.linlinjava.litemall.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallProduction;
import org.linlinjava.litemall.db.domain.LitemallProductionExample;

public interface LitemallProductionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    long countByExample(LitemallProductionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int deleteByExample(LitemallProductionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int insert(LitemallProduction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int insertSelective(LitemallProduction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    LitemallProduction selectOneByExample(LitemallProductionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    LitemallProduction selectOneByExampleSelective(@Param("example") LitemallProductionExample example, @Param("selective") LitemallProduction.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    LitemallProduction selectOneByExampleWithBLOBs(LitemallProductionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    List<LitemallProduction> selectByExampleSelective(@Param("example") LitemallProductionExample example, @Param("selective") LitemallProduction.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    List<LitemallProduction> selectByExampleWithBLOBs(LitemallProductionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    List<LitemallProduction> selectByExample(LitemallProductionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    LitemallProduction selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") LitemallProduction.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    LitemallProduction selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    LitemallProduction selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") LitemallProduction record, @Param("example") LitemallProductionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int updateByExampleWithBLOBs(@Param("record") LitemallProduction record, @Param("example") LitemallProductionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") LitemallProduction record, @Param("example") LitemallProductionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LitemallProduction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(LitemallProduction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LitemallProduction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") LitemallProductionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_production
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}