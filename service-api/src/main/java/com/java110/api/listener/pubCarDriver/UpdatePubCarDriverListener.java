package com.java110.api.listener.pubCarDriver;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarDriver.IPubCarDriverBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarDriverConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 修改司机管理表侦听
 * add by hh
 */
@Java110Listener("updatePubCarDriverListener")
public class UpdatePubCarDriverListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarDriverBMO pubCarDriverBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        pubCarDriverBMOImpl.updatePubCarDriver(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarDriverConstant.UPDATE_PUBCAR_DRIVER;
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
