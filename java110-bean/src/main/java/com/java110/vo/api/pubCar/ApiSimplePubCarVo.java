package com.java110.vo.api.pubCar;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiSimplePubCarVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -8171645947293906579L;

    List<ApiSimplePubCarDataVo> data;

    public List<ApiSimplePubCarDataVo> getData() {
        return data;
    }

    public void setData(List<ApiSimplePubCarDataVo> data) {
        this.data = data;
    }
}
