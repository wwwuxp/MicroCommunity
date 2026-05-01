package com.java110.api.bmo.outGoingPersonnel;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * create date 2021/9/10
 */
public interface IOutGoingPersonnelBMO extends IApiBaseBMO {

    void addOutGoingPersonnel(JSONObject reqJson, DataFlowContext context);

}
