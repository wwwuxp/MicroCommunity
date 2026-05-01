package com.java110.vo.api.inspectionRoute;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * create date 2021/9/18
 */
public class ApiInspectionPositionVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -7047000607693450222L;

    private List<ApiInspectionPositionDataVo> data;

    public List<ApiInspectionPositionDataVo> getData() {
        return data;
    }

    public void setData(List<ApiInspectionPositionDataVo> data) {
        this.data = data;
    }
}
