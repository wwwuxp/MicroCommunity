package com.java110.community.smo.impl;

import com.java110.community.dao.IVisitInvitationServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.visitInvitation.VisitInvitationDto;
import com.java110.intf.community.IVisitInvitationInnerServiceSMO;
import com.java110.vo.api.visitInvitation.ApiVisitInvitationDataVo;
import com.java110.vo.api.visitInvitation.ApiVisitorDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 访客邀约信息表内部服务实现类
 * add by hh
 **/
@RestController
public class VisitInvitationInnerServiceSMOImpl extends BaseServiceSMO implements IVisitInvitationInnerServiceSMO {

    @Autowired
    private IVisitInvitationServiceDao visitInvitationServiceDaoImpl;

    @Override
    public boolean hasSameUniqueCode(String uniqueCode, String communityId) {
        Map<String, String> map = new HashMap<>();
        map.put("uniqueCode", uniqueCode);
        map.put("communityId", communityId);
        return Objects.nonNull(visitInvitationServiceDaoImpl.hasSameUniqueCode(map));
    }

    @Override
    public int queryVisitInvitationsCount(@RequestBody VisitInvitationDto visitInvitationDto) {
        return visitInvitationServiceDaoImpl.queryVisitInvitationsCount(visitInvitationDto);
    }

    @Override
    public List<ApiVisitInvitationDataVo> queryVisitInvitations(@RequestBody VisitInvitationDto visitInvitationDto) {
        //校验是否传了 分页信息
        int page = visitInvitationDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            visitInvitationDto.setPage((page - 1) * visitInvitationDto.getRow());
        }
        List<ApiVisitInvitationDataVo> visitInvitationDataVos = visitInvitationServiceDaoImpl.listVisitInvitations(visitInvitationDto);
        for (ApiVisitInvitationDataVo visitInvitationDataVo : visitInvitationDataVos) {
            List<ApiVisitorDataVo> visitors = visitInvitationDataVo.getVisitors();
            List<ApiVisitorDataVo> sortedVisitors = visitors.stream()
                    .sorted(Comparator.comparingInt(ApiVisitorDataVo::getNo))
                    .collect(Collectors.toList());
            for (ApiVisitorDataVo sortedVisitor : sortedVisitors) {
                sortedVisitor.setVisitorId(null);
            }
            visitInvitationDataVo.setVisitors(sortedVisitors);
        }
        // 降序排列
        visitInvitationDataVos.sort(Comparator.comparing(ApiVisitInvitationDataVo::getCreateTime).reversed());
        return visitInvitationDataVos;
    }
}
