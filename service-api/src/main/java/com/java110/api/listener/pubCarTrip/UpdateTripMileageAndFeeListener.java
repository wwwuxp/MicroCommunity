package com.java110.api.listener.pubCarTrip;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.intf.pubcar.IPubCarTripMileageInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarTripConstant;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;

/**
 * @author hh
 * @date 2022/4/15
 */
@Java110Listener("updateTripMileageAndFeeListener")
public class UpdateTripMileageAndFeeListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarTripMileageInnerServiceSMO pubCarTripMileageInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        PubCarTripMileageDto pubCarTripMileageDto = new PubCarTripMileageDto();
        pubCarTripMileageDto.setCommunityId(reqJson.getString("communityId"));
        pubCarTripMileageDto.setTripId(reqJson.getString("tripId"));
        pubCarTripMileageDto.setDriverId(reqJson.getString("driverId"));
        Double startMileage = reqJson.getDouble("startMileage");
        Double endMileage = reqJson.getDouble("endMileage");
        pubCarTripMileageDto.setStartMileage(startMileage);
        pubCarTripMileageDto.setEndMileage(endMileage);
        pubCarTripMileageDto.setPassMileage(endMileage - startMileage);
        pubCarTripMileageDto.setRunRange(reqJson.getString("runRange"));
        pubCarTripMileageDto.setRepairFee(reqJson.getString("repairFee"));
        pubCarTripMileageDto.setOilFee(reqJson.getString("oilFee"));
        pubCarTripMileageDto.setParkingFee(reqJson.getString("parkingFee"));
        pubCarTripMileageDto.setRoadBridgeFee(reqJson.getString("roadBridgeFee"));

        int flag = pubCarTripMileageInnerServiceSMOImpl.updateTripMileageAndFee(pubCarTripMileageDto);
        if (flag < 1){
            throw new RuntimeException("修改失败");
        }
        context.setResponseEntity(ResultVo.success());
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripConstant.UPDATE_MILEAGE_PUBCARTRIP;
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
