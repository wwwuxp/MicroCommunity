package com.java110.api.listener.pubCarTripMileage;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarTripMileageInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarTripMileageConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.vo.api.pubCarTripMileage.ApiPubCarTripMileageDataVo;
import com.java110.vo.api.pubCarTripMileage.ApiPubCarTripMileageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listPubCarTripMileagesListener")
public class ListPubCarTripMileagesListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarTripMileageInnerServiceSMO pubCarTripMileageInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripMileageConstant.LIST_PUBCARTRIPMILEAGES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        PubCarTripMileageDto pubCarTripMileageDto = BeanConvertUtil.covertBean(reqJson, PubCarTripMileageDto.class);

        int count = pubCarTripMileageInnerServiceSMOImpl.queryPubCarTripMileagesCount(pubCarTripMileageDto);

        List<ApiPubCarTripMileageDataVo> pubCarTripMileages;

        if (count > 0) {
            pubCarTripMileages = BeanConvertUtil.covertBeanList(pubCarTripMileageInnerServiceSMOImpl.queryPubCarTripMileages(pubCarTripMileageDto), ApiPubCarTripMileageDataVo.class);
        } else {
            pubCarTripMileages = Collections.emptyList();
        }

        ApiPubCarTripMileageVo apiPubCarTripMileageVo = new ApiPubCarTripMileageVo();

        apiPubCarTripMileageVo.setTotal(count);
        apiPubCarTripMileageVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarTripMileageVo.setData(pubCarTripMileages);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPubCarTripMileageVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
