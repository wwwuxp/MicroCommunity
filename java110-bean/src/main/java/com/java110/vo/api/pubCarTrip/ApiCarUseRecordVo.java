package com.java110.vo.api.pubCarTrip;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2022/1/10
 */
public class ApiCarUseRecordVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = 8267670840789857628L;

    private List<ApiCarUseRecordDataVo> data;

    public List<ApiCarUseRecordDataVo> getData() {
        return data;
    }

    public void setData(List<ApiCarUseRecordDataVo> data) {
        this.data = data;
    }
}
