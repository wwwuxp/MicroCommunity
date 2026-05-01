package com.java110.vo.api.pubCar;

import java.io.Serializable;

/**
 * @author hh
 * @datetime 2021/7/29 16:25
 */
public class ApiCarStatisticsDataVo implements Serializable {

    private static final long serialVersionUID = -487096936805582894L;

    private String carId;
    private String license;
    private String brand;
    private String model;
    private String assetNum;
    /**
     * 所属部门
     */
    private String department;

    private Integer seatNum;

    /**
     * 总行驶里程
     */
    private Double totalMileage;

    /**
     * 订单行驶里程
     */
    private Double orderMileage;

    /**
     * 总行驶时长
     */
    private Integer totalDrivingTime;

    private Integer tripNum;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(String assetNum) {
        this.assetNum = assetNum;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(Integer seatNum) {
        this.seatNum = seatNum;
    }

    public Double getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(Double totalMileage) {
        this.totalMileage = totalMileage;
    }

    public Double getOrderMileage() {
        return orderMileage;
    }

    public void setOrderMileage(Double orderMileage) {
        this.orderMileage = orderMileage;
    }

    public Integer getTotalDrivingTime() {
        return totalDrivingTime;
    }

    public void setTotalDrivingTime(Integer totalDrivingTime) {
        this.totalDrivingTime = totalDrivingTime;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getTripNum() {
        return tripNum;
    }

    public void setTripNum(Integer tripNum) {
        this.tripNum = tripNum;
    }


}
