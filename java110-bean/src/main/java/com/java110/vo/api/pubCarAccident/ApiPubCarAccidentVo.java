package com.java110.vo.api.pubCarAccident;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/10/11
 */
public class ApiPubCarAccidentVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = 7438807875551489088L;

    private List<ApiPubCarAccidentDataVo> data;

    public List<ApiPubCarAccidentDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarAccidentDataVo> data) {
        this.data = data;
    }
}
