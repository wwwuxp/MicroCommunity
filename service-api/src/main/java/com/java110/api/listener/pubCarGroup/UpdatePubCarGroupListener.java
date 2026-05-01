package com.java110.api.listener.pubCarGroup;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarGroup.IPubCarGroupBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarGroupConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存分组信息表侦听
 * add by hh
 */
@Java110Listener("updatePubCarGroupListener")
public class UpdatePubCarGroupListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarGroupBMO pubCarGroupBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        pubCarGroupBMOImpl.updatePubCarGroup(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarGroupConstant.UPDATE_PUBCARGROUP;
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
