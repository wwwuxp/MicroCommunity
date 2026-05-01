package com.java110.api.listener.pubCar.repairForm;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCar.pubcarRepairFrom.IPubcarRepairFromBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarUserInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubcarRepairFromConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 添加公车申请维修
 *
 * @author lmf
 * @since 2022-07-26 17:26:21
 */
@Java110Listener("deletePubcarRepairFormListener")
public class DeletePubcarRepairFormListener extends AbstractServiceApiPlusListener {

    @Autowired
    IPubcarRepairFromBMO pubcarRepairFromBMO;

    @Autowired
    IPubCarUserInnerServiceSMO pubCarUserInnerServiceSMO;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //传输数据进行处理
        pubcarRepairFromBMO.deletePubcarRepairFrom(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubcarRepairFromConstant.DELETE_REPAIR_REQUEST;
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

