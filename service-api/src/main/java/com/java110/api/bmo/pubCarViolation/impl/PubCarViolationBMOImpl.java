package com.java110.api.bmo.pubCarViolation.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarViolation.IPubCarViolationBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.pubCarViolation.PubCarViolationPo;
import com.java110.utils.constant.BusinessTypeConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * @datetime 2021/8/5 16:21
 */
@Service("pubCarViolationBMOImpl")
public class PubCarViolationBMOImpl extends ApiBaseBMO implements IPubCarViolationBMO {

    @Override
    public void addCarViolation(JSONObject reqJson, DataFlowContext context) {
        reqJson.put("violationId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_VIOLATION_ID));
        reqJson.put("handleStatus", 1);
        handleBusinessJsonObjectData(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_VIOLATION, PubCarViolationPo.class);
    }

    @Override
    public void handleCarViolation(JSONObject reqJson, DataFlowContext context) {
        handleBusinessJsonObjectData(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_VIOLATION, PubCarViolationPo.class);
    }

    @Override
    public void deleteCarViolation(JSONObject reqJson, DataFlowContext context) {
        handleBusinessJsonObjectData(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_VIOLATION, PubCarViolationPo.class);
    }
}
