package com.java110.api.bmo.visitor.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.visitor.IVisitorBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.visitor.VisitorPo;
import com.java110.utils.constant.BusinessTypeConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * create date 2021/9/16
 */
@Service("visitorBMOImpl")
public class VisitorBMOImpl extends ApiBaseBMO implements IVisitorBMO {

    @Override
    public void addVisitor(JSONObject reqJson, DataFlowContext context) {
        JSONArray visitorArray = reqJson.getJSONArray("visitors");
        for (int i = 0, size = visitorArray.size(); i < size; i++) {
            JSONObject oneJsonObject = visitorArray.getJSONObject(i);
            oneJsonObject.put("visitorId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_VISITOR_ID));
            oneJsonObject.put("invitationId", reqJson.getString("invitationId"));
        }

        handleBusinessJsonArrayData(context, visitorArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_VISITOR, VisitorPo.class);
    }

    @Override
    public void updateVisitor(JSONObject reqJson, DataFlowContext context) {
        handleBusinessJsonObjectData(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_VISITOR, VisitorPo.class);
    }

    @Override
    public void deleteVisitor(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessJson = new JSONObject();
        businessJson.put("invitationId", reqJson.getString("invitationId"));
        handleBusinessJsonObjectData(context, businessJson, BusinessTypeConstant.BUSINESS_TYPE_DELETE_VISITOR, VisitorPo.class);
    }

}
