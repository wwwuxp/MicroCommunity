package com.java110.po.pubCarTripCarNum;

import java.io.Serializable;
import java.util.Date;

public class PubCarTripCarNumPo implements Serializable {

    private String numId;
    private String tripType;
    private String passengerNum;
    private String tripId;

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getPassengerNum() {
        return passengerNum;
    }

    public void setPassengerNum(String passengerNum) {
        this.passengerNum = passengerNum;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
}
