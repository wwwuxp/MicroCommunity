package com.java110.api.listener.pubCar;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCar.IPubCarBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarConstant;

import com.java110.core.annotation.Java110Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存公车侦听
 * add by hh
 */
@Java110Listener("savePubCarListener")
public class SavePubCarListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarBMO pubCarBmoImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        // 添加公车基本信息
        pubCarBmoImpl.addCar(reqJson, context);
    }


    @Override
    public String getServiceCode() {
        return ServiceCodePubCarConstant.ADD_PUBCAR;
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
