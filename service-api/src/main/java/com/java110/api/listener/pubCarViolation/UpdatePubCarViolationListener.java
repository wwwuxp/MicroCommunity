package com.java110.api.listener.pubCarViolation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePubCarViolationConstant;
import org.springframework.http.HttpMethod;

/**
 * 保存车辆违章信息表侦听
 * add by hh
 */
@Java110Listener("updatePubCarViolationListener")
public class UpdatePubCarViolationListener extends AbstractServiceApiPlusListener {

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "violationId", "违章信息ID不能为空");
        Assert.hasKeyAndValue(reqJson, "license", "必填，请填写车牌");
        Assert.hasKeyAndValue(reqJson, "brand", "必填，请选择类型");

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
//        businesses.add(updatePubCarViolation(reqJson, context));
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
        return ServiceCodePubCarViolationConstant.UPDATE_PUBCARVIOLATION;
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
     * 添加车辆违章信息表信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject updatePubCarViolation(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_VIOLATION);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessPubCarViolation = new JSONObject();
        businessPubCarViolation.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessPubCarViolation", businessPubCarViolation);
        return business;
    }

}
