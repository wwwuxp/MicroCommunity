package com.java110.po.pubCarAlarm;

import java.io.Serializable;

public class PubCarAlarmPo implements Serializable {

    /**
     * 行程严重超时（高）
     */
    public static final int SEVERE_TIMEOUT = 1;
    /**
     * 司机未提交里程（中）
     */
    public static final int MILEAGE_NOT_SUBMITTED = 2;
    /**
     * 司机未按时开始行程（中）
     */
    public static final int NOT_START_TRIP_ON_TIME = 3;
    /**
     * 行程里程超标（高）
     */
    public static final int MILEAGE_EXCEEDED = 4;

    /**
     * 管理人在行程开始1小时前仍未进行审核，且司机提交申请已超过2小时（高）
     */
    public static final int NOT_APPROVE = 5;

    /**
     * 里程数存在异常（中）。同一部车，上个司机提交的结束里程跟当前司机提交的开始里程不一致，而且误差大于10公里
     */
    public static final int ERROR_MILEAGE = 6;

    /**
     * 钥匙未归还（高）。离用车申请结束时间超过24小时，钥匙仍未归还。
     */
    public static final int KEY_NOT_RETURNED = 7;

    /**
     * 钥匙超时归还（高）。车钥匙已经归还到钥匙柜，但是归还时间超过用车申请结束时间12个小时以上。
     */
    public static final int KEY_RETURNED_OVER_TIME = 8;

    /**
     * 超时领取钥匙（中）。车钥匙领取时间比申请时间晚2小时以上。
     */
    public static final int OVERTIME_KEY_COLLECTION = 9;

    /**
     * 钥匙柜网络异常（高）。HC有个服务定期监测面板机的状态，如果网络不通则报警。
     */
    public static final int KEY_LOCKER_NETWORK_EXCEPTION = 10;

    /**
     * 报警等级：高
     */
    public static final String ALARM_LEVEL_HIGH = "H";

    /**
     * 报警等级：中
     */
    public static final String ALARM_LEVEL_MIDDLE = "M";

    private String alarmId;
    private Integer name;
    private String grade;
    private String driverId;
    private String tripId;
    private String detail;
    private String resultProcess;
    private String remark;
    private String bId;
    private String communityId;
    private String statusCd = "0";
    private String realStartTime;
    private String realEndTime;

    public String getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(String realEndTime) {
        this.realEndTime = realEndTime;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getRealStartTime() {
        return realStartTime;
    }

    public void setRealStartTime(String realStartTime) {
        this.realStartTime = realStartTime;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getResultProcess() {
        return resultProcess;
    }

    public void setResultProcess(String resultProcess) {
        this.resultProcess = resultProcess;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }
}
