package com.java110.vo.api.pubCarArchives;

import com.java110.vo.api.pubCarRepair.ApiPubCarRepairDataVo;
import com.java110.vo.api.pubCarViolation.ApiPubCarViolationDataVo;
import com.java110.vo.api.pubCarViolation.ApiSimplePubCarViolationDataVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @datetime 2021/8/6 14:50
 */
public class ApiPubCarArchivesDataVo implements Serializable {

    private static final long serialVersionUID = 471857214281882392L;

    private String carId;
    private String license;
    private String color;
    private String brand;
    private String model;
    private String frameNum;
    private String engineNum;
    private String registerDate;
    private Double totalMileage;
    private Double orderMileage;
    private Integer totalDrivingTime;
    private Integer orderNum;
    private String insuranceCompany;
    private String insuranceExpirationDate;
    private String quarterReview;
    private Integer quarterReviewContent;
    private String yearReview;
    private Integer yearReviewContent;
    private String maintenanceExpirationDate;
    private String maintenanceFee;
    private Double mileage;

    private List<ApiSimplePubCarViolationDataVo> violations;
    private List<ApiPubCarRepairDataVo> repairs;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaintenanceExpirationDate() {
        return maintenanceExpirationDate;
    }

    public void setMaintenanceExpirationDate(String maintenanceExpirationDate) {
        this.maintenanceExpirationDate = maintenanceExpirationDate;
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

    public String getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(String frameNum) {
        this.frameNum = frameNum;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
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

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getInsuranceExpirationDate() {
        return insuranceExpirationDate;
    }

    public void setInsuranceExpirationDate(String insuranceExpirationDate) {
        this.insuranceExpirationDate = insuranceExpirationDate;
    }

    public String getQuarterReview() {
        return quarterReview;
    }

    public void setQuarterReview(String quarterReview) {
        this.quarterReview = quarterReview;
    }

    public Integer getQuarterReviewContent() {
        return quarterReviewContent;
    }

    public void setQuarterReviewContent(Integer quarterReviewContent) {
        this.quarterReviewContent = quarterReviewContent;
    }

    public String getYearReview() {
        return yearReview;
    }

    public void setYearReview(String yearReview) {
        this.yearReview = yearReview;
    }

    public Integer getYearReviewContent() {
        return yearReviewContent;
    }

    public void setYearReviewContent(Integer yearReviewContent) {
        this.yearReviewContent = yearReviewContent;
    }

    public String getMaintenanceFee() {
        return maintenanceFee;
    }

    public void setMaintenanceFee(String maintenanceFee) {
        this.maintenanceFee = maintenanceFee;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public List<ApiSimplePubCarViolationDataVo> getViolations() {
        return violations;
    }

    public void setViolations(List<ApiSimplePubCarViolationDataVo> violations) {
        this.violations = violations;
    }

    public List<ApiPubCarRepairDataVo> getRepairs() {
        return repairs;
    }

    public void setRepairs(List<ApiPubCarRepairDataVo> repairs) {
        this.repairs = repairs;
    }
}
