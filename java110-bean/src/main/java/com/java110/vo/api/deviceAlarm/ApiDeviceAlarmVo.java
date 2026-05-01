package com.java110.vo.api.deviceAlarm;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/12/14
 */
public class ApiDeviceAlarmVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -2032584248566887534L;

    List<ApiDeviceAlarmDataVo> data;

    public List<ApiDeviceAlarmDataVo> getData() {
        return data;
    }

    public void setData(List<ApiDeviceAlarmDataVo> data) {
        this.data = data;
    }
}
