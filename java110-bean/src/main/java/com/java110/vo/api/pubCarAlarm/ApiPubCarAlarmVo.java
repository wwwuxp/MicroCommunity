package com.java110.vo.api.pubCarAlarm;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarAlarmVo extends MorePageVo implements Serializable {

    List<ApiPubCarAlarmDataVo> data;

    public List<ApiPubCarAlarmDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarAlarmDataVo> data) {
        this.data = data;
    }
}
