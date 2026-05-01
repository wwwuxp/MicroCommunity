package com.java110.api.bmo.outGoingPersonnel.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.outGoingPersonnel.IOutGoingPersonnelBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.outGoing.OutGoingPo;
import com.java110.po.outGoingPersonnel.OutGoingPersonnelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * create date 2021/9/10
 */
@Service("outGoingPersonnelBMOImpl")
public class OutGoingPersonnelBMOImpl extends ApiBaseBMO implements IOutGoingPersonnelBMO {

    @Override
    public void addOutGoingPersonnel(JSONObject reqJson, DataFlowContext context) {
        JSONArray businessPersonnelArray = new JSONArray();

        JSONArray personnelArray = reqJson.getJSONArray("personnel");
        for (int i = 0; i < personnelArray.size(); i++) {
            // 外出人员的id
            String userId = personnelArray.getString(i);

            JSONObject personnelJson = new JSONObject();
            personnelJson.fluentPut("personnelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_OUT_GOING_PERSONNEL_ID))
                    .fluentPut("userId", userId)
                    .fluentPut("outId", reqJson.getString("outId"));

            businessPersonnelArray.add(personnelJson);
        }
        handleBusinessJsonArrayData(context, businessPersonnelArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OUT_GOING_PERSONNEL, OutGoingPersonnelPo.class);
    }

}
