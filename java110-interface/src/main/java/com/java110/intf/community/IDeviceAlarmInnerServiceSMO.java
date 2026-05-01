package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.deviceAlarm.DeviceAlarmDto;
import com.java110.vo.api.deviceAlarm.ApiDeviceAlarmDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author hh
 * @date 2021/12/14
 */
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/deviceAlarmApi")
public interface IDeviceAlarmInnerServiceSMO {

    /**
     * 查询设备告警数量
     * @param deviceAlarmDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/queryDeviceAlarmsCount", method = RequestMethod.POST)
    int queryDeviceAlarmsCount(@RequestBody DeviceAlarmDto deviceAlarmDto);

    /**
     * 查询设备告警信息
     * @param deviceAlarmDto 请求参数
     * @return list
     */
    @RequestMapping(value = "/queryDeviceAlarms", method = RequestMethod.POST)
    List<ApiDeviceAlarmDataVo> queryDeviceAlarms(@RequestBody DeviceAlarmDto deviceAlarmDto);

    /**
     * 确认设备告警
     * @param deviceAlarmDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/confirmDeviceAlarm", method = RequestMethod.POST)
    int confirmDeviceAlarm(@RequestBody DeviceAlarmDto deviceAlarmDto);

}
