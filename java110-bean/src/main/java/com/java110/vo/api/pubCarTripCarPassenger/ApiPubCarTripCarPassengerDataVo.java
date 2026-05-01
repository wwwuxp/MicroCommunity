package com.java110.vo.api.pubCarTripCarPassenger;

import java.io.Serializable;

public class ApiPubCarTripCarPassengerDataVo implements Serializable {

    private String passengerId;
    private String passengerName;
    private String passengerDepartment;
    private String passengerPosition;

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerDepartment() {
        return passengerDepartment;
    }

    public void setPassengerDepartment(String passengerDepartment) {
        this.passengerDepartment = passengerDepartment;
    }

    public String getPassengerPosition() {
        return passengerPosition;
    }

    public void setPassengerPosition(String passengerPosition) {
        this.passengerPosition = passengerPosition;
    }
}
