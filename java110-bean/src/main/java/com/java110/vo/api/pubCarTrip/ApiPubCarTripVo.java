package com.java110.vo.api.pubCarTrip;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarTripVo extends MorePageVo implements Serializable {

    List<ApiPubCarTripDataVo> data;

    public List<ApiPubCarTripDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarTripDataVo> data) {
        this.data = data;
    }
}
