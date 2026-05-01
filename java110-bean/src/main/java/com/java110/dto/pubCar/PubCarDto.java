package com.java110.dto.pubCar;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName PubCarDto
 * @Description 车辆管理表数据层封装
 **/
public class PubCarDto extends PageDto implements Serializable {

    private String keyCabinet;
    private String cardNumber;
    private Integer snId;
    private Integer keyStatus;

    private String color;
    private String remark;
    private String carId;
    private String license;
    private Integer seatNum;
    private Integer carType;
    private Integer carType1;
    private String attrId;
    private String assetNum;
    private String insuranceId;
    private String model;
    private String departmentId;
    private String departmentName;
    private String communityId;
    private String brand;
    private Integer lessSeatNum;
    private Integer moreSeatNum;

    private Date registerDate;
    private String engineNum;
    private String frameNum;
    private Integer accidentSituation;
    private String groupId;

    private Date quarterReview;
    private Integer quarterReviewContent;
    private Date yearReview;
    private Integer yearReviewContent;
    private Date insuranceExpirationDate;
    private Date maintenanceExpirationDate;
    private String insuranceCompany;
    private String maintenanceFee;
    private String mileage;

    private Date createTime;

    private String statusCd = "0";

    private Date startTime;
    private Date endTime;
    private String applicantId;


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(String assetNum) {
        this.assetNum = assetNum;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public Integer getLessSeatNum() {
        return lessSeatNum;
    }

    public void setLessSeatNum(Integer lessSeatNum) {
        this.lessSeatNum = lessSeatNum;
    }

    public Integer getMoreSeatNum() {
        return moreSeatNum;
    }

    public void setMoreSeatNum(Integer moreSeatNum) {
        this.moreSeatNum = moreSeatNum;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(String frameNum) {
        this.frameNum = frameNum;
    }

    public Integer getAccidentSituation() {
        return accidentSituation;
    }

    public void setAccidentSituation(Integer accidentSituation) {
        this.accidentSituation = accidentSituation;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getQuarterReview() {
        return quarterReview;
    }

    public void setQuarterReview(Date quarterReview) {
        this.quarterReview = quarterReview;
    }

    public Integer getQuarterReviewContent() {
        return quarterReviewContent;
    }

    public void setQuarterReviewContent(Integer quarterReviewContent) {
        this.quarterReviewContent = quarterReviewContent;
    }

    public Date getYearReview() {
        return yearReview;
    }

    public void setYearReview(Date yearReview) {
        this.yearReview = yearReview;
    }

    public Integer getYearReviewContent() {
        return yearReviewContent;
    }

    public void setYearReviewContent(Integer yearReviewContent) {
        this.yearReviewContent = yearReviewContent;
    }

    public Date getInsuranceExpirationDate() {
        return insuranceExpirationDate;
    }

    public void setInsuranceExpirationDate(Date insuranceExpirationDate) {
        this.insuranceExpirationDate = insuranceExpirationDate;
    }

    public Date getMaintenanceExpirationDate() {
        return maintenanceExpirationDate;
    }

    public void setMaintenanceExpirationDate(Date maintenanceExpirationDate) {
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

    public Integer getKeyStatus() {
        return keyStatus;
    }

    public void setKeyStatus(Integer keyStatus) {
        this.keyStatus = keyStatus;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }
}
