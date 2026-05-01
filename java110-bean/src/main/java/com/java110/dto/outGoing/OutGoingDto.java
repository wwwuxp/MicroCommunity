package com.java110.dto.outGoing;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 外出信息表数据层封装
 **/
public class OutGoingDto extends PageDto implements Serializable {

    private List<String> outIds;
    private String destination;
    private String tripId;
    private String handlingMatter;
    private String userId;
    private String matterDetail;
    private String outId;
    private String startTime;
    private String endTime;
    private Integer state;
    private String communityId;

    private String name;
    private String department;
    private String position;
    private String startDate;
    private String endDate;

    private Date createTime;

    private String statusCd = "0";

    public List<String> getOutIds() {
        return outIds;
    }

    public void setOutIds(List<String> outIds) {
        this.outIds = outIds;
    }

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
