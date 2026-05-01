package com.java110.vo.api.pubCarDataScreen;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class CarTripVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -3748598734480245219L;

    List<CarTripDataVo> data;

    public List<CarTripDataVo> getData() {
        return data;
    }

    public void setData(List<CarTripDataVo> data) {
        this.data = data;
    }
}
