package com.java110.api.listener.pubCarViolation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarViolation.IPubCarViolationBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarViolationConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 删除违章侦听
 * @author hh
 * @date 2021/10/13
 */
@Java110Listener("deletePubCarViolationListener")
public class DeletePubCarViolationListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarViolationBMO pubCarViolationBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "violationId", "违章信息ID不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        pubCarViolationBMOImpl.deleteCarViolation(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarViolationConstant.DELETE_PUBCARVIOLATION;
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
