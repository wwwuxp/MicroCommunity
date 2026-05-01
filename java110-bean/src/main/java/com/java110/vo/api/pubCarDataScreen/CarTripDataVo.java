package com.java110.vo.api.pubCarDataScreen;

import java.io.Serializable;

/**
 * @author hh
 * @datetime 2021/8/16 15:37
 */
public class CarTripDataVo implements Serializable {

    private static final long serialVersionUID = 374362711210068998L;

    private String userId;

    private String tripId;

    /**
     * 发车时间
     */
    private String departureTime;

    /**
     * 出发地
     */
    private String departure;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 行程类型
     */
    private Integer tripType;

    /**
     * 车型
     */
    private String model;

    /**
     * 车牌号
     */
    private String license;

    /**
     * 司机
     */
    private String driver;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getTripType() {
        return tripType;
    }

    public void setTripType(Integer tripType) {
        this.tripType = tripType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
