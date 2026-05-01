package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.visitInvitation.VisitInvitationDto;
import com.java110.vo.api.visitInvitation.ApiVisitInvitationDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName IVisitInvitationInnerServiceSMO
 * @Description 访客邀约信息表接口类
 * add by hh
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/visitInvitationApi")
public interface IVisitInvitationInnerServiceSMO {

    @RequestMapping(value = "/hasSameUniqueCode", method = RequestMethod.GET)
    boolean hasSameUniqueCode(@RequestParam("uniqueCode") String uniqueCode,
                              @RequestParam("communityId") String communityId);

    /**
     * 查询访客邀约数量
     * @param visitInvitationDto 参数
     * @return int
     */
    @RequestMapping(value = "/queryVisitInvitationsCount", method = RequestMethod.POST)
    int queryVisitInvitationsCount(@RequestBody VisitInvitationDto visitInvitationDto);

    /**
     * 查询访客邀约列表
     * @param visitInvitationDto 参数
     * @return list
     */
    @RequestMapping(value = "/queryVisitInvitations", method = RequestMethod.POST)
    List<ApiVisitInvitationDataVo> queryVisitInvitations(@RequestBody VisitInvitationDto visitInvitationDto);

}
