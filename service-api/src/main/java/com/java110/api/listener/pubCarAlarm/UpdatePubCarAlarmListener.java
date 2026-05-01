package com.java110.api.listener.pubCarAlarm;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarAlarm.IPubCarAlarmBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarAlarmConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 修改异常报警信息表侦听
 * add by hh
 */
@Java110Listener("updatePubCarAlarmListener")
public class UpdatePubCarAlarmListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarAlarmBMO pubCarAlarmBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        pubCarAlarmBMOImpl.updatePubCarAlarm(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarAlarmConstant.UPDATE_PUBCARALARM;
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
