package com.hezong.pubcar.dao;




import com.java110.entity.pubcar.PubcarRepairFormFlow;
import java.util.List;

/**
 * (PubcarRepairFormFlow)表数据库访问层
 *
 * @author lmf
 * @since 2022-09-21 09:07:21
 */
public interface PubcarRepairFormFlowDao {

    /**
     * 通过ID查询单条数据
     *
     * @param flowId 主键
     * @return 实例对象
     */
    PubcarRepairFormFlow queryById(String flowId);

    /**
     * 查询指定行数据
     *
     * @param pubcarRepairFormFlow 查询条件
     * @return 对象列表
     */
    List<PubcarRepairFormFlow> queryList(PubcarRepairFormFlow pubcarRepairFormFlow);

    /**
     * 统计总行数
     *
     * @param pubcarRepairFormFlow 查询条件
     * @return 总行数
     */
    long count(PubcarRepairFormFlow pubcarRepairFormFlow);

    /**
     * 新增数据
     *
     * @param pubcarRepairFormFlow 实例对象
     * @return 影响行数
     */
    int insert(PubcarRepairFormFlow pubcarRepairFormFlow);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PubcarRepairFormFlow> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(List<PubcarRepairFormFlow> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PubcarRepairFormFlow> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(List<PubcarRepairFormFlow> entities);

    /**
     * 修改数据
     *
     * @param pubcarRepairFormFlow 实例对象
     * @return 影响行数
     */
    int update(PubcarRepairFormFlow pubcarRepairFormFlow);

    /**
     * 通过主键删除数据
     *
     * @param flowId 主键
     * @return 影响行数
     */
    int deleteById(String flowId);

    List<PubcarRepairFormFlow> queryListByIds(List<String> ids);
}

