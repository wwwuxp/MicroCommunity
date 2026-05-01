package com.java110.po.pubCarAccident;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/10/9
 */
public class PubCarAccidentPo implements Serializable {

    private static final long serialVersionUID = -8665407793049024296L;

    private String accidentId;
    private String tripId;
    private String accidentSceneImg;
    private String responsibilityConfirmImg;
    private String detail;
    private String communityId;

    public String getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(String accidentId) {
        this.accidentId = accidentId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getAccidentSceneImg() {
        return accidentSceneImg;
    }

    public void setAccidentSceneImg(String accidentSceneImg) {
        this.accidentSceneImg = accidentSceneImg;
    }

    public String getResponsibilityConfirmImg() {
        return responsibilityConfirmImg;
    }

    public void setResponsibilityConfirmImg(String responsibilityConfirmImg) {
        this.responsibilityConfirmImg = responsibilityConfirmImg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
