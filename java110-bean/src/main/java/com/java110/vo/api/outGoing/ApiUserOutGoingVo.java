package com.java110.vo.api.outGoing;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/11/18
 */
public class ApiUserOutGoingVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -226556572133800302L;

    private List<ApiUserOutGoingDataVo> data;

    public List<ApiUserOutGoingDataVo> getData() {
        return data;
    }

    public void setData(List<ApiUserOutGoingDataVo> data) {
        this.data = data;
    }

}
