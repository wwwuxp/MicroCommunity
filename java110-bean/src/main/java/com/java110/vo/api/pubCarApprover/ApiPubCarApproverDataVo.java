package com.java110.vo.api.pubCarApprover;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/12/13
 */
public class ApiPubCarApproverDataVo implements Serializable {

    private static final long serialVersionUID = 324556655635459498L;

    private Integer id;
    private String approverId;
    private Integer roleType;
    private String approvalRange;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getApprovalRange() {
        return approvalRange;
    }

    public void setApprovalRange(String approvalRange) {
        this.approvalRange = approvalRange;
    }
}
