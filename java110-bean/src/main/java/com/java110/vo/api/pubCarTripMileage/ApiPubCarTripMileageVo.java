package com.java110.vo.api.pubCarTripMileage;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarTripMileageVo extends MorePageVo implements Serializable {

    List<ApiPubCarTripMileageDataVo> data;

    public List<ApiPubCarTripMileageDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarTripMileageDataVo> data) {
        this.data = data;
    }
}
