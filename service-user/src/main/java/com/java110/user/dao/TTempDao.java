package com.java110.user.dao;


import com.java110.entity.user.TTemp;

import java.util.List;

/**
 * (TTemp)表数据库访问层
 *
 * @author lmf
 * @since 2022-09-20 18:46:59
 */
public interface TTempDao {

    /**
     * 通过ID查询单条数据
     *
     * @param tempId 主键
     * @return 实例对象
     */
    TTemp queryById(Long tempId);

    /**
     * 查询指定行数据
     *
     * @param tTemp 查询条件
     * @return 对象列表
     */
    List<TTemp> queryList(TTemp tTemp);

    /**
     * 统计总行数
     *
     * @param tTemp 查询条件
     * @return 总行数
     */
    long count(TTemp tTemp);

    /**
     * 新增数据
     *
     * @param tTemp 实例对象
     * @return 影响行数
     */
    int insert(TTemp tTemp);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TTemp> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(List<TTemp> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TTemp> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(List<TTemp> entities);

    /**
     * 修改数据
     *
     * @param tTemp 实例对象
     * @return 影响行数
     */
    int update(TTemp tTemp);

    /**
     * 通过主键删除数据
     *
     * @param tempId 主键
     * @return 影响行数
     */
    int deleteById(Long tempId);

    /**
     * 查询单条数据
     * @param tTemp
     * @return
     */
    TTemp queryOne(TTemp tTemp);
}

