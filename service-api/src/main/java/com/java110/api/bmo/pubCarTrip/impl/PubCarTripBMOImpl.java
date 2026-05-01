package com.java110.api.bmo.pubCarTrip.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarTrip.IPubCarTripBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.pubCarTrip.PubCarTripPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.DateUtil;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

/**
 * @author hh
 * @datetime 2021/7/26 14:53
 */
@Service("pubCarTripBMOImpl")
public class PubCarTripBMOImpl extends ApiBaseBMO implements IPubCarTripBMO {

    @Override
    public void addPubCarTrip(JSONObject paramInJson, DataFlowContext context) {
        JSONArray businessTripArray = new JSONArray();

        JSONArray tripArray = paramInJson.getJSONArray("trip");
        StringJoiner sj = new StringJoiner(",");
        for (int i = 0; i < tripArray.size(); i++) {
            JSONObject oneBusinessTrip = tripArray.getJSONObject(i);
            JSONObject businessTripObject = new JSONObject();

            String tripId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_ID);
            businessTripObject.put("tripId", tripId);
            oneBusinessTrip.put("tripId", tripId);
            sj.add(tripId);
            businessTripObject.put("userId", paramInJson.getString("userId"));
            businessTripObject.put("communityId", paramInJson.getString("communityId"));

            businessTripObject.put("carType", oneBusinessTrip.getString("carType"));
            businessTripObject.put("carTypeDetail", oneBusinessTrip.getInteger("carTypeDetail"));
            businessTripObject.put("start", oneBusinessTrip.getString("start"));
            businessTripObject.put("destination", oneBusinessTrip.getString("destination"));
            businessTripObject.put("mileage", oneBusinessTrip.getDouble("mileage"));
            businessTripObject.put("startTime", oneBusinessTrip.getString("startTime"));
            businessTripObject.put("endTime", oneBusinessTrip.getString("endTime"));
            businessTripObject.put("applyNum", oneBusinessTrip.getInteger("applyNum"));
            businessTripObject.put("applyReason", paramInJson.getString("applyReason"));
            businessTripObject.put("isUrgent", paramInJson.getInteger("isUrgent"));
            businessTripObject.put("isDaily", paramInJson.getInteger("isDaily"));
            businessTripObject.put("dailyPeriod", paramInJson.getInteger("dailyPeriod"));

            businessTripArray.add(businessTripObject);

            // 将申请用车的id放入redis缓存中
            toRedis(oneBusinessTrip, tripId);
        }

        addPubCarTripDatas(context, businessTripArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_TRIP);

    }

    @Override
    public void updatePubCarTrip(JSONObject reqJson, DataFlowContext context) {

        updatePubCarTripDatas(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_TRIP);

    }

    @Override
    public void updateTripStatus(JSONObject reqJson, DataFlowContext context, Integer status) {
        JSONObject businessTrip = new JSONObject();
        businessTrip.put("tripStatus", status);
        businessTrip.put("tripId", reqJson.getString("tripId"));
        businessTrip.put("driverId", reqJson.getString("driverId"));
        businessTrip.put("communityId", reqJson.getString("communityId"));
        updatePubCarTripDatas(context, businessTrip, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_TRIP);
    }


    /**
     * Json数据统一封装处理
     * @param context context
     * @param param param
     * @param businessType businessType
     */
    private void addPubCarTripDatas(DataFlowContext context, JSONArray param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarTripPo.class.getSimpleName(), param);
        context.addServiceBusiness(business);
    }

    private void updatePubCarTripDatas(DataFlowContext context, Object param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessArr = new JSONArray();
        businessArr.add(param);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarTripPo.class.getSimpleName(), businessArr);
        context.addServiceBusiness(business);
    }

    /**
     * 存入redis中
     * @param oneBusinessTrip oneBusinessTrip
     * @param tripId tripId
     */
    private void toRedis(JSONObject oneBusinessTrip, String tripId) {
        LocalDateTime tempNow = LocalDateTime.now();
        LocalDateTime now = tempNow.withSecond(0).withNano(0);
        LocalDateTime startTime = LocalDateTime.parse(oneBusinessTrip.getString("startTime"), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
        LocalDateTime endTime = LocalDateTime.parse(oneBusinessTrip.getString("endTime"), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
        if (startTime.isBefore(now)){
            throw new IllegalArgumentException("开始时间已经过去，请选择一个未来时间");
        }
        if (endTime.isBefore(startTime)){
            throw new IllegalArgumentException("结束时间不能在开始时间之前");
        }
        // 保留到申请开始时间
        Duration duration = Duration.between(now, startTime);
        CommonCache.setValue(CommonConstant.APPLY_PUBCAR_START + tripId, oneBusinessTrip.getString("startTime"), (int) duration.getSeconds());
        // 保留到距申请结束时间24小时（钥匙未归还。离用车申请结束时间超过24小时，钥匙仍未归还。）
        LocalDateTime after24Hour = endTime.plusHours(25);
        Duration after24HourDuration = Duration.between(endTime, after24Hour);
        CommonCache.setValue(CommonConstant.APPLY_PUBCAR_END24 + tripId, oneBusinessTrip.getString("endTime"), (int) after24HourDuration.getSeconds());
        // 保留到距申请结束时间12小时（钥匙超时归还。车钥匙已经归还到钥匙柜，但是归还时间超过用车申请结束时间12个小时以上。）
        LocalDateTime after12Hour = endTime.plusHours(13);
        Duration after12HourDuration = Duration.between(endTime, after12Hour);
        CommonCache.setValue(CommonConstant.APPLY_PUBCAR_END12 + tripId, oneBusinessTrip.getString("endTime"), (int) after12HourDuration.getSeconds());
        // 保留到距申请时间2小时（超时领取钥匙（中）。车钥匙领取时间比申请时间晚2小时以上。）
        LocalDateTime after2Hour = startTime.plusHours(3);
        Duration after2HourDuration = Duration.between(startTime, after2Hour);
        CommonCache.setValue(CommonConstant.APPLY_PUBCAR_START2 + tripId, oneBusinessTrip.getString("startTime"), (int) after2HourDuration.getSeconds());
    }

}
