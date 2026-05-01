package com.java110.api.listener.pubCarTrip;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarTripMileageInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarTripConstant;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * @author hh
 * @date 2022/4/8
 */
@Java110Listener("getEndMileageOfPreviousCarListener")
public class GetEndMileageOfPreviousCarListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarTripMileageInnerServiceSMO pubCarTripMileageInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        String communityId = reqJson.getString("communityId");
        String carId = reqJson.getString("carId");

        Double mileageOfPreviousCar = pubCarTripMileageInnerServiceSMOImpl.queryEndMileageOfPreviousCar(carId, communityId);
        context.setResponseEntity(ResultVo.createResponseEntity(mileageOfPreviousCar));
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripConstant.ENDMILEAGE_PREVIOUS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
