package com.java110.dto.pubCarTrip;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 行程管理表数据层封装
 **/
public class PubCarTripDto extends PageDto implements Serializable {

    private String keyCabinet;
    private String applicant;
    private String tel;
    private String start;
    private String destination;
    private String applyNum;
    private String isUrgent;
    /**
     * 申请用车人id
     */
    private String userId;

    /**
     * 申请人部门
     */
    private String department;

    /**
     * 行程类型 1：指派，2：自驾, 0:全部
     */
    private Integer tripType;
    /**
     * 行程状态。1：未开始，2：进行中，3：已结束, 0:全部
     */
    private Integer tripStatus;
    /**
     * 审批状态。1：不通过，2：未审核，3：通过，0:全部
     */
    private Integer approvalStatus;

    /**
     * 申请时间
     */
    private Date applyTimeStart;
    private Date applyTimeEnd;

    /**
     * 出发时间排序 升序：ASC,降序：DESC
     */
    private String sort;

    private String tripId;
    private String remark;
    private String applyReason;
    private String dailyPeriod;
    private String isDaily;
    private String startTime;
    private String endTime;
    /**
     * 职位
     */
    private String position;

    private List<String> tripIds;

    private String communityId;

    private String createTime;

    private String statusCd = "0";

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(String applyNum) {
        this.applyNum = applyNum;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getTripType() {
        return tripType;
    }

    public void setTripType(Integer tripType) {
        this.tripType = tripType;
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

    public Date getApplyTimeStart() {
        return applyTimeStart;
    }

    public void setApplyTimeStart(Date applyTimeStart) {
        this.applyTimeStart = applyTimeStart;
    }

    public Date getApplyTimeEnd() {
        return applyTimeEnd;
    }

    public void setApplyTimeEnd(Date applyTimeEnd) {
        this.applyTimeEnd = applyTimeEnd;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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

    public String getDailyPeriod() {
        return dailyPeriod;
    }

    public void setDailyPeriod(String dailyPeriod) {
        this.dailyPeriod = dailyPeriod;
    }

    public String getIsDaily() {
        return isDaily;
    }

    public void setIsDaily(String isDaily) {
        this.isDaily = isDaily;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getTripIds() {
        return tripIds;
    }

    public void setTripIds(List<String> tripIds) {
        this.tripIds = tripIds;
    }

    public String getKeyCabinet() {
        return keyCabinet;
    }

    public void setKeyCabinet(String keyCabinet) {
        this.keyCabinet = keyCabinet;
    }
}
