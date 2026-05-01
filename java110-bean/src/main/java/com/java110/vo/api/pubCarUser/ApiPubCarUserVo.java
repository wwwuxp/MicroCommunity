package com.java110.vo.api.pubCarUser;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarUserVo extends MorePageVo implements Serializable {

    List<ApiPubCarUserDataVo> data;

    public List<ApiPubCarUserDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarUserDataVo> data) {
        this.data = data;
    }
}
