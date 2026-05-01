package com.java110.community.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IDeviceAlarmServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.deviceAlarm.DeviceAlarmDto;
import com.java110.intf.community.IDeviceAlarmInnerServiceSMO;
import com.java110.vo.api.deviceAlarm.ApiDeviceAlarmDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hh
 * @date 2021/12/14
 */
@RestController
public class DeviceAlarmInnerServiceSMOImpl extends BaseServiceSMO implements IDeviceAlarmInnerServiceSMO {

    @Autowired
    private IDeviceAlarmServiceDao deviceAlarmServiceDaoImpl;

    @Override
    public int queryDeviceAlarmsCount(@RequestBody DeviceAlarmDto deviceAlarmDto) {
        return deviceAlarmServiceDaoImpl.queryDeviceAlarmsCount(deviceAlarmDto);
    }

    @Override
    public List<ApiDeviceAlarmDataVo> queryDeviceAlarms(@RequestBody DeviceAlarmDto deviceAlarmDto) {
        // 检查分页信息
        int page = deviceAlarmDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            deviceAlarmDto.setPage((page - 1) * deviceAlarmDto.getRow());
        }
        // 将设备id从additionalInfo字段中摘出来
        List<ApiDeviceAlarmDataVo> deviceAlarmDataVos = deviceAlarmServiceDaoImpl.queryDeviceAlarms(deviceAlarmDto);
        List<String> deviceIdList = new ArrayList<>();
        for (ApiDeviceAlarmDataVo deviceAlarmDataVo : deviceAlarmDataVos) {
            String additionalInfo = deviceAlarmDataVo.getAdditionalInfo();
            JSONObject additionalInfoJson = JSONObject.parseObject(additionalInfo);
            JSONArray dataArray = additionalInfoJson.getJSONArray("data");
            if (CollectionUtils.isEmpty(dataArray)){
                continue;
            }
            List<String> alarmDeviceIds = new ArrayList<>();
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject oneData = dataArray.getJSONObject(i);
                String deviceId = oneData.getString("deviceId");
                if (StringUtils.isBlank(deviceId)){
                    continue;
                }
                alarmDeviceIds.add(deviceId);
            }
            deviceAlarmDataVo.setDeviceIds(alarmDeviceIds);
            deviceIdList.addAll(alarmDeviceIds);

            // 处理告警等级
            String level = deviceAlarmDataVo.getLevel();
            switch (level){
                case "CRITICAL": deviceAlarmDataVo.setLevel("严重");break;
                case "MAJOR": deviceAlarmDataVo.setLevel("较严重");break;
                case "MINOR": deviceAlarmDataVo.setLevel("轻微");break;
                case "WARNING": deviceAlarmDataVo.setLevel("警告");break;
                case "INDETERMINATE": deviceAlarmDataVo.setLevel("不确定");break;
                default:
            }

            // 处理时间
            String time = deviceAlarmDataVo.getTime();
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(time)), TimeZone.getDefault().toZoneId());
            LocalDate localDate = dateTime.toLocalDate();
            deviceAlarmDataVo.setTime(localDate.toString());

            // 处理类型
            String type = deviceAlarmDataVo.getType();
            switch (type){
                case "DEFAULT": deviceAlarmDataVo.setType("默认"); break;
                case "OFFLINE": deviceAlarmDataVo.setType("离线"); break;
                case "EVENT": deviceAlarmDataVo.setType("事件"); break;
                default:
            }
            deviceAlarmDataVo.setAdditionalInfo(null);
        }

        // 根据设备id查询出对应的设备
        if (CollectionUtils.isNotEmpty(deviceIdList)){
            List<Map<String, String>> deviceInfoMapList = deviceAlarmServiceDaoImpl.queryDeviceInfoByIds(deviceIdList);
            // List转Map
            Map<String, String> deviceIdAndNameMap = deviceInfoMapList.stream().collect(Collectors.toMap(t -> t.get("id"), t -> t.get("name"), (key1, key2) -> key2));
            for (ApiDeviceAlarmDataVo deviceAlarmDataVo : deviceAlarmDataVos) {
                StringJoiner sj = new StringJoiner(",");
                List<String> deviceIds = deviceAlarmDataVo.getDeviceIds();
                for (String deviceId : deviceIds) {
                    String deviceName = deviceIdAndNameMap.get(deviceId);
                    if (StringUtils.isNotBlank(deviceName)){
                        sj.add(deviceName);
                    }
                }
                deviceAlarmDataVo.setDevice(sj.toString());
                deviceAlarmDataVo.setDeviceIds(null);
            }
        }
        return deviceAlarmDataVos;
    }

    @Override
    public int confirmDeviceAlarm(@RequestBody DeviceAlarmDto deviceAlarmDto) {
        return deviceAlarmServiceDaoImpl.confirmDeviceAlarm(deviceAlarmDto);
    }
}
