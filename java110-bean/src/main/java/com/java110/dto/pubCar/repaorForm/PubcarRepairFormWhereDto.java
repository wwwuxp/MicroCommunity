package com.java110.dto.pubCar.repaorForm;

import com.java110.dto.PageDto;

import java.util.Date;

/**
 * 查询条件类
 * @author lmf
 * @since 2022-07-26 17:26:03
 */
public class PubcarRepairFormWhereDto extends PageDto {

    private static final long serialVersionUID = -4280118378211863370L;
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
     * 文件名称
     */
    private String fileName;
    /**
     * 文件名称
     */
    private String communityId;
    /**
     * 文件id
     */
    private String ftpId;
    /**
     * 审核状态 1未审批  2审批通过  3审批未通过
     */
    private String auditStatus;
    /**
     * 审核意见
     */
    private String auditOpinion;
    /**
     * 删除标志 0可用，1删除
     */
    private Integer deletedFlag;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 申请时间排查条件
     */
    private String createTimeStart;
    private String createTimeEnd;

    /**
     * 用户id
     */
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFtpId() {
        return ftpId;
    }

    public void setFtpId(String ftpId) {
        this.ftpId = ftpId;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
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
}
