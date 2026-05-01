package com.java110.api.bmo.pubCarGroup.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarGroup.IPubCarGroupBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.pubCarGroup.PubCarGroupPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * @datetime 2021/8/10 17:27
 */
@Service("pubCarGroupBMOImpl")
public class PubCarGroupBMOImpl extends ApiBaseBMO implements IPubCarGroupBMO {

    @Override
    public void addPubCarGroup(JSONObject reqJson, DataFlowContext context) {
        reqJson.put("groupId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_GROUP_ID));
        handlePubCarGroupDatas(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_GROUP);
    }

    @Override
    public void updatePubCarGroup(JSONObject reqJson, DataFlowContext context) {
        handlePubCarGroupDatas(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_GROUP);
    }

    @Override
    public void deletePubCarGroup(JSONObject reqJson, DataFlowContext context) {
        handlePubCarGroupDatas(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_GROUP);
    }

    private void handlePubCarGroupDatas(DataFlowContext context, JSONObject param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessArr = new JSONArray();
        businessArr.add(param);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarGroupPo.class.getSimpleName(), businessArr);
        context.addServiceBusiness(business);
    }

}
