package com.java110.api.listener.pubCarTrip;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarTrip.IPubCarTripBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarTripConstant;
import com.java110.utils.constant.StatusConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 更新行程状态侦听
 * add by hh
 */
@Java110Listener("updatePubCarTripStatusListener")
public class UpdatePubCarTripStatusListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarTripBMO pubCarTripBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        pubCarTripBMOImpl.updateTripStatus(reqJson, context, StatusConstant.STATUS_FINISHED);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripConstant.UPDATE_PUBCARTRIP;
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
