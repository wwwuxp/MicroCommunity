package com.java110.front.smo.deviceAlarm;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/12/14
 */
public interface IConfirmDeviceAlarmSMO {

    /**
     * 确认设备告警
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> confirmDeviceAlarm(IPageData pd);

}
