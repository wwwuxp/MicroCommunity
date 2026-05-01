package com.java110.front.components.deviceAlarm;

import com.java110.core.context.IPageData;
import com.java110.front.smo.deviceAlarm.IListDeviceAlarmSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询设备告警信息
 * @author hh
 * @date 2021/12/14
 */
@Component("listDeviceAlarm")
public class ListDeviceAlarmComponent {

    @Autowired
    private IListDeviceAlarmSMO listDeviceAlarmSMOImpl;

    /**
     * 查询设备告警信息
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> list(IPageData pd){
        return listDeviceAlarmSMOImpl.listDeviceAlarm(pd);
    }

}
