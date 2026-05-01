package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.entity.community.FtpFile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * (FtpFile)表feign访问层
 *
 * @author lmf
 * @since 2022-07-27 09:26:15
 */
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ftpFileSMOApi")
public interface FtpFileDaoSMO {

    /**
     * 通过ID查询单条数据
     *
     * @param ftpId 主键
     * @return 实例对象
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    FtpFile queryById(@RequestBody String ftpId);

    /**
     * 查询指定行数据
     *
     * @param ftpFile 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    @RequestMapping(value = "/queryAllByLimit", method = RequestMethod.POST)
    List<FtpFile> queryAllByLimit(@RequestBody FtpFile ftpFile);

    /**
     * 统计总行数
     *
     * @param ftpFile 查询条件
     * @return 总行数
     */
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    long count(@RequestBody FtpFile ftpFile);

    /**
     * 新增数据
     *
     * @param ftpFile 实例对象
     * @return 影响行数
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    int insert(@RequestBody FtpFile ftpFile);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<FtpFile> 实例对象列表
     * @return 影响行数
     */
    @RequestMapping(value = "/insertBatch", method = RequestMethod.POST)
    int insertBatch(@RequestBody  List<FtpFile> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<FtpFile> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    @RequestMapping(value = "/insertOrUpdateBatch", method = RequestMethod.POST)
    int insertOrUpdateBatch(@RequestBody List<FtpFile> entities);

    /**
     * 修改数据
     *
     * @param ftpFile 实例对象
     * @return 影响行数
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    int update(@RequestBody FtpFile ftpFile);

    /**
     * 通过主键删除数据
     *
     * @param ftpId 主键
     * @return 影响行数
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    int deleteById(@RequestBody String ftpId);
}

