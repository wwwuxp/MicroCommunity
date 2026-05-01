package com.java110.api.bmo.PubCarTripCarPassenger;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @datetime 2021/7/26 15:03
 */
public interface IPubCarTripCarPassengerBMO  extends IApiBaseBMO {

    /**
     * 添加乘车人信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void addPubCarTripCarPassenger(JSONObject reqJson, DataFlowContext context);

}
