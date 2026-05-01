package com.java110.api.bmo.outGoing;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * create date 2021/9/10
 */
public interface IOutGoingBMO extends IApiBaseBMO {

    /**
     * 添加外出登记信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void addOutGoing(JSONObject reqJson, DataFlowContext context);

    /**
     * 更新外出信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void updateOutGoing(JSONObject reqJson, DataFlowContext context);

}
