package com.java110.vo.api.outGoing;

import java.io.Serializable;

public class ApiOutGoingDataVo implements Serializable {

    private static final long serialVersionUID = 536574540270609153L;

    private String outId;
    private String userName;
    private String departPosition;
    private String tel;
    private String destination;
    private String startTime;
    private String endTime;
    private String matterDetail;
    private Integer reviewerRole;

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartPosition() {
        return departPosition;
    }

    public void setDepartPosition(String departPosition) {
        this.departPosition = departPosition;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMatterDetail() {
        return matterDetail;
    }

    public void setMatterDetail(String matterDetail) {
        this.matterDetail = matterDetail;
    }

    public Integer getReviewerRole() {
        return reviewerRole;
    }

    public void setReviewerRole(Integer reviewerRole) {
        this.reviewerRole = reviewerRole;
    }
}
