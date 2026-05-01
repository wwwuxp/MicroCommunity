package com.java110.api.listener.deviceAlarm;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.deviceAlarm.DeviceAlarmDto;
import com.java110.intf.community.IDeviceAlarmInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeDeviceAlarmConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

/**
 * 确认设备告警
 * @author hh
 * @date 2021/12/14
 */
@Java110Listener("confirmDeviceAlarmListener")
public class ConfirmDeviceAlarmListener extends AbstractServiceApiPlusListener{

    @Autowired
    private IDeviceAlarmInnerServiceSMO deviceAlarmInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        Assert.hasKeyAndValue(reqJson, "id", "id不能为空");
        Assert.hasKeyAndValue(reqJson, "handleState", "handleState不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        DeviceAlarmDto deviceAlarmDto = BeanConvertUtil.covertBean(reqJson, DeviceAlarmDto.class);
        int updateFlag = deviceAlarmInnerServiceSMOImpl.confirmDeviceAlarm(deviceAlarmDto);
        if (updateFlag < 1){
            throw new RuntimeException("确认失败");
        }
        ResponseEntity<String> success = ResultVo.success();
        context.setResponseEntity(success);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeDeviceAlarmConstant.CONFIRM_DEVICE_ALARM;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
