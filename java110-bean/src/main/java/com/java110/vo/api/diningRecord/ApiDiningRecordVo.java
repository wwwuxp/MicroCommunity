package com.java110.vo.api.diningRecord;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/11/11
 */
public class ApiDiningRecordVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -6119879265957668794L;

    private List<ApiDiningRecordDataVo> data;

    public List<ApiDiningRecordDataVo> getData() {
        return data;
    }

    public void setData(List<ApiDiningRecordDataVo> data) {
        this.data = data;
    }
}
