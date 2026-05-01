package com.java110.vo.api.outGoing;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiOutGoingVo extends MorePageVo implements Serializable {

    List<ApiOutGoingDataVo> data;

    public List<ApiOutGoingDataVo> getData() {
        return data;
    }

    public void setData(List<ApiOutGoingDataVo> data) {
        this.data = data;
    }
}
