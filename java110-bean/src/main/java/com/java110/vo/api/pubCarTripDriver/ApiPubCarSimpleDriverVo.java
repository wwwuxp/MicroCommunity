package com.java110.vo.api.pubCarTripDriver;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarSimpleDriverVo extends MorePageVo implements Serializable {

    List<ApiPubCarSimpleDriverDataVo> data;

    public List<ApiPubCarSimpleDriverDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarSimpleDriverDataVo> data) {
        this.data = data;
    }
}
