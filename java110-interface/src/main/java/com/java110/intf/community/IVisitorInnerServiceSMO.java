package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.visitor.VisitorDto;
import com.java110.po.visitor.VisitorPo;
import com.java110.vo.api.visitInvitation.ApiVisitInvitationDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IVisitorInnerServiceSMO
 * @Description 访客邀约人信息表接口类
 * add by hh
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/visitorApi")
public interface IVisitorInnerServiceSMO {

    @RequestMapping(value = "/saveVisitor", method = RequestMethod.POST)
    int saveVisitor(@RequestBody VisitorPo visitorPo);

    @RequestMapping(value = "/updateVisitor", method = RequestMethod.POST)
    int updateVisitor(@RequestBody  VisitorPo visitorPo);

    @RequestMapping(value = "/deleteVisitor", method = RequestMethod.POST)
    int deleteVisitor(@RequestBody  VisitorPo visitorPo);

    @RequestMapping(value = "/queryVisitors", method = RequestMethod.POST)
    List<ApiVisitInvitationDataVo> queryVisitors(@RequestBody VisitorDto visitorDto);

    @RequestMapping(value = "/queryVisitorsCount", method = RequestMethod.POST)
    int queryVisitorsCount(@RequestBody VisitorDto visitorDto);

}
