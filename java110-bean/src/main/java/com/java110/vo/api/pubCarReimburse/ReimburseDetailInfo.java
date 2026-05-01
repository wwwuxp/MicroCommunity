package com.java110.vo.api.pubCarReimburse;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hh
 * @date 2021/10/12
 */
public class ReimburseDetailInfo implements Serializable {

    private static final long serialVersionUID = 5195079785323958594L;

    private String reimburseId;
    private Integer feeType;
    private BigDecimal reimburseAmount;
    private String carUseDate;
    private String start;
    private String destination;
    private String invoice;
    private String[] invoices;
    private Integer approvalStatus;
    private String remark;

    public String getReimburseId() {
        return reimburseId;
    }

    public void setReimburseId(String reimburseId) {
        this.reimburseId = reimburseId;
    }

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getReimburseAmount() {
        return reimburseAmount;
    }

    public void setReimburseAmount(BigDecimal reimburseAmount) {
        this.reimburseAmount = reimburseAmount;
    }

    public String getCarUseDate() {
        return carUseDate;
    }

    public void setCarUseDate(String carUseDate) {
        this.carUseDate = carUseDate;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String[] getInvoices() {
        return invoices;
    }

    public void setInvoices(String[] invoices) {
        this.invoices = invoices;
    }
}
