package com.java110.po.visitInvitation;

import java.io.Serializable;
import java.util.Date;

public class VisitInvitationPo implements Serializable {

    /**
     * 今天
     */
    public static final Integer TODAY = 1;

    /**
     * 明天
     */
    public static final Integer TOMORROW = 2;

    /**
     * 后天
     */
    public static final Integer THE_DAY_AFTER_TOMORROW = 3;

    /**
     * 自定义
     */
    public static final Integer CUSTOM= 4;

    private String invitationId;
    private Integer visitTime;
    private Date startTime;
    private Date endTime;
    private Integer isCome;

    private String communityId;
    private String statusCd = "0";
    private String bId;

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public Integer getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Integer visitTime) {
        this.visitTime = visitTime;
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

    public Integer getIsCome() {
        return isCome;
    }

    public void setIsCome(Integer isCome) {
        this.isCome = isCome;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }
}
