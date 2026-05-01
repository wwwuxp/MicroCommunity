package com.java110.vo.api.pubCarTrip;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiSimpleTripVo extends MorePageVo implements Serializable {

    List<ApiSimpleTripDataVo> data;

    public List<ApiSimpleTripDataVo> getData() {
        return data;
    }

    public void setData(List<ApiSimpleTripDataVo> data) {
        this.data = data;
    }
}
