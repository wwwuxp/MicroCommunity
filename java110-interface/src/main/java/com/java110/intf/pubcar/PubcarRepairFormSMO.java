package com.java110.intf.pubcar;

import com.java110.dto.pubCar.repaorForm.PubcarRepairFormDto;
import com.java110.dto.pubCar.repaorForm.PubcarRepairFormWhereDto;
import com.java110.entity.pubcar.PubcarRepairForm;
import com.java110.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * (PubcarRepairForm)表feign访问层
 *
 * @author lmf
 * @since 2022-07-26 17:26:20
 */
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubcarRepairFormSMOApi")
public interface PubcarRepairFormSMO {

    /**
     * 通过ID查询单条数据
     *
     * @param entity 请求参数
     * @return 实例对象
     */
    @RequestMapping(value = "/quertyPageList", method = RequestMethod.POST)
    List<PubcarRepairFormDto> quertyPageList(@RequestBody PubcarRepairFormWhereDto entity);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @RequestMapping(value = "/quertyPageCount", method = RequestMethod.POST)
    int quertyPageCount(@RequestBody PubcarRepairFormWhereDto entity);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    PubcarRepairForm queryById(@RequestBody String id);

    /**
     * 查询指定行数据
     *
     * @param pubcarRepairForm 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    @RequestMapping(value = "/queryAllByLimit", method = RequestMethod.POST)
    List<PubcarRepairForm> queryAllByLimit(PubcarRepairForm pubcarRepairForm);

    /**
     * 统计总行数
     *
     * @param pubcarRepairForm 查询条件
     * @return 总行数
     */
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    Integer count(@RequestBody PubcarRepairForm pubcarRepairForm);

    /**
     * 新增数据
     *
     * @param pubcarRepairForm 实例对象
     * @return 影响行数
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    int insert(PubcarRepairForm pubcarRepairForm);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PubcarRepairForm> 实例对象列表
     * @return 影响行数
     */
    @RequestMapping(value = "/insertBatch", method = RequestMethod.POST)
    int insertBatch( List<PubcarRepairForm> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PubcarRepairForm> 实例对象列表
     * @return 影响行数
     * @throws 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    @RequestMapping(value = "/insertOrUpdateBatch", method = RequestMethod.POST)
    int insertOrUpdateBatch(List<PubcarRepairForm> entities);

    /**
     * 修改数据
     *
     * @param pubcarRepairForm 实例对象
     * @return 影响行数
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    int update(@RequestBody PubcarRepairForm pubcarRepairForm);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    int deleteById(Integer id);
}

