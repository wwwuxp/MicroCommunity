package com.java110.vo.api.pubCarUser;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiDepartmentNamesVo extends MorePageVo implements Serializable {

    List<ApiDepartmentNamesDataVo> data;

    public List<ApiDepartmentNamesDataVo> getData() {
        return data;
    }

    public void setData(List<ApiDepartmentNamesDataVo> data) {
        this.data = data;
    }

}
