package com.java110.api.bmo.pubCarRepair.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarRepair.IPubCarRepairBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.pubCarRepair.PubCarRepairPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * @datetime 2021/8/6 9:24
 */
@Service("pubCarRepairBMOImpl")
public class PubCarRepairBMOImpl extends ApiBaseBMO implements IPubCarRepairBMO {

    @Override
    public void addPubCarRepair(JSONObject reqJson, DataFlowContext context) {
        reqJson.put("repairId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_REPAIR_ID));
        handlePubCarRepairDatas(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_REPAIR);
    }

    private void handlePubCarRepairDatas(DataFlowContext context, JSONObject param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessArr = new JSONArray();
        businessArr.add(param);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarRepairPo.class.getSimpleName(), businessArr);
        context.addServiceBusiness(business);
    }


}
