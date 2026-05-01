package com.java110.vo.api.pubCarGroup;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarGroupVo extends MorePageVo implements Serializable {

    List<ApiPubCarGroupDataVo> data;

    public List<ApiPubCarGroupDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarGroupDataVo> data) {
        this.data = data;
    }
}
