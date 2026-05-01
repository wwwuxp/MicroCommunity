package com.java110.vo.api.visitInvitation;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * create date 2021/9/17
 */
public class ApiVisitInvitationDataVo implements Serializable {

    private static final long serialVersionUID = 2180671551399332714L;

    private String invitationId;
    private Integer visitType;
    private String visitTime;
    private Integer isCome;
    private String qrCode;
    private String invitationReason;
    private String createTime;

    private List<ApiVisitorDataVo> visitors;

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public Integer getVisitType() {
        return visitType;
    }

    public void setVisitType(Integer visitType) {
        this.visitType = visitType;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public Integer getIsCome() {
        return isCome;
    }

    public void setIsCome(Integer isCome) {
        this.isCome = isCome;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public List<ApiVisitorDataVo> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<ApiVisitorDataVo> visitors) {
        this.visitors = visitors;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getInvitationReason() {
        return invitationReason;
    }

    public void setInvitationReason(String invitationReason) {
        this.invitationReason = invitationReason;
    }
}
