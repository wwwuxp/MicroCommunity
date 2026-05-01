package com.java110.vo.api.outGoing;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/12/20
 */
public class ApiOutGoingStatisticsVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -7410932509637918889L;

    private List<ApiOutGoingStatisticsDataVo> data;

    public List<ApiOutGoingStatisticsDataVo> getData() {
        return data;
    }

    public void setData(List<ApiOutGoingStatisticsDataVo> data) {
        this.data = data;
    }

}
