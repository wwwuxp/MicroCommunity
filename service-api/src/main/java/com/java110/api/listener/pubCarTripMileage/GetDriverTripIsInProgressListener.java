package com.java110.api.listener.pubCarTripMileage;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.intf.pubcar.IPubCarTripMileageInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarTripMileageConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarTripMileage.ApiTripIsInProgressDataVo;
import com.java110.vo.api.pubCarTripMileage.ApiTripIsInProgressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

/**
 * 获取当前司机行程是否在进行中
 * @author hh
 * @datetime 2021/8/23 14:03
 */
@Java110Listener("getDriverTripIsInProgressListener")
public class GetDriverTripIsInProgressListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarTripMileageInnerServiceSMO pubCarTripMileageInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区id不能为空");
        Assert.hasKeyAndValue(reqJson, "driverId", "司机id不能为空");
        Assert.hasKeyAndValue(reqJson, "tripId", "行程id不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        PubCarTripMileageDto pubCarTripMileageDto = BeanConvertUtil.covertBean(reqJson, PubCarTripMileageDto.class);

        // 获取司机的当前行程是否是在进行中
        Boolean isInProgress = pubCarTripMileageInnerServiceSMOImpl.getDriverTripIsInProgress(pubCarTripMileageDto);
        ApiTripIsInProgressDataVo apiTripIsInProgressDataVo = new ApiTripIsInProgressDataVo();
        apiTripIsInProgressDataVo.setInProgress(isInProgress);

        ApiTripIsInProgressVo apiTripIsInProgressVo = new ApiTripIsInProgressVo();
        apiTripIsInProgressVo.setData(apiTripIsInProgressDataVo);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiTripIsInProgressVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripMileageConstant.IS_INPROGRESS_PUBCARTRIPMILEAGE;
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
