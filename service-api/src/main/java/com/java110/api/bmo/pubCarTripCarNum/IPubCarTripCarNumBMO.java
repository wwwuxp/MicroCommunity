package com.java110.api.bmo.pubCarTripCarNum;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @datetime 2021/7/26 15:01
 */
public interface IPubCarTripCarNumBMO extends IApiBaseBMO {

    /**
     * 添加申请车辆数信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void addPubCarTripCarNum(JSONObject reqJson, DataFlowContext context);

    /**
     * 更新申请车辆数信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void updatePubCarTripCarNum(JSONObject reqJson, DataFlowContext context);

}
