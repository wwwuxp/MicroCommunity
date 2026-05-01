package com.java110.vo.api.pubCarTripCarNum;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarTripCarNumVo extends MorePageVo implements Serializable {

    List<ApiPubCarTripCarNumDataVo> data;

    public List<ApiPubCarTripCarNumDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarTripCarNumDataVo> data) {
        this.data = data;
    }
}
