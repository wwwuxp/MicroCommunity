package com.java110.vo.api.pubCarTrip;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/10/9
 */
public class ApiChooseTripVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -8187523838359994555L;

    private List<ApiChooseTripDataVo> data;

    public List<ApiChooseTripDataVo> getData() {
        return data;
    }

    public void setData(List<ApiChooseTripDataVo> data) {
        this.data = data;
    }
}
