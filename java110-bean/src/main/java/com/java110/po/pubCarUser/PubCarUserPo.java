package com.java110.po.pubCarUser;

import java.io.Serializable;

public class PubCarUserPo implements Serializable {

    private String isSelfDriving;
    private String statusCd = "0";
    private String communityId;
    private String userId;
    private String carUserId;

    public String getIsSelfDriving() {
        return isSelfDriving;
    }

    public void setIsSelfDriving(String isSelfDriving) {
        this.isSelfDriving = isSelfDriving;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarUserId() {
        return carUserId;
    }

    public void setCarUserId(String carUserId) {
        this.carUserId = carUserId;
    }


}
