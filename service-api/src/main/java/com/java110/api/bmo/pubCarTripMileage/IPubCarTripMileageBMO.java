package com.java110.api.bmo.pubCarTripMileage;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @datetime 2021/7/30 9:23
 */
public interface IPubCarTripMileageBMO extends IApiBaseBMO {

    /**
     * 新增行程里程
     * @param reqJson 请求json
     * @param context 上下文
     */
    void addTripMileage(JSONObject reqJson, DataFlowContext context);

    /**
     * 新增行程里程V2
     * @param reqJson 请求json
     * @param context 上下文
     */
    void addTripMileageV2(JSONObject reqJson, DataFlowContext context);

    /**
     * 更新行程里程
     * @param reqJson 请求json
     * @param context 上下文
     */
    void updateTripMileage(JSONObject reqJson, DataFlowContext context);

    /**
     * 申请延时
     * @param reqJson 请求json
     * @param context 上下文
     */
    void applyDelay(JSONObject reqJson, DataFlowContext context);

}
