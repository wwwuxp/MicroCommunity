package com.java110.entity.pubcar;

import java.util.Date;

/**
 * (PubcarRepairForm)表实体类
 *
 * @author lmf
 * @since 2022-07-26 17:26:03
 */
public class PubcarRepairForm {

    /**
     * 主键
     */
    private String repairId;
    /**
     * 车辆id
     */
    private String carId;
    /**
     * 维修费用
     */
    private Double repairFees;
    /**
     * 申请说明
     */
    private String remark;
    /**
     * 删除标志 0可用，1删除
     */
    private Integer deletedFlag;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 文件json
     */
    private String ftpJson;
    /**
     * 小区id，预留
     */
    private String communityId;
    /**
     * 申请人id
     */
    private String applicantId;
    /**
     * 车辆保管人办公号码
     */
    private String pubcarCustodianOfficePhone;
    /**
     * 车辆保管人办公电话
     */
    private String pubcarCustodianId;
    /**
     * 是否交通事故
     */
    private String isTrafficAccident;
    /**
     * 是否已送维修
     */
    private String isRepair;
    /**
     * 建议维修公司 字典表中 type为
     */
    private String repairRelProposal;
    /**
     * 确认维修公司 字典表中 type为repair_rel
     */
    private String repairRelConfirm;
    /**
     * 流程步骤
     */
    private Integer flowStep;



    /**
     * 业务id
     */
    private String bId;

    /**
     * 审批状态 1未审批 2审批通过 3审批不通过 4撤回
     */
    private String auditStatus;

    /**
     * 申请人办公电话
     */
    private String applicantOfficePhone;

    /**
     * 修改时间
     */
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getApplicantOfficePhone() {
        return applicantOfficePhone;
    }

    public void setApplicantOfficePhone(String applicantOfficePhone) {
        this.applicantOfficePhone = applicantOfficePhone;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }


    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public Double getRepairFees() {
        return repairFees;
    }

    public void setRepairFees(Double repairFees) {
        this.repairFees = repairFees;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getFtpJson() {
        return ftpJson;
    }

    public void setFtpJson(String ftpJson) {
        this.ftpJson = ftpJson;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getPubcarCustodianOfficePhone() {
        return pubcarCustodianOfficePhone;
    }

    public void setPubcarCustodianOfficePhone(String pubcarCustodianOfficePhone) {
        this.pubcarCustodianOfficePhone = pubcarCustodianOfficePhone;
    }

    public String getPubcarCustodianId() {
        return pubcarCustodianId;
    }

    public void setPubcarCustodianId(String pubcarCustodianId) {
        this.pubcarCustodianId = pubcarCustodianId;
    }

    public String getIsTrafficAccident() {
        return isTrafficAccident;
    }

    public void setIsTrafficAccident(String isTrafficAccident) {
        this.isTrafficAccident = isTrafficAccident;
    }

    public String getIsRepair() {
        return isRepair;
    }

    public void setIsRepair(String isRepair) {
        this.isRepair = isRepair;
    }

    public String getRepairRelProposal() {
        return repairRelProposal;
    }

    public void setRepairRelProposal(String repairRelProposal) {
        this.repairRelProposal = repairRelProposal;
    }

    public String getRepairRelConfirm() {
        return repairRelConfirm;
    }

    public void setRepairRelConfirm(String repairRelConfirm) {
        this.repairRelConfirm = repairRelConfirm;
    }

    public Integer getFlowStep() {
        return flowStep;
    }

    public void setFlowStep(Integer flowStep) {
        this.flowStep = flowStep;
    }
}
