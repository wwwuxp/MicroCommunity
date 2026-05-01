package com.java110.api.bmo.pubCarAccident.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarAccident.IPubCarAccidentBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.po.pubCarAccident.PubCarAccidentPo;
import com.java110.utils.constant.BusinessTypeConstant;
import org.springframework.stereotype.Service;

/**
 * 用车事故业务处理
 * @author hh
 * @date 2021/10/9
 */
@Service("pubCarAccidentBMOImpl")
public class PubCarAccidentBMOImpl extends ApiBaseBMO implements IPubCarAccidentBMO {

    @Override
    public void addPubCarAccident(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessJson = new JSONObject();
        businessJson.put("accidentId", reqJson.getString("accidentId"));
        businessJson.put("tripId", reqJson.getString("tripId"));
        businessJson.put("accidentSceneImg", reqJson.getString("accidentSceneImg"));
        businessJson.put("responsibilityConfirmImg", reqJson.getString("responsibilityConfirmImg"));
        businessJson.put("accidentLocation", reqJson.getString("accidentLocation"));
        businessJson.put("detail", reqJson.getString("detail"));
        businessJson.put("communityId", reqJson.getString("communityId"));
        handleBusinessJsonObjectData(context, businessJson, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_ACCIDENT, PubCarAccidentPo.class);
    }

}
