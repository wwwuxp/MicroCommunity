package com.java110.api.bmo.pubCarTripMileage.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarTripMileage.IPubCarTripMileageBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.po.pubCarTripMileage.PubCarTripMileagePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.DateUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * @datetime 2021/7/30 9:24
 */
@Service("pubCarTripMileageBMOImpl")
public class PubCarTripMileageBMOImpl extends ApiBaseBMO implements IPubCarTripMileageBMO {

    @Override
    public void addTripMileage(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessTripMileage = new JSONObject();
        businessTripMileage.put("mileageId", reqJson.getString("mileageId"));
        businessTripMileage.put("tripId", reqJson.getString("tripId"));
        businessTripMileage.put("driverId", reqJson.getString("driverId"));
        businessTripMileage.put("communityId", reqJson.getString("communityId"));
        businessTripMileage.put("startMileage", reqJson.getDouble("currentMileage"));
        businessTripMileage.put("startTime", DateFormatUtils.format(reqJson.getLong("currentTime"), DateUtil.DATE_FORMATE_STRING_A));
        businessTripMileage.put("carCondition", reqJson.getInteger("carCondition"));
        businessTripMileage.put("exceptionDesc", reqJson.getString("exceptionDesc"));
        businessTripMileage.put("carConditionImg", reqJson.getString("carConditionImg"));

        handlePubCarTripMileageDatas(context, businessTripMileage, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_TRIP_MILEAGE);
    }

    @Override
    public void addTripMileageV2(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessTripMileage = new JSONObject();
        businessTripMileage.put("mileageId", reqJson.getString("mileageId"));
        businessTripMileage.put("tripId", reqJson.getString("tripId"));
        businessTripMileage.put("driverId", reqJson.getString("driverId"));
        businessTripMileage.put("communityId", reqJson.getString("communityId"));
        businessTripMileage.put("startMileage", reqJson.getDouble("startMileage"));
        businessTripMileage.put("startTime", DateFormatUtils.format(reqJson.getLong("startTime"), DateUtil.DATE_FORMATE_STRING_A));
        businessTripMileage.put("carCondition", reqJson.getInteger("carCondition"));
        businessTripMileage.put("exceptionDesc", reqJson.getString("exceptionDesc"));
        businessTripMileage.put("carConditionImg", reqJson.getString("carConditionImg"));

        //2022 5.13 hs:整合开始行程和结束行程
        businessTripMileage.put("endMileage", reqJson.getDouble("endMileage"));
        businessTripMileage.put("endTime", DateFormatUtils.format(reqJson.getLong("endTime"), DateUtil.DATE_FORMATE_STRING_A));
        businessTripMileage.put("passMileage", reqJson.getDouble("passMileage"));
        businessTripMileage.put("duration", reqJson.getInteger("duration"));
        businessTripMileage.put("runRange", reqJson.getString("runRange"));
        businessTripMileage.put("repairFee", reqJson.getString("repairFee"));
        businessTripMileage.put("oilFee", reqJson.getString("oilFee"));
        businessTripMileage.put("parkingFee", reqJson.getString("parkingFee"));
        businessTripMileage.put("roadBridgeFee", reqJson.getString("roadBridgeFee"));
        handlePubCarTripMileageDatas(context, businessTripMileage, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_TRIP_MILEAGE);
    }

    @Override
    public void updateTripMileage(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessTripMileage = new JSONObject();
        businessTripMileage.put("tripId", reqJson.getString("tripId"));
        businessTripMileage.put("driverId", reqJson.getString("driverId"));
        businessTripMileage.put("communityId", reqJson.getString("communityId"));
        businessTripMileage.put("endMileage", reqJson.getDouble("currentMileage"));
        businessTripMileage.put("passMileage", reqJson.getDouble("passMileage"));
        businessTripMileage.put("endTime", DateFormatUtils.format(reqJson.getLong("currentTime"), DateUtil.DATE_FORMATE_STRING_A));
        businessTripMileage.put("duration", reqJson.getInteger("duration"));
        businessTripMileage.put("runRange", reqJson.getString("runRange"));
        businessTripMileage.put("repairFee", reqJson.getString("repairFee"));
        businessTripMileage.put("oilFee", reqJson.getString("oilFee"));
        businessTripMileage.put("parkingFee", reqJson.getString("parkingFee"));
        businessTripMileage.put("roadBridgeFee", reqJson.getString("roadBridgeFee"));

        handlePubCarTripMileageDatas(context, businessTripMileage, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_TRIP_MILEAGE);
    }

    @Override
    public void applyDelay(JSONObject reqJson, DataFlowContext context) {
        handlePubCarTripMileageDatas(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_TRIP_MILEAGE);
    }

    /**
     * Json数据统一封装处理
     * @param context context
     * @param param param
     * @param businessType businessType
     */
    private void handlePubCarTripMileageDatas(DataFlowContext context, Object param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessArr = new JSONArray();
        businessArr.add(param);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarTripMileagePo.class.getSimpleName(), businessArr);
        context.addServiceBusiness(business);
    }
}
