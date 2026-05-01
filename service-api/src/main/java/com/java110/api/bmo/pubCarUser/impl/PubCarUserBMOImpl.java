package com.java110.api.bmo.pubCarUser.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarUser.IPubCarUserBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.pubCarUser.PubCarUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * @datetime 2021/7/24 10:01
 */
@Service("pubCarUserBMOImpl")
public class PubCarUserBMOImpl extends ApiBaseBMO implements IPubCarUserBMO {

    @Override
    public void addPubCarUser(JSONObject paramInJson, DataFlowContext context) {
        JSONArray businessArray = new JSONArray();
        JSONArray userIds = paramInJson.getJSONArray("userIds");
        for (Object userIdObj : userIds) {
            JSONObject businessJson = new JSONObject();
            businessJson.put("carUserId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_CAR_USER_ID));
            businessJson.put("userId", userIdObj);
            businessJson.put("isSelfDriving", paramInJson.getInteger("isSelfDriving"));
            businessJson.put("communityId", paramInJson.getString("communityId"));
            businessArray.add(businessJson);
        }

        insertPubCarUserDatas(context, businessArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_USER);

    }

    @Override
    public void updatePubCarUser(JSONObject paramInJson, DataFlowContext context) {
        handlePubCarUserDatas(context, paramInJson, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_USER);
    }

    @Override
    public void deletePubCarUser(JSONObject paramInJson, DataFlowContext context) {
        handlePubCarUserDatas(context, paramInJson, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_USER);
    }

    private void handlePubCarUserDatas(DataFlowContext context, JSONObject param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessArr = new JSONArray();
        businessArr.add(param);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarUserPo.class.getSimpleName(), businessArr);
        context.addServiceBusiness(business);
    }

    private void insertPubCarUserDatas(DataFlowContext context, JSONArray param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarUserPo.class.getSimpleName(), param);
        context.addServiceBusiness(business);
    }
}
