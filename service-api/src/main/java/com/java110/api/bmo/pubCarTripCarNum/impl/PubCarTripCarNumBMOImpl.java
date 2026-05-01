package com.java110.api.bmo.pubCarTripCarNum.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarTripCarNum.IPubCarTripCarNumBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.pubCarTripCarNum.PubCarTripCarNumPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author hh
 * @datetime 2021/7/26 15:02
 */
@Service("pubCarTripCarNumBMOImpl")
public class PubCarTripCarNumBMOImpl extends ApiBaseBMO implements IPubCarTripCarNumBMO {

    @Override
    public void addPubCarTripCarNum(JSONObject reqJson, DataFlowContext context) {
        JSONArray businessCarNumArray = new JSONArray();

        JSONArray tripArray = reqJson.getJSONArray("trip");
        String userId = reqJson.getString("userId");

        for (int i = 0; i < tripArray.size(); i++) {
            JSONObject oneTripObject = tripArray.getJSONObject(i);

            JSONArray carPassengerInfoArray = oneTripObject.getJSONArray("carPassengerInfo");

            for (int j = 0; j < carPassengerInfoArray.size(); j++) {
                JSONObject oneCarNumObject = carPassengerInfoArray.getJSONObject(j);
                oneCarNumObject.put("numId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_CAR_NUM));
                oneCarNumObject.put("tripId", oneTripObject.getString("tripId"));

                JSONArray passengerArray = oneCarNumObject.getJSONArray("passenger");

                boolean flag = false;
                for (int k = 0; k < passengerArray.size(); k++) {
                    JSONObject passengerInfoJson = passengerArray.getJSONObject(k);
                    String passengerId = passengerInfoJson.getString("userId");

                    // 计算是否是本人用车,如果当前申请人的userId和乘客的userId吻合,则标记为是本人乘车
                    if (Objects.equals(userId, passengerId)) {
                        oneCarNumObject.put("isSelf", 1);
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    oneCarNumObject.put("isSelf", 0);
                }

                businessCarNumArray.add(oneCarNumObject);
            }
        }

        handleCarNumDatas(context, businessCarNumArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_TRIP_CAR_NUM);

    }

    @Override
    public void updatePubCarTripCarNum(JSONObject reqJson, DataFlowContext context) {

        JSONArray infoArray = reqJson.getJSONArray("info");
        for (int i = 0; i < infoArray.size(); i++) {
            JSONObject oneApprovalObject = infoArray.getJSONObject(i);
            oneApprovalObject.put("tripId", reqJson.getString("tripId"));
            /*String driverId = oneApprovalObject.getString("driverId");
            if (StringUtils.isBlank(driverId) || "-1".equals(driverId)){
                oneApprovalObject.put("driverId", context.getUserId());
            }*/
        }

        handleCarNumDatas(context, infoArray, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_TRIP_CAR_NUM);

    }

    /**
     * Json数据统一封装处理
     *
     * @param context      context
     * @param param        param
     * @param businessType businessType
     */
    private void handleCarNumDatas(DataFlowContext context, JSONArray param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarTripCarNumPo.class.getSimpleName(), param);
        context.addServiceBusiness(business);
    }

}
