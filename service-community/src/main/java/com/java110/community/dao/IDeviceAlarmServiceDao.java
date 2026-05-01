package com.java110.community.dao;

import com.java110.dto.deviceAlarm.DeviceAlarmDto;
import com.java110.vo.api.deviceAlarm.ApiDeviceAlarmDataVo;

import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @date 2021/12/14
 */
public interface IDeviceAlarmServiceDao {

    /**
     * 查询设备告警数量
     * @param deviceAlarmDto 请求参数
     * @return int
     */
    int queryDeviceAlarmsCount(DeviceAlarmDto deviceAlarmDto);

    /**
     * 查询设备告警列表
     * @param deviceAlarmDto 请求参数
     * @return list
     */
    List<ApiDeviceAlarmDataVo> queryDeviceAlarms(DeviceAlarmDto deviceAlarmDto);

    /**
     * 根据id集合查询对应的设备列表
     * @param deviceIdList 设备id列表
     * @return list
     */
    List<Map<String, String>> queryDeviceInfoByIds(List<String> deviceIdList);

    /**
     * 确认设备告警
     * @param deviceAlarmDto 请求参数
     * @return int
     */
    int confirmDeviceAlarm(DeviceAlarmDto deviceAlarmDto);

}
