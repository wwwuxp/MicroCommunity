package com.java110.api.listener.pubCar;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 查询该时间段内的空车数
 * @author hh
 * @date 2021/10/15
 */
@Java110Listener("countEmptyPubCarListener")
public class CountEmptyPubCarListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarInnerServiceSMO pubCarInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "startTime", "开始时间不能为空");
        Assert.hasKeyAndValue(reqJson, "endTime", "结束时间不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        String communityId = reqJson.getString("communityId");
        String startTime = reqJson.getString("startTime");
        String endTime = reqJson.getString("endTime");
        Integer carType = reqJson.getInteger("carType");

        int count = pubCarInnerServiceSMOImpl.countEmptyPubCar(startTime, endTime, carType, communityId);

        context.setResponseEntity(ResultVo.createResponseEntity(count));
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarConstant.COUNT_EMPTY_PUBCAR;
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
