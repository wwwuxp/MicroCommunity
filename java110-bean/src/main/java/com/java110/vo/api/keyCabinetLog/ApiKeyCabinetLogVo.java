package com.java110.vo.api.keyCabinetLog;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2022/2/14
 */
public class ApiKeyCabinetLogVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -7480632961526465213L;

    private List<ApiKeyCabinetLogDataVo> data;

    public List<ApiKeyCabinetLogDataVo> getData() {
        return data;
    }

    public void setData(List<ApiKeyCabinetLogDataVo> data) {
        this.data = data;
    }
}
