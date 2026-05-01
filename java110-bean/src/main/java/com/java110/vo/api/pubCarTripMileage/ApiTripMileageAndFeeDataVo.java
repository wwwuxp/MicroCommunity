package com.java110.vo.api.pubCarTripMileage;

import java.io.Serializable;

/**
 * @author hh
 * @date 2022/4/15
 */
public class ApiTripMileageAndFeeDataVo implements Serializable {

    private static final long serialVersionUID = -5921045104343689119L;

    private String startMileage;
    private String endMileage;
    private String runRange;
    private String repairFee;
    private String oilFee;
    private String parkingFee;
    private String roadBridgeFee;

    public String getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(String startMileage) {
        this.startMileage = startMileage;
    }

    public String getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(String endMileage) {
        this.endMileage = endMileage;
    }

    public String getRunRange() {
        return runRange;
    }

    public void setRunRange(String runRange) {
        this.runRange = runRange;
    }

    public String getRepairFee() {
        return repairFee;
    }

    public void setRepairFee(String repairFee) {
        this.repairFee = repairFee;
    }

    public String getOilFee() {
        return oilFee;
    }

    public void setOilFee(String oilFee) {
        this.oilFee = oilFee;
    }

    public String getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(String parkingFee) {
        this.parkingFee = parkingFee;
    }

    public String getRoadBridgeFee() {
        return roadBridgeFee;
    }

    public void setRoadBridgeFee(String roadBridgeFee) {
        this.roadBridgeFee = roadBridgeFee;
    }
}
