package com.java110.vo.api.pubCarTrip;

import com.java110.vo.api.pubCarTripCarNum.ApiPubCarTripCarNumDataVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarTripDataVo implements Serializable {

    private String tripId;
    private String start;
    private String destination;
    private String startTime;
    private String endTime;
    private Integer applyNum;

    /**
     * 1：执法执勤车，2：一般公务车
     */
    private Integer carType;

    /**
     * 行程状态。1：未开始，2：进行中，3：已结束, 0:全部
     */
    private Integer tripStatus;
    /**
     * 审批状态。1：不通过，2：未审核，3：通过，0:全部
     */
    private Integer approvalStatus;

    /**
     * 是否局领导审批。0：否，1：是
     */
    private Integer isBureauLeaderApprove;

    /**
     * 是否科室领导审批。0：否，1：是
     */
    private Integer isDepartLeaderApprove;

    private String remark;

    private String applyReason;
    private Integer isUrgent;
    private Integer isDaily;
    /**
     * 日常用车周期。1：每天，2：每周，3：每月
     */
    private Integer dailyPeriod;
    private String communityId;

    private String applicant;

    private String applicantId;

    /**
     * 申请人部门
     */
    private String department;

    /**
     * 职位
     */
    private String position;

    /**
     * 申请人电话
     */
    private String tel;

    private String applicationTime;

    private Integer reviewerRole;

    /**
     * 是否审批过。1：审批过，0：未审批过
     */
    private Integer isApproved;

    private String startMileage;
    private String endMileage;
    private String passMileage;
    private String runRange;
    private String repairFee;
    private String oilFee;
    private String parkingFee;
    private String roadBridgeFee;

    private Integer pass;

    private List<ProcessDataVo> processDataVos;

    private List<ApiPubCarTripCarNumDataVo> carNumDataVos;

    public String getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(String applicationTime) {
        this.applicationTime = applicationTime;
    }

    public List<ApiPubCarTripCarNumDataVo> getCarNumDataVos() {
        return carNumDataVos;
    }

    public void setCarNumDataVos(List<ApiPubCarTripCarNumDataVo> carNumDataVos) {
        this.carNumDataVos = carNumDataVos;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(Integer applyNum) {
        this.applyNum = applyNum;
    }

    public void setIsUrgent(Integer isUrgent) {
        this.isUrgent = isUrgent;
    }

    public void setIsDaily(Integer isDaily) {
        this.isDaily = isDaily;
    }

    public void setDailyPeriod(Integer dailyPeriod) {
        this.dailyPeriod = dailyPeriod;
    }

    public Integer getIsUrgent() {
        return isUrgent;
    }

    public Integer getIsDaily() {
        return isDaily;
    }

    public Integer getDailyPeriod() {
        return dailyPeriod;
    }

    public Integer getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(Integer tripStatus) {
        this.tripStatus = tripStatus;
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

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getReviewerRole() {
        return reviewerRole;
    }

    public void setReviewerRole(Integer reviewerRole) {
        this.reviewerRole = reviewerRole;
    }

    public Integer getIsBureauLeaderApprove() {
        return isBureauLeaderApprove;
    }

    public void setIsBureauLeaderApprove(Integer isBureauLeaderApprove) {
        this.isBureauLeaderApprove = isBureauLeaderApprove;
    }

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getIsDepartLeaderApprove() {
        return isDepartLeaderApprove;
    }

    public void setIsDepartLeaderApprove(Integer isDepartLeaderApprove) {
        this.isDepartLeaderApprove = isDepartLeaderApprove;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public String getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(String startMileage) {
        this.startMileage = startMileage;
    }

    public String getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(String endMileage) {
        this.endMileage = endMileage;
    }

    public String getPassMileage() {
        return passMileage;
    }

    public void setPassMileage(String passMileage) {
        this.passMileage = passMileage;
    }

    public String getRunRange() {
        return runRange;
    }

    public void setRunRange(String runRange) {
        this.runRange = runRange;
    }

    public String getRepairFee() {
        return repairFee;
    }

    public void setRepairFee(String repairFee) {
        this.repairFee = repairFee;
    }

    public String getOilFee() {
        return oilFee;
    }

    public void setOilFee(String oilFee) {
        this.oilFee = oilFee;
    }

    public String getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(String parkingFee) {
        this.parkingFee = parkingFee;
    }

    public String getRoadBridgeFee() {
        return roadBridgeFee;
    }

    public void setRoadBridgeFee(String roadBridgeFee) {
        this.roadBridgeFee = roadBridgeFee;
    }

    public List<ProcessDataVo> getProcessDataVos() {
        return processDataVos;
    }

    public void setProcessDataVos(List<ProcessDataVo> processDataVos) {
        this.processDataVos = processDataVos;
    }

    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }
}
