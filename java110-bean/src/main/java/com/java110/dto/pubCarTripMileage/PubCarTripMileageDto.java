package com.java110.dto.pubCarTripMileage;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * 行驶里程信息表数据层封装
 **/
public class PubCarTripMileageDto extends PageDto implements Serializable {

    private String mileageId;
    private Double currentMileage;
    private Double startMileage;
    private Double endMileage;
    private Double passMileage;
    private Date startTime;
    private Date endTime;

    /**
     * 时长，单位为分钟
     */
    private Integer duration;

    /**
     * 原定开始时间
     */
    private Date applyStartTime;
    /**
     * 原定结束时间
     */
    private Date applyEndTime;
    /**
     * 原定里程
     */
    private Double applyMileage;

    private String runRange;
    private String repairFee;
    private String oilFee;
    private String parkingFee;
    private String roadBridgeFee;

    private String tripId;
    private String driverId;
    private String communityId;
    private String statusCd = "0";

    public String getMileageId() {
        return mileageId;
    }

    public void setMileageId(String mileageId) {
        this.mileageId = mileageId;
    }

    public Double getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(Double currentMileage) {
        this.currentMileage = currentMileage;
    }

    public Double getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(Double startMileage) {
        this.startMileage = startMileage;
    }

    public Double getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(Double endMileage) {
        this.endMileage = endMileage;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Date getApplyStartTime() {
        return applyStartTime;
    }

    public void setApplyStartTime(Date applyStartTime) {
        this.applyStartTime = applyStartTime;
    }

    public Date getApplyEndTime() {
        return applyEndTime;
    }

    public void setApplyEndTime(Date applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public Double getApplyMileage() {
        return applyMileage;
    }

    public void setApplyMileage(Double applyMileage) {
        this.applyMileage = applyMileage;
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

    public Double getPassMileage() {
        return passMileage;
    }

    public void setPassMileage(Double passMileage) {
        this.passMileage = passMileage;
    }
}
