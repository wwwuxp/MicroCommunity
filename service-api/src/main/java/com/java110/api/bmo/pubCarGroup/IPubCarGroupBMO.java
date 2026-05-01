package com.java110.api.bmo.pubCarGroup;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @datetime 2021/8/10 17:27
 */
public interface IPubCarGroupBMO extends IApiBaseBMO {

    void addPubCarGroup(JSONObject reqJson, DataFlowContext context);

    void updatePubCarGroup(JSONObject reqJson, DataFlowContext context);

    void deletePubCarGroup(JSONObject reqJson, DataFlowContext context);

}
