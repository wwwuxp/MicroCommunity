package com.java110.api.bmo.outGoing.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.outGoing.IOutGoingBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.outGoing.OutGoingPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * create date 2021/9/10
 */
@Service("outGoingBMOImpl")
public class OutGoingBMOImpl extends ApiBaseBMO implements IOutGoingBMO {

    @Override
    public void addOutGoing(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessOutGoingJson = new JSONObject();
        String outId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_OUT_GOING_ID);
        businessOutGoingJson.fluentPut("outId", outId)
                .fluentPut("outType", reqJson.getInteger("outType"))
                .fluentPut("destination", reqJson.getString("destination"))
                .fluentPut("startTime", reqJson.getString("startTime"))
                .fluentPut("endTime", reqJson.getString("endTime"))
                .fluentPut("handlingMatter", reqJson.getString("handlingMatter"))
                .fluentPut("matterDetail", reqJson.getString("matterDetail"))
                .fluentPut("state", StatusConstant.STATUS_RUNNING)
                .fluentPut("userId", context.getUserId())
                .fluentPut("tripId", reqJson.getString("tripId"))
                .fluentPut("communityId", reqJson.getString("communityId"));
        reqJson.put("outId", outId);

        handleBusinessJsonObjectData(context, businessOutGoingJson, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OUT_GOING, OutGoingPo.class);
    }

    @Override
    public void updateOutGoing(JSONObject reqJson, DataFlowContext context) {
        handleBusinessJsonObjectData(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OUT_GOING, OutGoingPo.class);
    }
}
