package com.java110.api.listener.PubCarDataScreen;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarDataScreenConstant;
import com.java110.vo.api.pubCarDataScreen.ApiPubCarDataScreenOverviewAndAnalysisDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 获取数据总览和分析部分数据侦听
 * add by hh
 */
@Java110Listener("getPubCarDataScreenOverviewAndAnalysisListener")
public class GetPubCarDataScreenOverviewAndAnalysisListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarTripInnerServiceSMO pubCarTripInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        String communityId = reqJson.getString("communityId");
        String storeId = reqJson.getString("storeId");
        String startTimeStr = reqJson.getString("startTime");
        String endTimeStr = reqJson.getString("endTime");

        // 获取数据总览、用车数据中的统计部分、部门用车分析、司机驾驶分析、车辆使用分析数据
        ApiPubCarDataScreenOverviewAndAnalysisDataVo overviewAndAnalysisData = pubCarTripInnerServiceSMOImpl.queryPubCarDataScreenOverviewAndAnalysisData(startTimeStr, endTimeStr, storeId, communityId);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(overviewAndAnalysisData), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarDataScreenConstant.LIST_PUBCAR_DATASCREEN_OVERVIEW_AND_ANALYSIS;
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
