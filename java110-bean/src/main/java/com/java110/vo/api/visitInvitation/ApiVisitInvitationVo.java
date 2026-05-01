package com.java110.vo.api.visitInvitation;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * create date 2021/9/17
 */
public class ApiVisitInvitationVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = 5048310671487580721L;

    private List<ApiVisitInvitationDataVo> data;

    public List<ApiVisitInvitationDataVo> getData() {
        return data;
    }

    public void setData(List<ApiVisitInvitationDataVo> data) {
        this.data = data;
    }
}
