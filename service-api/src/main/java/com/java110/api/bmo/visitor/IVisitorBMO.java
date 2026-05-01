package com.java110.api.bmo.visitor;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * create date 2021/9/16
 */
public interface IVisitorBMO extends IApiBaseBMO {

    /**
     * 添加访客信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void addVisitor(JSONObject reqJson, DataFlowContext context);

    /**
     * 更新访客信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void updateVisitor(JSONObject reqJson, DataFlowContext context);

    /**
     * 删除访客
     * @param reqJson 请求json
     * @param context 上下文
     */
    void deleteVisitor(JSONObject reqJson, DataFlowContext context);

}
