package com.java110.dto.pubCarTripCarNum;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * 申请车辆数量管理表数据层封装
 **/
public class PubCarTripCarNumDto extends PageDto implements Serializable {

    /**
     * 指派
     */
    public static final Integer APPOINT = 1;

    /**
     * 自驾
     */
    public static final Integer SELF_DRIVING = 2;

    private Integer tripType;
    private String numId;
    private String passengerId;
    private String passengerNum;
    private String isSelf;

    private Integer no;
    private String userId;

    private Date createTime;

    private String statusCd = "0";


    public Integer getTripType() {
        return tripType;
    }

    public void setTripType(Integer tripType) {
        this.tripType = tripType;
    }

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerNum() {
        return passengerNum;
    }

    public void setPassengerNum(String passengerNum) {
        this.passengerNum = passengerNum;
    }

    public String getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(String isSelf) {
        this.isSelf = isSelf;
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

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
