package com.java110.vo.api.resourceOrder;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * create date 2021/9/17
 */
public class ApiOrderSimpleVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -4506944375066857249L;

    private List<ApiOrderSimpleDataVo> data;

    public List<ApiOrderSimpleDataVo> getData() {
        return data;
    }

    public void setData(List<ApiOrderSimpleDataVo> data) {
        this.data = data;
    }
}
