package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarApprover.PubCarApproverDto;
import com.java110.vo.api.pubCarApprover.ApiPubCarApproverDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hh
 * @date 2021/12/7
 */
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarApproverApi")
public interface IPubCarApproverInnerServiceSMO {

    /**
     * 获取用车审核人信息
     * @return List
     */
    @RequestMapping(value = "/getPubCarApprovers", method = RequestMethod.GET)
    List<String> getPubCarApprovers(@RequestParam("roleType") Integer roleType,
                                    @RequestParam("tableType") Integer tableType);

    /**
     * 获取用户领导
     * @param userId 用户id
     * @return List
     */
    @RequestMapping(value = "/getUserLeader", method = RequestMethod.GET)
    List<String> getUserLeader(@RequestParam("userId") String userId,
                               @RequestParam("tableType") Integer tableType);

    /**
     * 是否已经存在相同用车审批人了
     * @param existApproverDto 请求参数
     * @return boolean
     */
    @RequestMapping(value = "/existPubCarApprover", method = RequestMethod.POST)
    boolean existPubCarApprover(@RequestBody PubCarApproverDto existApproverDto);

    /**
     * 保存用车审批人信息
     * @param approverDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/savePubCarApprover", method = RequestMethod.POST)
    int savePubCarApprover(@RequestBody PubCarApproverDto approverDto);

    /**
     * 获取用车审批人数量
     * @param pubCarApproverDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/queryPubCarApproversCount", method = RequestMethod.POST)
    int queryPubCarApproversCount(@RequestBody PubCarApproverDto pubCarApproverDto);

    /**
     * 获取用车审批人列表
     * @param pubCarApproverDto 请求参数
     * @return List
     */
    @RequestMapping(value = "/queryPubCarApprovers", method = RequestMethod.POST)
    List<ApiPubCarApproverDataVo> queryPubCarApprovers(@RequestBody PubCarApproverDto pubCarApproverDto);

    /**
     * 根据id删除用车审核人信息
     * @param pubCarApproverDto id
     * @return int
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    int deleteById(@RequestBody PubCarApproverDto pubCarApproverDto);

    /**
     * 是否是用车审批人
     * @param userId 用户id
     * @return boolean
     */
    @RequestMapping(value = "/isApprover", method = RequestMethod.GET)
    boolean isApprover(@RequestParam("userId") String userId,
                       @RequestParam("tableType") Integer tableType);

    /**
     * 根据审核人id获取角色类型
     * @param userId 审核人id
     * @param tableType 表类型
     * @return PubCarApproverDto
     */
    @RequestMapping(value = "/getRoleTypeByApproverId", method = RequestMethod.GET)
    PubCarApproverDto getRoleTypeAndDepartmentIdByApproverId(@RequestParam("userId") String userId,
                                                             @RequestParam("tableType") Integer tableType);

    /**
     * 当前部门是由哪些局领导管理
     * @param departmentId 部门id
     * @return List
     */
    @RequestMapping(value = "/whichBureauLeaderManage", method = RequestMethod.GET)
    List<String> whichBureauLeaderManage(@RequestParam("departmentId") String departmentId);

    /**
     * 更新审批人
     * @param approverDto approverDto
     * @return int
     */
    @RequestMapping(value = "/updatePubCarApprover", method = RequestMethod.POST)
    int updatePubCarApprover(@RequestBody PubCarApproverDto approverDto);

}
