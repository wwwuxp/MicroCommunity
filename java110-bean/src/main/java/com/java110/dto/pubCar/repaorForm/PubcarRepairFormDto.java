package com.java110.dto.pubCar.repaorForm;

import com.java110.entity.pubcar.PubcarRepairFormFlow;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (PubcarRepairForm)表实体类
 *
 * @author lmf
 * @since 2022-07-26 17:26:03
 */
public class PubcarRepairFormDto implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * 申请人员id
     */
    private String applicantId;
    /**
     * 申请人员名称
     */
    private String applicantName;
    /**
     * 申请人手机号
     */
    private String applicantPhone;
    /**
     * 申请人办公号码
     */
    private String applicantOfficePhone;
    /**
     * 公车管理人员id
     */
    private String pubcarCustodianId;
    /**
     * 公车管理人员手机号
     */
    private String pubcarCustodianPhone;
    /**
     * 公车管理人员办公手机号
     */
    private String pubcarCustodianOfficePhone;
    /**
     * 公车管理人员姓名
     */
    private String pubcarCustodianName;
    /**
     * 是否交通事故 0是1否
     */
    private String isTrafficAccident;
    /**
     * 是否已送维修 0是1否
     */
    private String isRepair;
    /**
     * 建议维修公司 字典表中 type为
     */
    private String repairRelProposal;
    /**
     * 确认维修公司
     */
    private String repairRelConfirm;

    /**
     * 主键
     */
    private String repairId;
    /**
     * 车辆id
     */
    private String carId;

    /**
     * 钥匙柜编号
     */
    private String keyCabinet;

    /**
     * 车牌
     */
    private String license;

    /**
     * 品牌
     */
    private String brand;
    /**
     * 型号
     */
    private String model;

    /**
     * 维修费用
     */
    private Double repairFees;
    /**
     * 申请说明
     */
    private String remark;
    /**
     * 审核状态 1未审批  2审批通过  3审批未通过
     */
    private String auditStatus;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 审批流程列表
     */
    List<PubcarRepairFormFlow> repairFormFlowList;

    /**
     * 车辆类型
     */
    private String carType;

    /**
     * 车辆类型名称
     */
    private String carTypeName;

    /**
     * 审批步骤
     */
    private String flowIndex;

    /**
     * 审批步骤
     */
    private String ftpJson;

    /**
     * 流程步长
     */
    private String flowStep;

    public String getFlowStep() {
        return flowStep;
    }

    public void setFlowStep(String flowStep) {
        this.flowStep = flowStep;
    }

    public String getFtpJson() {
        return ftpJson;
    }

    public void setFtpJson(String ftpJson) {
        this.ftpJson = ftpJson;
    }

    public String getFlowIndex() {
        return flowIndex;
    }

    public void setFlowIndex(String flowIndex) {
        this.flowIndex = flowIndex;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public String getApplicantOfficePhone() {
        return applicantOfficePhone;
    }

    public void setApplicantOfficePhone(String applicantOfficePhone) {
        this.applicantOfficePhone = applicantOfficePhone;
    }

    public String getPubcarCustodianId() {
        return pubcarCustodianId;
    }

    public void setPubcarCustodianId(String pubcarCustodianId) {
        this.pubcarCustodianId = pubcarCustodianId;
    }

    public String getPubcarCustodianPhone() {
        return pubcarCustodianPhone;
    }

    public void setPubcarCustodianPhone(String pubcarCustodianPhone) {
        this.pubcarCustodianPhone = pubcarCustodianPhone;
    }

    public String getPubcarCustodianOfficePhone() {
        return pubcarCustodianOfficePhone;
    }

    public void setPubcarCustodianOfficePhone(String pubcarCustodianOfficePhone) {
        this.pubcarCustodianOfficePhone = pubcarCustodianOfficePhone;
    }

    public String getPubcarCustodianName() {
        return pubcarCustodianName;
    }

    public void setPubcarCustodianName(String pubcarCustodianName) {
        this.pubcarCustodianName = pubcarCustodianName;
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

    public List<PubcarRepairFormFlow> getRepairFormFlowList() {
        return repairFormFlowList;
    }

    public void setRepairFormFlowList(List<PubcarRepairFormFlow> repairFormFlowList) {
        this.repairFormFlowList = repairFormFlowList;
    }

    public String getKeyCabinet() {
        return keyCabinet;
    }

    public void setKeyCabinet(String keyCabinet) {
        this.keyCabinet = keyCabinet;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
