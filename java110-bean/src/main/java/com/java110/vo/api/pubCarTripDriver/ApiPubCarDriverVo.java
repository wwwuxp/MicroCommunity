package com.java110.vo.api.pubCarTripDriver;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarDriverVo extends MorePageVo implements Serializable {

    List<ApiPubCarDriverDataVo> data;

    public List<ApiPubCarDriverDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarDriverDataVo> data) {
        this.data = data;
    }
}
