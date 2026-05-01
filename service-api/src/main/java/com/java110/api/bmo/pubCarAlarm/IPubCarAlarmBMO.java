package com.java110.api.bmo.pubCarAlarm;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;

/**
 * @author hh
 * @datetime 2021/8/6 18:20
 */
public interface IPubCarAlarmBMO extends IApiBaseBMO {

    void addPubCarAlarm(Integer alarmNameCode, String gradeCode, String detail, String driverName, PubCarTripMileageDto driverTripMileage, DataFlowContext context);

    void updatePubCarAlarm(JSONObject reqJson, DataFlowContext context);

}
