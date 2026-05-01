package com.java110.api.bmo.pubCarRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @datetime 2021/8/6 9:23
 */
public interface IPubCarRepairBMO extends IApiBaseBMO {

    void addPubCarRepair(JSONObject reqJson, DataFlowContext context);

}
