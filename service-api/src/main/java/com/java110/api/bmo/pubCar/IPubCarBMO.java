package com.java110.api.bmo.pubCar;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * @datetime 2021/7/15 11:31
 */
public interface IPubCarBMO extends IApiBaseBMO {

    /**
     * 添加车辆信息
     * @param reqJson
     * @param context
     */
    void addCar(JSONObject reqJson, DataFlowContext context);

    /**
     * 修改车辆信息
     * @param reqJson
     * @param context
     */
    void updatePubCar(JSONObject reqJson, DataFlowContext context);

    /**
     * 删除车辆信息
     * @param reqJson
     * @param context
     */
    void deletePubCar(JSONObject reqJson, DataFlowContext context);

    /**
     * 确认已续保、确认已保养、确认已季审或已领标、确认已年审或已领标
     * @param reqJson
     * @param context
     */
    void confirmPubCar(JSONObject reqJson, DataFlowContext context);
}
