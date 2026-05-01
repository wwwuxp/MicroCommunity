package com.java110.api.bmo.PubCarTripCarPassenger.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.PubCarTripCarPassenger.IPubCarTripCarPassengerBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.pubCarTripCarPassenger.PubCarTripCarPassengerPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * @datetime 2021/7/26 15:04
 */
@Service("pubCarTripCarPassengerBMOImpl")
public class PubCarTripCarPassengerBMOImpl extends ApiBaseBMO implements IPubCarTripCarPassengerBMO {

    @Override
    public void addPubCarTripCarPassenger(JSONObject reqJson, DataFlowContext context) {
        JSONArray businessCarPassengerArray = new JSONArray();

        JSONArray tripArray = reqJson.getJSONArray("trip");
        for (int i = 0; i < tripArray.size(); i++) {
            JSONObject oneTripObject = tripArray.getJSONObject(i);

            JSONArray carPassengerInfoArray = oneTripObject.getJSONArray("carPassengerInfo");
            for (int j = 0; j < carPassengerInfoArray.size(); j++) {
                JSONObject onePassengerInfo = carPassengerInfoArray.getJSONObject(j);

                JSONArray passengerArray = onePassengerInfo.getJSONArray("passenger");
                for (int k = 0; k < passengerArray.size(); k++) {
                    JSONObject passengerObject = passengerArray.getJSONObject(k);
                    JSONObject businessCarPassenger = new JSONObject();
                    businessCarPassenger.put("passengerId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_CAR_PASSENGER));
                    businessCarPassenger.put("numId", onePassengerInfo.getString("numId"));
                    businessCarPassenger.put("userId", passengerObject.getString("userId"));
                    businessCarPassenger.put("passengerName", passengerObject.getString("name"));
                    businessCarPassenger.put("passengerDepartment", passengerObject.getString("department"));
                    businessCarPassenger.put("passengerPosition", passengerObject.getString("position"));
                    businessCarPassengerArray.add(businessCarPassenger);
                }
            }
        }

        handleCarPassengerDatas(context, businessCarPassengerArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_TRIP_CAR_PASSENGER);

    }

    /**
     * Json数据统一封装处理
     * @param context context
     * @param param param
     * @param businessType businessType
     */
    private void handleCarPassengerDatas(DataFlowContext context, JSONArray param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarTripCarPassengerPo.class.getSimpleName(), param);
        context.addServiceBusiness(business);
    }

}
