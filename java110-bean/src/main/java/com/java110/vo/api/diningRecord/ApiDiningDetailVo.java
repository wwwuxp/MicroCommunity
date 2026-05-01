package com.java110.vo.api.diningRecord;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/11/12
 */
public class ApiDiningDetailVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -1556158235387082993L;

    private List<ApiDiningDetailDataVo> data;

    public List<ApiDiningDetailDataVo> getData() {
        return data;
    }

    public void setData(List<ApiDiningDetailDataVo> data) {
        this.data = data;
    }
}
