package com.java110.api.listener.pubCarRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarRepair.IPubCarRepairBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarRepairConstant;

import com.java110.core.annotation.Java110Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存车辆维修侦听
 * add by hh
 */
@Java110Listener("savePubCarRepairListener")
public class SavePubCarRepairListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarRepairBMO pubCarRepairBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        pubCarRepairBMOImpl.addPubCarRepair(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarRepairConstant.ADD_PUBCARREPAIR;
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
