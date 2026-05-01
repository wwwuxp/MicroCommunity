package com.java110.api.bmo.pubCarReimburse.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarReimburse.IPubCarReimburseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.po.pubCarReimburse.PubCarReimbursePo;
import com.java110.utils.constant.BusinessTypeConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * @date 2021/10/11
 */
@Service("pubCarReimburseBMOImpl")
public class PubCarReimburseBMOImpl extends ApiBaseBMO implements IPubCarReimburseBMO {

    @Override
    public void addPubCarReimburse(JSONObject reqJson, DataFlowContext context) {
        reqJson.put("approvalStatus", 1);
        reqJson.put("userId", context.getUserId());
        handleBusinessJsonObjectData(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_REIMBURSE, PubCarReimbursePo.class);
    }

}
