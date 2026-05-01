package com.java110.api.listener.outGoing;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.outGoing.IOutGoingBMO;
import com.java110.api.bmo.pubCarApprovalProcess.IPubCarApprovalProcessBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeOutGoingConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 审核外出信息 侦听
 * add by hh
 */
@Java110Listener("auditOutGoingListener")
public class AuditOutGoingListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IOutGoingBMO outGoingBMOImpl;

    @Autowired
    private IPubCarApprovalProcessBMO pubCarApprovalProcessBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "outId", "外出id不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区id不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "审核状态不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        outGoingBMOImpl.updateOutGoing(reqJson, context);

        JSONObject approvalProcessStatusJson = new JSONObject();
        approvalProcessStatusJson.put("approvalStatus", reqJson.getInteger("state"));
        approvalProcessStatusJson.put("tripId", reqJson.getString("outId"));
        approvalProcessStatusJson.put("reviewerRole", reqJson.getString("reviewerRole"));
        approvalProcessStatusJson.put("communityId", reqJson.getString("communityId"));
        pubCarApprovalProcessBMOImpl.updatePubCarApprovalProcess(approvalProcessStatusJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOutGoingConstant.AUDIT_OUTGOING;
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
