package com.java110.api.bmo.pubCarDriver;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @datetime 2021/7/22 17:15
 */
public interface IPubCarDriverBMO extends IApiBaseBMO {

    void addPubCarDriver(JSONObject paramInJson, DataFlowContext context);

    void updatePubCarDriver(JSONObject reqJson, DataFlowContext context);

    void deletePubCarDriver(JSONObject reqJson, DataFlowContext context);

}
