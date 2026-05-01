package com.java110.po.pubCarTripCarPassenger;

import java.io.Serializable;
import java.util.Date;

public class PubCarTripCarPassengerPo implements Serializable {

    private String passengerName;
private String passengerTel;
private String passengerId;
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



}
