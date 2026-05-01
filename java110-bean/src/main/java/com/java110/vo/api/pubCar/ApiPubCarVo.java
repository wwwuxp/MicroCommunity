package com.java110.vo.api.pubCar;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarVo extends MorePageVo implements Serializable {

    List<ApiPubCarDataVo> data;

    public List<ApiPubCarDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarDataVo> data) {
        this.data = data;
    }
}
