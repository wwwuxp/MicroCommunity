package com.java110.api.bmo.pubCarAlarm.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarAlarm.IPubCarAlarmBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * @datetime 2021/8/6 18:20
 */
@Service("pubCarAlarmBMOImpl")
public class PubCarAlarmBMOImpl extends ApiBaseBMO implements IPubCarAlarmBMO {

    @Override
    public void addPubCarAlarm(Integer alarmNameCode, String gradeCode, String detail, String driverName, PubCarTripMileageDto driverTripMileage, DataFlowContext context) {
        JSONObject businessAlarm = new JSONObject();
        businessAlarm.put("alarmId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ALARM_ID));
        businessAlarm.put("name", alarmNameCode);
        businessAlarm.put("grade", gradeCode);
        businessAlarm.put("driverId", driverTripMileage.getDriverId());
        businessAlarm.put("driverName", driverName);
        businessAlarm.put("tripId", driverTripMileage.getTripId());
        businessAlarm.put("detail", detail);
        businessAlarm.put("communityId", driverTripMileage.getCommunityId());

        handlePubCarAlarmDatas(context, businessAlarm, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_ALARM);
    }

    @Override
    public void updatePubCarAlarm(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessAlarm = new JSONObject();
        businessAlarm.put("alarmId", reqJson.getString("alarmId"));
        businessAlarm.put("resultProcess", reqJson.getString("resultProcess"));
        businessAlarm.put("remark", reqJson.getString("remark"));
        businessAlarm.put("communityId", reqJson.getString("communityId"));
        handlePubCarAlarmDatas(context, businessAlarm, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_ALARM);

    }

    private void handlePubCarAlarmDatas(DataFlowContext context, JSONObject param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessArr = new JSONArray();
        businessArr.add(param);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(PubCarAlarmPo.class.getSimpleName(), businessArr);
        context.addServiceBusiness(business);
    }



}
