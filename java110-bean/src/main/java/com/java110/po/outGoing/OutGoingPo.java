package com.java110.po.outGoing;

import java.io.Serializable;
import java.util.Date;

public class OutGoingPo implements Serializable {

    private String outId;
    private String destination;
    private String startTime;
    private String endTime;
    private String handlingMatter;
    private String matterDetail;
    private String state;
    private String userId;
    private String tripId;
    private String statusCd = "0";
    private String communityId;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getHandlingMatter() {
        return handlingMatter;
    }

    public void setHandlingMatter(String handlingMatter) {
        this.handlingMatter = handlingMatter;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMatterDetail() {
        return matterDetail;
    }

    public void setMatterDetail(String matterDetail) {
        this.matterDetail = matterDetail;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

}
