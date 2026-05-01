package com.java110.api.bmo.pubCarDriver.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarDriver.IPubCarDriverBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.pubCarDriver.PubCarDriverPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * @datetime 2021/7/22 17:16
 */
@Service("pubCarDriverBMOImpl")
public class PubCarDriverBMOImpl extends ApiBaseBMO implements IPubCarDriverBMO {


    @Override
    public void addPubCarDriver(JSONObject paramInJson, DataFlowContext context) {

        paramInJson.put("driverId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_DRIVER_ID));

        handlePubCarDriverDatas(context, paramInJson, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_DRIVER);
    }

    @Override
    public void updatePubCarDriver(JSONObject paramInJson, DataFlowContext context) {

        handlePubCarDriverDatas(context, paramInJson, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_DRIVER);
    }

    @Override
    public void deletePubCarDriver(JSONObject paramInJson, DataFlowContext context) {
        handlePubCarDriverDatas(context, paramInJson, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_DRIVER);
    }


    private void handlePubCarDriverDatas(DataFlowContext context, JSONObject param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessArr = new JSONArray();
        businessArr.add(param);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarDriverPo.class.getSimpleName(), businessArr);
        context.addServiceBusiness(business);
    }


}
