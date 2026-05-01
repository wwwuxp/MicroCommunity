package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarApprovalProcess.PubCarApprovalProcessDto;
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
@RequestMapping("/pubCarApprovalProcessApi")
public interface IPubCarApprovalProcessInnerServiceSMO {

    /**
     * 获取用户能看见的行程id数量
     * @param pubCarApprovalProcessDto 请求参数
     * @return List
     */
    @RequestMapping(value = "/getTripIdsCount", method = RequestMethod.POST)
    int getTripIdsCount(@RequestBody PubCarApprovalProcessDto pubCarApprovalProcessDto);

    /**
     * 获取行程id列表
     * @param pubCarApprovalProcessDto 请求参数
     * @return List
     */
    @RequestMapping(value = "/getTrips", method = RequestMethod.POST)
    List<PubCarApprovalProcessDto> getTrips(@RequestBody PubCarApprovalProcessDto pubCarApprovalProcessDto);

    /**
     * 获取未审核的数量
     * @param pubCarApprovalProcessDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/getNotApprovalCount", method = RequestMethod.POST)
    int getNotApprovalCount(@RequestBody PubCarApprovalProcessDto pubCarApprovalProcessDto);

    /**
     * 局领导是否审批过了
     * @param tripId 行程id
     * @param communityId 小区id
     * @return boolean
     */
    @RequestMapping(value = "/hasBureauLeaderApprove", method = RequestMethod.GET)
    boolean hasBureauLeaderApprove(@RequestParam("tripId") String tripId,
                                   @RequestParam("communityId") String communityId);

    /**
     * 获取当前用户需要审核的外出Id的数量
     * @param pubCarApprovalProcessDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/getOutGoingIdsCount", method = RequestMethod.POST)
    int getOutGoingIdsCount(@RequestBody PubCarApprovalProcessDto pubCarApprovalProcessDto);

    /**
     * 获取当亲用户需要审核的外出id
     * @param pubCarApprovalProcessDto 请求参数
     * @return List
     */
    @RequestMapping(value = "/getOutGoingIds", method = RequestMethod.POST)
    List<String> getOutGoingIds(@RequestBody PubCarApprovalProcessDto pubCarApprovalProcessDto);

    /**
     * 根据行程id获取审批流程数据
     * @param tripId 行程id
     * @return List
     */
    @RequestMapping(value = "/getPubCarApprovalProcess", method = RequestMethod.GET)
    List<Integer> getPubCarApprovalProcess(@RequestParam("tripId") String tripId);

}
