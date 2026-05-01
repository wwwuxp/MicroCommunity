package com.java110.dto.pubCarUser;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @ClassName PubCarUserDto
 * @Description 用车人管理表数据层封装
 **/
public class PubCarUserDto extends PageDto implements Serializable {

    private String name;
    private Integer gender;
    private String department;
    private String position;
    private String jobNum;
    private String tel;

    private String carUserId;
    private String userId;
    private String currentUserId;
    private Integer isSelfDriving;

    private String communityId;

    private String statusCd = "0";

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getCarUserId() {
        return carUserId;
    }

    public void setCarUserId(String carUserId) {
        this.carUserId = carUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getIsSelfDriving() {
        return isSelfDriving;
    }

    public void setIsSelfDriving(Integer isSelfDriving) {
        this.isSelfDriving = isSelfDriving;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
}
