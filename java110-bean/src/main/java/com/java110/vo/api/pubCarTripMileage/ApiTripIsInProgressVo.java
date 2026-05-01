package com.java110.vo.api.pubCarTripMileage;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiTripIsInProgressVo extends MorePageVo implements Serializable {

    ApiTripIsInProgressDataVo data;

    public ApiTripIsInProgressDataVo getData() {
        return data;
    }

    public void setData(ApiTripIsInProgressDataVo data) {
        this.data = data;
    }
}
