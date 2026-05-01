package com.java110.api.listener.visitInvitation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.visitInvitation.IVisitInvitationBMO;
import com.java110.api.bmo.visitor.IVisitorBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeVisitInvitationConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;

/**
 * 删除访客邀约信息
 * @author hh
 * create date 2021/9/23
 */
@Java110Listener("deleteVisitInvitationListener")
public class DeleteVisitInvitationListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IVisitInvitationBMO visitInvitationBMOImpl;

    @Autowired
    private IVisitorBMO visitorBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        visitInvitationBMOImpl.deleteVisitInvitation(reqJson, context);
        visitorBMOImpl.deleteVisitor(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeVisitInvitationConstant.DELETE_VISIT_INVITATION;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
