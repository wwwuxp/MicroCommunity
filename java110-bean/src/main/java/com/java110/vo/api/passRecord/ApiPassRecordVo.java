package com.java110.vo.api.passRecord;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2022/1/26
 */
public class ApiPassRecordVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -2504450929466033517L;

    private List<ApiPassRecordDataVo> data;

    public List<ApiPassRecordDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPassRecordDataVo> data) {
        this.data = data;
    }
}
