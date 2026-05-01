package com.java110.front.components.deviceAlarm;

import com.java110.core.context.IPageData;
import com.java110.front.smo.deviceAlarm.IConfirmDeviceAlarmSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2021/12/14
 */
@Component("confirmDeviceAlarm")
public class ConfirmDeviceAlarmComponent {

    @Autowired
    private IConfirmDeviceAlarmSMO confirmDeviceAlarmSMOImpl;

    /**
     * 确认设备告警
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> confirm(IPageData pd){
        return confirmDeviceAlarmSMOImpl.confirmDeviceAlarm(pd);
    }

}
