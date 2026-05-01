package com.java110.vo.api.pubCarTrip;

import java.io.Serializable;

public class ApiCarTripStatisticsDataVo extends BaseStatisticsDataVo implements Serializable {

    /**
     * 人数
     */
    private Integer passengerNum;

    /**
     * 驾驶人
     */
    private String driver;

    /**
     * 行驶里程
     */
    private Double drivenDistance;

    public Integer getPassengerNum() {
        return passengerNum;
    }

    public void setPassengerNum(Integer passengerNum) {
        this.passengerNum = passengerNum;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Double getDrivenDistance() {
        return drivenDistance;
    }

    public void setDrivenDistance(Double drivenDistance) {
        this.drivenDistance = drivenDistance;
    }
}
