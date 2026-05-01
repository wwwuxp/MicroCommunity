package com.java110.api.bmo.pubCarAccident;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @date 2021/10/9
 */
public interface IPubCarAccidentBMO extends IApiBaseBMO {

    /**
     * 保存用车事故信息
     * @param reqJson 请求参数
     * @param context context
     */
    void addPubCarAccident(JSONObject reqJson, DataFlowContext context);

}
