package com.java110.api.listener.outGoing;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;

import com.java110.core.annotation.Java110Listener;
import com.java110.utils.constant.ServiceCodeOutGoingConstant;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by hh
 */
@Java110Listener("deleteOutGoingListener")
public class DeleteOutGoingListener extends AbstractServiceApiPlusListener {

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "outId", "外出ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOutGoingConstant.DELETE_OUTGOING;
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
