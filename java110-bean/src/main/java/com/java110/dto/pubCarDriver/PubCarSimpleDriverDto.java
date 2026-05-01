package com.java110.dto.pubCarDriver;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @author hh
 * @datetime 2021/7/30 16:57
 */
public class PubCarSimpleDriverDto extends PageDto implements Serializable {

    private static final long serialVersionUID = 6317191731100657673L;

    private String driverId;
    private Double totalMileage;
    private Integer totalDuration;
    private Integer totalTrip;
    private String statusCd = "0";
    private String communityId;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Double getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(Double totalMileage) {
        this.totalMileage = totalMileage;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Integer getTotalTrip() {
        return totalTrip;
    }

    public void setTotalTrip(Integer totalTrip) {
        this.totalTrip = totalTrip;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
