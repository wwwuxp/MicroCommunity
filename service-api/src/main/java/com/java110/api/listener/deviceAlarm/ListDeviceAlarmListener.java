package com.java110.api.listener.deviceAlarm;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.deviceAlarm.DeviceAlarmDto;
import com.java110.intf.community.IDeviceAlarmInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeDeviceAlarmConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.deviceAlarm.ApiDeviceAlarmDataVo;
import com.java110.vo.api.deviceAlarm.ApiDeviceAlarmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询设备告警信息
 * @author hh
 * @date 2021/12/14
 */
@Java110Listener("listDeviceAlarmListener")
public class ListDeviceAlarmListener extends AbstractServiceApiListener {

    @Autowired
    private IDeviceAlarmInnerServiceSMO deviceAlarmInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        Assert.hasKeyAndValue(reqJson, "handleState", "处理状态不能为空");
        validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        DeviceAlarmDto deviceAlarmDto = BeanConvertUtil.covertBean(reqJson, DeviceAlarmDto.class);

        int count = deviceAlarmInnerServiceSMOImpl.queryDeviceAlarmsCount(deviceAlarmDto);

        List<ApiDeviceAlarmDataVo> deviceAlarmDataVos;

        if (count > 0) {
            deviceAlarmDataVos = deviceAlarmInnerServiceSMOImpl.queryDeviceAlarms(deviceAlarmDto);
        } else {
            deviceAlarmDataVos = Collections.emptyList();
        }

        ApiDeviceAlarmVo apiDeviceAlarmVo = new ApiDeviceAlarmVo();

        apiDeviceAlarmVo.setTotal(count);
        apiDeviceAlarmVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiDeviceAlarmVo.setData(deviceAlarmDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiDeviceAlarmVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeDeviceAlarmConstant.LIST_DEVICE_ALARM;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
