package com.java110.api.listener.pubCarViolation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarViolation.IPubCarViolationBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarViolationConstant;

import com.java110.core.annotation.Java110Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存车辆违章信息侦听
 * add by hh
 */
@Java110Listener("savePubCarViolationListener")
public class SavePubCarViolationListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarViolationBMO pubCarViolationBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        pubCarViolationBMOImpl.addCarViolation(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarViolationConstant.ADD_PUBCARVIOLATION;
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
