package com.java110.vo.api.pubCar;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiCarStatisticsVo extends MorePageVo implements Serializable {

    List<ApiCarStatisticsDataVo> data;

    public List<ApiCarStatisticsDataVo> getData() {
        return data;
    }

    public void setData(List<ApiCarStatisticsDataVo> data) {
        this.data = data;
    }
}
