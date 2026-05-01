package com.java110.api.bmo.pubCarReimburse;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @date 2021/10/11
 */
public interface IPubCarReimburseBMO extends IApiBaseBMO {

    /**
     * 保存用车报销信息
     * @param reqJson 请求参数
     * @param context 上下文
     */
    void addPubCarReimburse(JSONObject reqJson, DataFlowContext context);

}
