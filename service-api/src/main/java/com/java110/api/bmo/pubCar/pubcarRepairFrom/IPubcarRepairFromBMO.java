package com.java110.api.bmo.pubCar.pubcarRepairFrom;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author lmf
 * @datetime 2022-07-27
 */
public interface IPubcarRepairFromBMO extends IApiBaseBMO {

    /**
     * 新增或者删除维修工单
     * @param reqJson 请求json
     * @param context 上下文
     */
    void saveOrEditPubcarRepairFrom(JSONObject reqJson, DataFlowContext context);

    void deletePubcarRepairFrom(JSONObject reqJson, DataFlowContext context);
}
