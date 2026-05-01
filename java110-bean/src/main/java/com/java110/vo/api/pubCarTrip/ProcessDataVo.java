package com.java110.vo.api.pubCarTrip;

import java.io.Serializable;

/**
 * @author hh
 * @date 2022/1/10
 */
public class ProcessDataVo implements Serializable {

    private static final long serialVersionUID = -5784238737030445599L;

    private Integer sequence;
    private Integer reviewerRole;
    private String name;
    private String approvalTime;
    private Integer approvalStatus;

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getReviewerRole() {
        return reviewerRole;
    }

    public void setReviewerRole(Integer reviewerRole) {
        this.reviewerRole = reviewerRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(String approvalTime) {
        this.approvalTime = approvalTime;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
