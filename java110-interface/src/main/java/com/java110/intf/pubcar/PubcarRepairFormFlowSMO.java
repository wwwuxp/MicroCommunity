package com.java110.intf.pubcar;



import com.java110.config.feign.FeignConfiguration;
import com.java110.entity.pubcar.PubcarRepairFormFlow;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * (PubcarRepairFormFlow)表feign访问层
 *
 * @author lmf
 * @since 2022-09-21 09:07:33
 */
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubcarRepairFormFlowSMOApi")
public interface PubcarRepairFormFlowSMO {

    /**
     * ok表示成功， next表示还有下一步， end表示结束， err表示这个数据错误,审批人员不正确
     */
    final int OK = 100,NEXT = 200,END = 300, ERR = 400;
    /**
     *  1未审批  2审批通过  3审批未通过 4退回
     */
    final String PAST = "2", FAIL = "3", GO_BACK_ONE_STEP = "4",NOT_APPROVED = "1";

    /**
     * 通过ID查询单条数据
     *
     * @param flowId 主键
     * @return 实例对象
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    PubcarRepairFormFlow queryById(@RequestBody String flowId);

    /**
     * 查询指定行数据
     *
     * @param pubcarRepairFormFlow 查询条件
     * @return 对象列表
     */
    @RequestMapping(value = "/queryAllByLimit", method = RequestMethod.POST)
    List<PubcarRepairFormFlow> queryList(@RequestBody PubcarRepairFormFlow pubcarRepairFormFlow);

    /**
     * 统计总行数
     *
     * @param pubcarRepairFormFlow 查询条件
     * @return 总行数
     */
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    long count(@RequestBody PubcarRepairFormFlow pubcarRepairFormFlow);

    /**
     * 新增数据
     *
     * @param pubcarRepairFormFlow 实例对象
     * @return 影响行数
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    int insert(@RequestBody PubcarRepairFormFlow pubcarRepairFormFlow);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PubcarRepairFormFlow> 实例对象列表
     * @return 影响行数
     */
    @RequestMapping(value = "/insertBatch", method = RequestMethod.POST)
    int insertBatch(@RequestBody List<PubcarRepairFormFlow> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PubcarRepairFormFlow> 实例对象列表
     * @return 影响行数
     */
    @RequestMapping(value = "/insertOrUpdateBatch", method = RequestMethod.POST)
    int insertOrUpdateBatch(@RequestBody List<PubcarRepairFormFlow> entities);

    /**
     * 修改数据
     *
     * @param pubcarRepairFormFlow 实例对象
     * @return 影响行数
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    int update(@RequestBody PubcarRepairFormFlow pubcarRepairFormFlow);

    /**
     * 通过主键删除数据
     *
     * @param flowId 主键
     * @return 影响行数
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    int deleteById(@RequestBody String flowId);

    /**
     * 插入流程数据
     * @param param
     */
    @RequestMapping(value = "/insertFlow", method = RequestMethod.POST)
    int insertFlow(@RequestBody PubcarRepairFormFlow param);

    /**
     * 根据主表id查询列表
     * @param ids
     * @return
     */
    @RequestMapping(value = "/queryListByIds", method = RequestMethod.POST)
    List<PubcarRepairFormFlow> queryListByIds(@RequestBody List<String> ids);
}

