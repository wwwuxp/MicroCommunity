package com.java110.dto.visitInvitation;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hh
 * create date 2021/9/16
 */
public class VisitInvitationDto extends PageDto implements Serializable {

    private static final long serialVersionUID = -4690656164850441615L;

    private Integer visitType;
    private Integer isCome;
    private String userId;
    private String name;
    private String unit;
    private Date lessVisitTime;
    private Date moreVisitTime;

    private String communityId;

    private Date createTime;
    private String statusCd = "0";

    public Integer getVisitType() {
        return visitType;
    }

    public void setVisitType(Integer visitType) {
        this.visitType = visitType;
    }

    public Integer getIsCome() {
        return isCome;
    }

    public void setIsCome(Integer isCome) {
        this.isCome = isCome;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLessVisitTime() {
        return lessVisitTime;
    }

    public void setLessVisitTime(Date lessVisitTime) {
        this.lessVisitTime = lessVisitTime;
    }

    public Date getMoreVisitTime() {
        return moreVisitTime;
    }

    public void setMoreVisitTime(Date moreVisitTime) {
        this.moreVisitTime = moreVisitTime;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
