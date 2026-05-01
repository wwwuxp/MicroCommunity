package com.java110.vo.api.pubCar;

import java.io.Serializable;

/**
 * @author hh
 * @datetime 2021/7/29 16:25
 */
public class ApiSimplePubCarDataVo implements Serializable {

    private static final long serialVersionUID = 4400689524676411424L;

    private String carId;
    private String brand;
    private String model;
    private String color;
    private String license;
    private Integer seatNum;
    private String returnTime;
    private String carTypeName;
    // 预约时间
    private String reserveTime;

    // new add
    private Integer isBusy;
    private Integer keyCabinet;

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }


    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public Integer getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(Integer seatNum) {
        this.seatNum = seatNum;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public Integer getIsBusy() {
        return isBusy;
    }

    public void setIsBusy(Integer isBusy) {
        this.isBusy = isBusy;
    }

    public Integer getKeyCabinet() {
        return keyCabinet;
    }

    public void setKeyCabinet(Integer keyCabinet) {
        this.keyCabinet = keyCabinet;
    }
}
