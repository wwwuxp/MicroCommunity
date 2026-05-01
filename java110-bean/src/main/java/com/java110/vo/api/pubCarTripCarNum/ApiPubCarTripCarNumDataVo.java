package com.java110.vo.api.pubCarTripCarNum;

import com.java110.vo.api.pubCarTripCarPassenger.ApiPubCarTripCarPassengerDataVo;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarTripCarNumDataVo implements Serializable {

    private String numId;
    private Integer tripType;
    private Integer passengerNum;

    private String carId;
    private String driverId;

    private List<ApiPubCarTripCarPassengerDataVo> passengerDataVos;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public List<ApiPubCarTripCarPassengerDataVo> getPassengerDataVos() {
        return passengerDataVos;
    }

    public void setPassengerDataVos(List<ApiPubCarTripCarPassengerDataVo> passengerDataVos) {
        this.passengerDataVos = passengerDataVos;
    }

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId;
    }

    public Integer getTripType() {
        return tripType;
    }

    public void setTripType(Integer tripType) {
        this.tripType = tripType;
    }

    public Integer getPassengerNum() {
        return passengerNum;
    }

    public void setPassengerNum(Integer passengerNum) {
        this.passengerNum = passengerNum;
    }
}
