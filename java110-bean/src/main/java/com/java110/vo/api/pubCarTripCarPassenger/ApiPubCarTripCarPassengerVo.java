package com.java110.vo.api.pubCarTripCarPassenger;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarTripCarPassengerVo extends MorePageVo implements Serializable {

    List<ApiPubCarTripCarPassengerDataVo> data;

    public List<ApiPubCarTripCarPassengerDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarTripCarPassengerDataVo> data) {
        this.data = data;
    }
}
