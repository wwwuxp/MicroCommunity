package com.java110.api.bmo.pubCarViolation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @datetime 2021/8/5 16:21
 */
public interface IPubCarViolationBMO extends IApiBaseBMO {

    /**
     * 添加公车违章信息
     * @param reqJson 请求Json
     * @param context 上下文
     */
    void addCarViolation(JSONObject reqJson, DataFlowContext context);

    /**
     * 处理违章信息
     * @param reqJson 请求Json
     * @param context 上下文
     */
    void handleCarViolation(JSONObject reqJson, DataFlowContext context);

    /**
     * 删除车辆违章信息
     * @param reqJson 请求Json
     * @param context 上下文
     */
    void deleteCarViolation(JSONObject reqJson, DataFlowContext context);

}
