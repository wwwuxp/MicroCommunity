package com.java110.front.smo.deviceAlarm;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/12/14
 */
public interface IListDeviceAlarmSMO {

    /**
     * 查询设备告警信息
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> listDeviceAlarm(IPageData pd);

}
