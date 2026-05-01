package com.hezong.pubcar.dao;

import com.java110.dto.pubCar.repaorForm.PubcarRepairFormDto;
import com.java110.dto.pubCar.repaorForm.PubcarRepairFormWhereDto;
import com.java110.entity.pubcar.BusinessPubcarRepairForm;
import com.java110.entity.pubcar.PubcarRepairForm;

import java.util.List;
import java.util.Map;

/**
 * (PubcarRepairForm)表数据库访问层
 *
 * @author lmf
 * @since 2022-07-26 17:26:12
 */
public interface PubcarRepairFormDao {

    /**
     * 根据条件查询分页
     * @param entity
     * @return
     */
    List<PubcarRepairFormDto> quertyPageList(PubcarRepairFormWhereDto entity);

    /**
     * 查询总数
     * @param entity
     * @return
     */
    int quertyPageCount(PubcarRepairFormWhereDto entity);

    /**
     * 新增Business
     * @param entity
     * @return
     */
    int saveBusinessPubcarRepairForm(BusinessPubcarRepairForm entity);

    /**
     * 保存 修车申请
     * @param info
     */
    int savePubcarRepairFormInfo(Map info);

    /**
     * 获取 Business 信息
     * @param info
     * @return
     */
    List<BusinessPubcarRepairForm> getBusinessPubcarRepairFormInfo(Map info);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PubcarRepairForm queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param pubcarRepairForm 查询条件
     * @return 对象列表
     */
    List<PubcarRepairForm> queryAllByLimit(PubcarRepairForm pubcarRepairForm);

    /**
     * 统计总行数
     *
     * @param pubcarRepairForm 查询条件
     * @return 总行数
     */
    Integer count(PubcarRepairForm pubcarRepairForm);

    /**
     * 新增数据
     *
     * @param pubcarRepairForm 实例对象
     * @return 影响行数
     */
    int insert(PubcarRepairForm pubcarRepairForm);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PubcarRepairForm> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(List<PubcarRepairForm> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PubcarRepairForm> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(List<PubcarRepairForm> entities);

    /**
     * 修改数据
     *
     * @param pubcarRepairForm 实例对象
     * @return 影响行数
     */
    int update(PubcarRepairForm pubcarRepairForm);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

