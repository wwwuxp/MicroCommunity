package com.java110.dto.pubCarAlarm;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

public class PubCarAlarmDto extends PageDto implements Serializable {

    private String alarmId;
    private String grade;
    private Integer isHandle;
    private Integer resultProcess;
    private Date moreAlarmTime;
    private Date lessAlarmTime;

    private String communityId;
    private String statusCd = "0";

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(Integer isHandle) {
        this.isHandle = isHandle;
    }

    public Integer getResultProcess() {
        return resultProcess;
    }

    public void setResultProcess(Integer resultProcess) {
        this.resultProcess = resultProcess;
    }

    public Date getMoreAlarmTime() {
        return moreAlarmTime;
    }

    public void setMoreAlarmTime(Date moreAlarmTime) {
        this.moreAlarmTime = moreAlarmTime;
    }

    public Date getLessAlarmTime() {
        return lessAlarmTime;
    }

    public void setLessAlarmTime(Date lessAlarmTime) {
        this.lessAlarmTime = lessAlarmTime;
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
}
