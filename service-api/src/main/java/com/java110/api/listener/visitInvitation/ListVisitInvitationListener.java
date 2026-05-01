package com.java110.api.listener.visitInvitation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.visitInvitation.VisitInvitationDto;
import com.java110.intf.community.IVisitInvitationInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeVisitInvitationConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.visitInvitation.ApiVisitInvitationDataVo;
import com.java110.vo.api.visitInvitation.ApiVisitInvitationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询访客邀约信息
 * @author hh
 * create date 2021/9/17
 */
@Java110Listener("listVisitInvitationListener")
public class ListVisitInvitationListener extends AbstractServiceApiListener {

    @Autowired
    private IVisitInvitationInnerServiceSMO visitInvitationInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        VisitInvitationDto visitInvitationDto = BeanConvertUtil.covertBean(reqJson, VisitInvitationDto.class);

        int count = visitInvitationInnerServiceSMOImpl.queryVisitInvitationsCount(visitInvitationDto);

        List<ApiVisitInvitationDataVo> visitInvitationDataVos;

        if (count > 0) {
            visitInvitationDataVos = visitInvitationInnerServiceSMOImpl.queryVisitInvitations(visitInvitationDto);
        } else {
            visitInvitationDataVos = Collections.emptyList();
        }

        ApiVisitInvitationVo apiVisitInvitationVo = new ApiVisitInvitationVo();

        apiVisitInvitationVo.setTotal(count);
        apiVisitInvitationVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiVisitInvitationVo.setData(visitInvitationDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiVisitInvitationVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeVisitInvitationConstant.LIST_VISIT_INVITATIONS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
