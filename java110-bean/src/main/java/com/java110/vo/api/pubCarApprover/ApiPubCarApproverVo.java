package com.java110.vo.api.pubCarApprover;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/12/13
 */
public class ApiPubCarApproverVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = 994132728458877976L;

    List<ApiPubCarApproverDataVo> data;

    public List<ApiPubCarApproverDataVo> getData() {
        return data;
    }

    public void setData(List<ApiPubCarApproverDataVo> data) {
        this.data = data;
    }
}
