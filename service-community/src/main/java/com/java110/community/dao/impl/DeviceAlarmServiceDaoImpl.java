package com.java110.community.dao.impl;

import com.java110.community.dao.IDeviceAlarmServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.deviceAlarm.DeviceAlarmDto;
import com.java110.vo.api.deviceAlarm.ApiDeviceAlarmDataVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @date 2021/12/14
 */
@Service("deviceAlarmServiceDaoImpl")
public class DeviceAlarmServiceDaoImpl extends BaseServiceDao implements IDeviceAlarmServiceDao {

    @Override
    public int queryDeviceAlarmsCount(DeviceAlarmDto deviceAlarmDto) {
        return sqlSessionTemplate.selectOne("deviceAlarmServiceDaoImpl.queryDeviceAlarmsCount", deviceAlarmDto);
    }

    @Override
    public List<ApiDeviceAlarmDataVo> queryDeviceAlarms(DeviceAlarmDto deviceAlarmDto) {
        return sqlSessionTemplate.selectList("deviceAlarmServiceDaoImpl.queryDeviceAlarms", deviceAlarmDto);
    }

    @Override
    public List<Map<String, String>> queryDeviceInfoByIds(List<String> deviceIdList) {
        return sqlSessionTemplate.selectList("deviceAlarmServiceDaoImpl.queryDeviceInfoByIds", deviceIdList);
    }

    @Override
    public int confirmDeviceAlarm(DeviceAlarmDto deviceAlarmDto) {
        return sqlSessionTemplate.update("deviceAlarmServiceDaoImpl.confirmDeviceAlarm", deviceAlarmDto);
    }
}
