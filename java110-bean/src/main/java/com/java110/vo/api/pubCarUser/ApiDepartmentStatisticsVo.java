package com.java110.vo.api.pubCarUser;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiDepartmentStatisticsVo extends MorePageVo implements Serializable {

    List<ApiDepartmentStatisticsDataVo> data;

    public List<ApiDepartmentStatisticsDataVo> getData() {
        return data;
    }

    public void setData(List<ApiDepartmentStatisticsDataVo> data) {
        this.data = data;
    }
}
