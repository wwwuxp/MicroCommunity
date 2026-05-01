package com.java110.vo.api.pubCarViolation;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarViolationVo extends MorePageVo implements Serializable {

    List<ApiPubCarViolationDataVo> data;

    public List<ApiPubCarViolationDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarViolationDataVo> data) {
        this.data = data;
    }
}
