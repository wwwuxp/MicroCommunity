package com.java110.vo.api.pubCarUser;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarUserDetailVo extends MorePageVo implements Serializable {

    List<ApiPubCarUserDetailDataVo> data;

    public List<ApiPubCarUserDetailDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarUserDetailDataVo> data) {
        this.data = data;
    }
}
