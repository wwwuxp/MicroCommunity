package com.java110.po.pubCarReimburse;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/10/12
 */
public class PubCarReimbursePo implements Serializable {

    private static final long serialVersionUID = 6038317935137749539L;

    private String reimburseId;
    private String feeType;
    private String reimburseAmount;
    private String tripId;
    private String invoice;
    private String remark;
    private String approvalStatus;
    private String statusCd;
    private String communityId;

    public String getReimburseId() {
        return reimburseId;
    }

    public void setReimburseId(String reimburseId) {
        this.reimburseId = reimburseId;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getReimburseAmount() {
        return reimburseAmount;
    }

    public void setReimburseAmount(String reimburseAmount) {
        this.reimburseAmount = reimburseAmount;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
