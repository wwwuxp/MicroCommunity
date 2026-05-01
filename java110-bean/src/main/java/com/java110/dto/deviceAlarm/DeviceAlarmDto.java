package com.java110.dto.deviceAlarm;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/12/14
 */
public class DeviceAlarmDto extends PageDto implements Serializable {

    private static final long serialVersionUID = 9144270015782460781L;

    private String id;
    private String name;
    private String time;
    private String type;
    private String level;
    private String device;
    private Integer handleState;

    private String communityId;
    private String statusCd = "0";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getHandleState() {
        return handleState;
    }

    public void setHandleState(Integer handleState) {
        this.handleState = handleState;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
