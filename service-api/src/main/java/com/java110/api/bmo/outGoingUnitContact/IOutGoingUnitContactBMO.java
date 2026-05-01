package com.java110.api.bmo.outGoingUnitContact;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * create date 2021/9/10
 */
public interface IOutGoingUnitContactBMO extends IApiBaseBMO {

    void addOutGoingUnitContact(JSONObject reqJson, DataFlowContext context);

}
