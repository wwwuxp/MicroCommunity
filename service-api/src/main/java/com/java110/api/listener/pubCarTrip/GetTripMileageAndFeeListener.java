package com.java110.api.listener.pubCarTrip;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.intf.pubcar.IPubCarTripMileageInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarTripConstant;
import com.java110.vo.ResultVo;
import com.java110.vo.api.pubCarTripMileage.ApiTripMileageAndFeeDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * @author hh
 * @date 2022/4/15
 */
@Java110Listener("getTripMileageAndFeeListener")
public class GetTripMileageAndFeeListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarTripMileageInnerServiceSMO pubCarTripMileageInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        String communityId = reqJson.getString("communityId");
        String tripId = reqJson.getString("tripId");
        String driverId = reqJson.getString("driverId");
        PubCarTripMileageDto pubCarTripMileageDto = new PubCarTripMileageDto();
        pubCarTripMileageDto.setCommunityId(communityId);
        pubCarTripMileageDto.setTripId(tripId);
        pubCarTripMileageDto.setDriverId(driverId);

        ApiTripMileageAndFeeDataVo tripMileageAndFeeDataVo = pubCarTripMileageInnerServiceSMOImpl.getTripMileageAndFee(pubCarTripMileageDto);
        String startMileage = tripMileageAndFeeDataVo.getStartMileage();
        String endMileage = tripMileageAndFeeDataVo.getEndMileage();
        double startMileageDouble = Double.parseDouble(startMileage);
        double endMileageDouble = Double.parseDouble(endMileage);
        tripMileageAndFeeDataVo.setStartMileage(String.valueOf((int) startMileageDouble));
        tripMileageAndFeeDataVo.setEndMileage(String.valueOf((int) endMileageDouble));
        context.setResponseEntity(ResultVo.createResponseEntity(tripMileageAndFeeDataVo));
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripConstant.GET_MILEAGE_PUBCARTRIP;
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
