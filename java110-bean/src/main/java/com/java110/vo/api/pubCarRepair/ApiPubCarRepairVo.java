package com.java110.vo.api.pubCarRepair;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarRepairVo extends MorePageVo implements Serializable {

    List<ApiPubCarRepairDataVo> data;

    public List<ApiPubCarRepairDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarRepairDataVo> data) {
        this.data = data;
    }
}
