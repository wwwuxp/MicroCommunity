package com.java110.api.listener.pubCarTrip;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.BusinessTypeConstant;

import com.java110.core.annotation.Java110Listener;
import com.java110.utils.constant.ServiceCodePubCarTripConstant;
import org.springframework.http.HttpMethod;

/**
 * 删除行程信息侦听
 * add by hh
 */
@Java110Listener("deletePubCarTripListener")
public class DeletePubCarTripListener extends AbstractServiceApiPlusListener {

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "tripId", "行程ID不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

//        HttpHeaders header = new HttpHeaders();
//        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
//        JSONArray businesses = new JSONArray();
//
//        AppService service = event.getAppService();
//
//        //添加单元信息
//        businesses.add(deletePubCarTrip(reqJson, context));
//
//        JSONObject paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());
//
//        //将 rest header 信息传递到下层服务中去
//        super.freshHttpHeader(header, context.getRequestCurrentHeaders());
//
//        ResponseEntity<String> responseEntity = this.callService(context, service.getServiceCode(), paramInObj);
//
//        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripConstant.DELETE_PUBCARTRIP;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject deletePubCarTrip(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_TRIP);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessPubCarTrip = new JSONObject();
        businessPubCarTrip.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessPubCarTrip", businessPubCarTrip);
        return business;
    }

}
