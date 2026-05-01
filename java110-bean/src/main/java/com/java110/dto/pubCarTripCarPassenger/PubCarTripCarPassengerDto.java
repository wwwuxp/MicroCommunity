package com.java110.dto.pubCarTripCarPassenger;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * 乘车人信息管理表数据层封装
 **/
public class PubCarTripCarPassengerDto extends PageDto implements Serializable {

    private String passengerName;
    private String passengerTel;
    private String passengerId;


    private Date createTime;

    private String statusCd = "0";


    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerTel() {
        return passengerTel;
    }

    public void setPassengerTel(String passengerTel) {
        this.passengerTel = passengerTel;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
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
}
