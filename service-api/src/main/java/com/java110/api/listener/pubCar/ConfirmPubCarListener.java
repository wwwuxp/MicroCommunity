package com.java110.api.listener.pubCar;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCar.IPubCarBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 确认已续保、确认已保养、确认已季审或已领标、确认已年审或已领标 侦听
 * @author hh
 * @datetime 2021/8/25 9:25
 */
@Java110Listener("confirmPubCarListener")
public class ConfirmPubCarListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarBMO pubCarBmoImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarConstant.CONFIRM_PUBCAR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        pubCarBmoImpl.confirmPubCar(reqJson, context);
    }
}
