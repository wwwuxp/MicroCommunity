package com.java110.vo.api.pubCarReimburse;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/10/12
 */
public class ApiPubCarReimburseVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = 222531374663490651L;

    private List<ApiPubCarReimburseDataVo> data;

    public List<ApiPubCarReimburseDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarReimburseDataVo> data) {
        this.data = data;
    }
}
