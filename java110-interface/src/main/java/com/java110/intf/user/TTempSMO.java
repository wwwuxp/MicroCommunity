package com.java110.intf.user;



import com.java110.config.feign.FeignConfiguration;
import com.java110.entity.user.TTemp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * (TTemp)表feign访问层
 *
 * @author lmf
 * @since 2022-09-20 18:47:05
 */
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/tTempSMOApi")
public interface TTempSMO {

    /**
     * 通过ID查询单条数据
     *
     * @param tempId 主键
     * @return 实例对象
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    TTemp queryById(@RequestBody Long tempId);

    /**
     * 查询单条数据
     *
     * @param tTemp 查询条件
     * @return 对象列表
     */
    @RequestMapping(value = "/queryOne", method = RequestMethod.POST)
    TTemp queryOne(@RequestBody TTemp tTemp);

    /**
     * 查询指定行数据
     *
     * @param tTemp 查询条件
     * @return 对象列表
     */
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    List<TTemp> queryList(@RequestBody TTemp tTemp);

    /**
     * 统计总行数
     *
     * @param tTemp 查询条件
     * @return 总行数
     */
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    long count(@RequestBody TTemp tTemp);

    /**
     * 新增数据
     *
     * @param tTemp 实例对象
     * @return 影响行数
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    int insert(@RequestBody TTemp tTemp);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TTemp> 实例对象列表
     * @return 影响行数
     */
    @RequestMapping(value = "/insertBatch", method = RequestMethod.POST)
    int insertBatch(@RequestBody List<TTemp> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TTemp> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    @RequestMapping(value = "/insertOrUpdateBatch", method = RequestMethod.POST)
    int insertOrUpdateBatch(@RequestBody List<TTemp> entities);

    /**
     * 修改数据
     *
     * @param tTemp 实例对象
     * @return 影响行数
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    int update(@RequestBody TTemp tTemp);

    /**
     * 通过主键删除数据
     *
     * @param tempId 主键
     * @return 影响行数
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    int deleteById(@RequestBody Long tempId);
}

