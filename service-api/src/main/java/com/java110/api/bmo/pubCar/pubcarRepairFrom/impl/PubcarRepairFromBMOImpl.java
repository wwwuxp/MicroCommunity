package com.java110.api.bmo.pubCar.pubcarRepairFrom.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCar.pubcarRepairFrom.IPubcarRepairFromBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.pubcar.BusinessPubcarRepairForm;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author lmf
 * @datetime 2022-07-27
 */
@Service("pubcarRepairFromBMOImpl")
public class PubcarRepairFromBMOImpl extends ApiBaseBMO implements IPubcarRepairFromBMO {

    @Override
    public void saveOrEditPubcarRepairFrom(JSONObject reqJson, DataFlowContext context) {
        String businessType;
        String repairId = reqJson.getString("repairId");
        if(StringUtils.isEmpty(repairId)){
            reqJson.put("repairId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.REPAIR_MANAGEMENT_DATA_ID));
            businessType = BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_REPAIR_FROM;
        }else{
            businessType = BusinessTypeConstant.BUSINESS_TYPE_EDIT_PUBCAR_REPAIR_FROM;
        }
        jsonPubcarRepairFrom(context, reqJson, businessType);
    }

    @Override
    public void deletePubcarRepairFrom(JSONObject reqJson, DataFlowContext context) {
        jsonPubcarRepairFrom(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_REPAIR_FROM);
    }


    /**
     * Json数据统一封装处理
     * @param context context
     * @param param param
     * @param businessType businessType
     */
    private void jsonPubcarRepairFrom(DataFlowContext context, Object param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(BusinessPubcarRepairForm.class.getSimpleName(), param);
        context.addServiceBusiness(business);
    }


}
