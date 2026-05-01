package com.java110.entity.pubcar;

import java.util.Date;

/**
 * (BusinessPubcarRepairForm)表实体类
 *
 * @author lmf
 * @since 2022-07-28 14:47:18
 */
public class BusinessPubcarRepairForm {


    /**
     * 此id业务上没有用，只为mysql内部不再因为没有主键而自己去找某列来建立索引或mysql内部维护一排好序的列而带来的性能开销
     */
    private Integer id;
    /**
     * 业务id
     */
    private String bId;
    /**
     * 行程id
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
     * 数据状态，添加ADD，修改MOD 删除DEL
     */
    private String operate;
    /**
     * 小区id
     */
    private String communityId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 文件json
     */
    private String ftpJson;
    /**
     * 申请人id
     */
    private String applicantId;
    /**
     * 车辆保管人手机号码
     */
    private String pubcarCustodianOfficePhone;
    /**
     * 车辆保管人id
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
     * 审批状态
     */
    private String auditStatus;

    /**
     * 数据库不存在此字段
     */
    private String userId;
    private String userName;
    private String approvalComments;

    /**
     * 申请人办公电话
     */
    private String applicantOfficePhone;

    public String getApprovalComments() {
        return approvalComments;
    }

    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
    }

    public String getApplicantOfficePhone() {
        return applicantOfficePhone;
    }

    public void setApplicantOfficePhone(String applicantOfficePhone) {
        this.applicantOfficePhone = applicantOfficePhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public String getPubcarCustodianId() {
        return pubcarCustodianId;
    }

    public void setPubcarCustodianId(String pubcarCustodianId) {
        this.pubcarCustodianId = pubcarCustodianId;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
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

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFtpJson() {
        return ftpJson;
    }

    public void setFtpJson(String ftpJson) {
        this.ftpJson = ftpJson;
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
