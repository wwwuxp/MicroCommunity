package com.java110.vo.api.keyCabinetSn;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2022/2/19
 */
public class ApiKeyCabinetSnVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = 6896128993453399221L;

    private List<ApiKeyCabinetSnDataVo> data;

    public List<ApiKeyCabinetSnDataVo> getData() {
        return data;
    }

    public void setData(List<ApiKeyCabinetSnDataVo> data) {
        this.data = data;
    }
}
