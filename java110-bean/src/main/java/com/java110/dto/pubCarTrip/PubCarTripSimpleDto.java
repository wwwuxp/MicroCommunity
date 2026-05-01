package com.java110.dto.pubCarTrip;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * 司机用车统计、部门用车统计、车辆统计中查询行程DTO封装类
 **/
public class PubCarTripSimpleDto extends PageDto implements Serializable {

    private String tripId;
    private String applicant;
    private String start;
    private String destination;
    private Date startTime;
    private Date endTime;
    private String license;

    private String applyNum;

    private String driverId;
    private String departmentId;
    private String carId;
    private Integer tripStatus;

    /**
     * 驾驶人
     */
    private String driver;

    /**
     * 行程类型（1:指派，2:自驾）
     */
    private Integer tripType;

    private Date lessStartTime;
    private Date moreStartTime;
    private Date lessEndTime;
    private Date moreEndTime;


    private String communityId;


    private Date createTime;

    private String statusCd = "0";

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(String applyNum) {
        this.applyNum = applyNum;
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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Integer getTripType() {
        return tripType;
    }

    public void setTripType(Integer tripType) {
        this.tripType = tripType;
    }

    public Date getLessStartTime() {
        return lessStartTime;
    }

    public void setLessStartTime(Date lessStartTime) {
        this.lessStartTime = lessStartTime;
    }

    public Date getMoreStartTime() {
        return moreStartTime;
    }

    public void setMoreStartTime(Date moreStartTime) {
        this.moreStartTime = moreStartTime;
    }

    public Date getLessEndTime() {
        return lessEndTime;
    }

    public void setLessEndTime(Date lessEndTime) {
        this.lessEndTime = lessEndTime;
    }

    public Date getMoreEndTime() {
        return moreEndTime;
    }

    public void setMoreEndTime(Date moreEndTime) {
        this.moreEndTime = moreEndTime;
    }

    public Integer getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(Integer tripStatus) {
        this.tripStatus = tripStatus;
    }
}
