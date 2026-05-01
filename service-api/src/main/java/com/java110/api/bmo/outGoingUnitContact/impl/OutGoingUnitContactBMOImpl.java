package com.java110.api.bmo.outGoingUnitContact.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.outGoingUnitContact.IOutGoingUnitContactBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.outGoingUnitContact.OutGoingUnitContactPo;
import com.java110.utils.constant.BusinessTypeConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * create date 2021/9/10
 */
@Service("outGoingUnitContactBMOImpl")
public class OutGoingUnitContactBMOImpl extends ApiBaseBMO implements IOutGoingUnitContactBMO {

    @Override
    public void addOutGoingUnitContact(JSONObject reqJson, DataFlowContext context) {
        JSONArray businessUnitContactArray = new JSONArray();

        JSONArray contactArray = reqJson.getJSONArray("contact");
        for (int i = 0; i < contactArray.size(); i++) {
            JSONObject oneContactJson = contactArray.getJSONObject(i);

            JSONObject unitContactJson = new JSONObject();
            unitContactJson.fluentPut("contactId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_OUT_GOING_UNIT_CONTACT_ID))
                    .fluentPut("name", oneContactJson.getString("name"))
                    .fluentPut("tel", oneContactJson.getString("tel"))
                    .fluentPut("outId", reqJson.getString("outId"));
            businessUnitContactArray.add(unitContactJson);
        }

        handleBusinessJsonArrayData(context, businessUnitContactArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OUT_GOING_UNIT_CONTACT, OutGoingUnitContactPo.class);

    }
}
