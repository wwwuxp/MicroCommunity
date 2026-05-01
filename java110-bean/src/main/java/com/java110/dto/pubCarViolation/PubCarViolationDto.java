package com.java110.dto.pubCarViolation;

import com.java110.dto.PageDto;

import java.io.Serializable;

public class PubCarViolationDto extends PageDto implements Serializable {

    private static final long serialVersionUID = -7085719854528281006L;

    private Integer handleStatus;
    private String communityId;
    private String driverId;

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
}
