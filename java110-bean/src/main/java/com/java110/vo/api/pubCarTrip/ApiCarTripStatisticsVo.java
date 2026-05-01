package com.java110.vo.api.pubCarTrip;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiCarTripStatisticsVo extends MorePageVo implements Serializable {

    List<ApiCarTripStatisticsDataVo> data;

    public List<ApiCarTripStatisticsDataVo> getData() {
        return data;
    }

    public void setData(List<ApiCarTripStatisticsDataVo> data) {
        this.data = data;
    }
}
