package com.java110.vo.api.pubCarArchives;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarArchivesVo extends MorePageVo implements Serializable {

    List<ApiPubCarArchivesDataVo> data;

    public List<ApiPubCarArchivesDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarArchivesDataVo> data) {
        this.data = data;
    }
}
