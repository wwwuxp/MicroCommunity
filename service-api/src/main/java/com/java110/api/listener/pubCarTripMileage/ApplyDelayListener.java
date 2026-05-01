package com.java110.api.listener.pubCarTripMileage;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarTripMileage.IPubCarTripMileageBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarTripMileageConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;

/**
 * 司机申请延时
 * @author hh
 * @date 2021/10/16
 */
@Java110Listener("applyDelayListener")
public class ApplyDelayListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarTripMileageBMO pubCarTripMileageBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "driverId", "driverId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "tripId", "tripId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "delayTime", "延时结束时间不能为空");
        Assert.hasKeyAndValue(reqJson, "delayReason", "延时原因不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        pubCarTripMileageBMOImpl.applyDelay(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripMileageConstant.APPLYDELAY_PUBCARTRIPMILEAGE;
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
