package com.java110.api.bmo.pubCarUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @datetime 2021/7/24 10:01
 */
public interface IPubCarUserBMO extends IApiBaseBMO {

    void addPubCarUser(JSONObject reqJson, DataFlowContext context);

    void updatePubCarUser(JSONObject reqJson, DataFlowContext context);

    void deletePubCarUser(JSONObject reqJson, DataFlowContext context);

}
