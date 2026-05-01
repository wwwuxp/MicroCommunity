package com.java110.api.bmo.pubCarTrip;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @datetime 2021/7/26 14:52
 */
public interface IPubCarTripBMO extends IApiBaseBMO {

    /**
     * 添加申请用车基本信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void addPubCarTrip(JSONObject reqJson, DataFlowContext context);

    /**
     * 更新行程信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void updatePubCarTrip(JSONObject reqJson, DataFlowContext context);

    /**
     * 更新行程状态
     * @param reqJson 请求json
     * @param context 上下文
     * @param status 状态
     */
    void updateTripStatus(JSONObject reqJson, DataFlowContext context, Integer status);

}
