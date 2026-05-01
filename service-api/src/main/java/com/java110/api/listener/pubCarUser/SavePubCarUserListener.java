package com.java110.api.listener.pubCarUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarUser.IPubCarUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarUserConstant;

import com.java110.core.annotation.Java110Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存用车人侦听
 * add by hh
 */
@Java110Listener("savePubCarUserListener")
public class SavePubCarUserListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarUserBMO pubCarUserBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        pubCarUserBMOImpl.addPubCarUser(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarUserConstant.ADD_PUBCARUSER;
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
