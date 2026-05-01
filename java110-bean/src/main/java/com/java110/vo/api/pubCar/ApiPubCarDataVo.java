package com.java110.vo.api.pubCar;

import java.io.Serializable;

public class ApiPubCarDataVo implements Serializable {

    private String carId;
    private String license;
    private String brand;
    private String model;
    private String color;
    private Integer seatNum;
    private Integer carType;
    private Integer carType1;
    private String carTypeName;
    private String departmentName;
    private String assetNum;
    private String communityId;
    private String remark;
    private String keyCabinet;
    private String cardNumber;
    private Integer snId;

    private ApiPubCarAttrDataVo attr;
    private ApiPubCarInsuranceDataVo insurance;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(Integer seatNum) {
        this.seatNum = seatNum;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(String assetNum) {
        this.assetNum = assetNum;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public ApiPubCarAttrDataVo getAttr() {
        return attr;
    }

    public void setAttr(ApiPubCarAttrDataVo attr) {
        this.attr = attr;
    }

    public ApiPubCarInsuranceDataVo getInsurance() {
        return insurance;
    }

    public void setInsurance(ApiPubCarInsuranceDataVo insurance) {
        this.insurance = insurance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getKeyCabinet() {
        return keyCabinet;
    }

    public void setKeyCabinet(String keyCabinet) {
        this.keyCabinet = keyCabinet;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getSnId() {
        return snId;
    }

    public void setSnId(Integer snId) {
        this.snId = snId;
    }

    public Integer getCarType1() {
        return carType1;
    }

    public void setCarType1(Integer carType1) {
        this.carType1 = carType1;
    }
}
