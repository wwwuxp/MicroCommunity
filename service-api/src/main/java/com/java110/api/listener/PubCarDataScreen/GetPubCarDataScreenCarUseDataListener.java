package com.java110.api.listener.PubCarDataScreen;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarDataScreenConstant;
import com.java110.vo.api.pubCarDataScreen.CarTripVo;
import com.java110.vo.api.pubCarDataScreen.CarTripDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 获取用车数据侦听
 * add by hh
 */
@Java110Listener("getPubCarDataScreenCarUseDataListener")
public class GetPubCarDataScreenCarUseDataListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarTripInnerServiceSMO pubCarTripInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        String communityId = reqJson.getString("communityId");
        Integer page = reqJson.getInteger("page");
        Integer row = reqJson.getInteger("row");
        // 必填，用以区分是今天、明天还是未来7天(1:今天，2:明天,3:未来7天)
        Integer symbol = reqJson.getInteger("symbol");

        int count = pubCarTripInnerServiceSMOImpl.queryPubCarDataScreenCarUseDataCount(symbol, communityId);

        List<CarTripDataVo> carUseDataVo;
        // 获取用车数据
        if (count > 0){
            carUseDataVo = pubCarTripInnerServiceSMOImpl.queryPubCarDataScreenCarUseData(page, row, symbol, communityId);
        }else {
            carUseDataVo = Collections.emptyList();
        }

        CarTripVo carTripVo = new CarTripVo();

        carTripVo.setTotal(count);
        carTripVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        carTripVo.setData(carUseDataVo);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(carTripVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarDataScreenConstant.LIST_PUBCAR_DATASCREEN_CARUSE_DATA;
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
