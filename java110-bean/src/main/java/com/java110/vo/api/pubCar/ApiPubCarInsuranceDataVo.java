package com.java110.vo.api.pubCar;

import java.io.Serializable;

/**
 * @author hh
 * @datetime 2021/7/21 16:13
 */
public class ApiPubCarInsuranceDataVo implements Serializable {

    private String insuranceId;
    private String quarterReview;
    private Integer quarterReviewContent;
    private String yearReview;
    private Integer yearReviewContent;
    private String insuranceExpirationDate;
    private String maintenanceExpirationDate;
    private String insuranceCompany;
    private String maintenanceFee;
    private String mileage;
    private Integer remainExpirationDays;

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
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

    public String getInsuranceExpirationDate() {
        return insuranceExpirationDate;
    }

    public void setInsuranceExpirationDate(String insuranceExpirationDate) {
        this.insuranceExpirationDate = insuranceExpirationDate;
    }

    public String getMaintenanceExpirationDate() {
        return maintenanceExpirationDate;
    }

    public void setMaintenanceExpirationDate(String maintenanceExpirationDate) {
        this.maintenanceExpirationDate = maintenanceExpirationDate;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getMaintenanceFee() {
        return maintenanceFee;
    }

    public void setMaintenanceFee(String maintenanceFee) {
        this.maintenanceFee = maintenanceFee;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public Integer getRemainExpirationDays() {
        return remainExpirationDays;
    }

    public void setRemainExpirationDays(Integer remainExpirationDays) {
        this.remainExpirationDays = remainExpirationDays;
    }
}
