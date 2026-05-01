package com.java110.community.dao;

import com.java110.entity.community.FtpFile;

import java.util.List;

/**
 * (FtpFile)表数据库访问层
 *
 * @author lmf
 * @since 2022-07-27 09:26:06
 */
public interface FtpFileDao {

    /**
     * 通过ID查询单条数据
     *
     * @param ftpId 主键
     * @return 实例对象
     */
    FtpFile queryById(String ftpId);

    /**
     * 查询指定行数据
     *
     * @param ftpFile 查询条件
     * @return 对象列表
     */
    List<FtpFile> queryAllByLimit(FtpFile ftpFile);

    /**
     * 统计总行数
     *
     * @param ftpFile 查询条件
     * @return 总行数
     */
    long count(FtpFile ftpFile);

    /**
     * 新增数据
     *
     * @param ftpFile 实例对象
     * @return 影响行数
     */
    int insert(FtpFile ftpFile);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<FtpFile> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(List<FtpFile> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<FtpFile> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(List<FtpFile> entities);

    /**
     * 修改数据
     *
     * @param ftpFile 实例对象
     * @return 影响行数
     */
    int update(FtpFile ftpFile);

    /**
     * 通过主键删除数据
     *
     * @param ftpId 主键
     * @return 影响行数
     */
    int deleteById(String ftpId);

}

