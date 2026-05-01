package com.java110.vo.api.pubCarDataScreen;

import java.io.Serializable;
import java.util.List;

/**
 * 公车管理 - 数据看板数据显示
 */
public class ApiPubCarDataScreenOverviewAndAnalysisDataVo implements Serializable {

    private static final long serialVersionUID = 3599250767127697929L;

    /**
     * 行程数
     */
    private Integer tripNum;

    /**
     * 行程里程
     */
    private Double tripMileage;

    /**
     * 行驶时长
     */
    private Integer drivingTime;

    /**
     * 单日用车峰值
     */
    private Integer peakSingleDayCar;

    /**
     * 平均行程里程
     */
    private Double averageMileage;

    /**
     * 平均行程时长
     */
    private Double averageTravelTime;

    /**
     * 日均行程数
     */
    private Integer averageTripsPerDay;


    /**
     * 总订单数
     */
    private Integer todayTotalOrders;

    /**
     * 进行订单
     */
    private Integer inProgressOrder;

    /**
     * 完成订单数
     */
    private Integer completeOrder;

    /**
     * 无效订单
     */
    private Integer invalidOrder;


    /**
     * 明天总的订单数
     */
    private Integer tomorrowTotalOrders;

    /**
     * 明天的行程里程
     */
    private Double tomorrowTripMileage;

    /**
     * 明天的自驾订单
     */
    private Integer tomorrowSelfDrivingOrders;

    /**
     * 明天的指派订单
     */
    private Integer tomorrowAssignOrders;

    /**
     * 未来7天总的订单数
     */
    private Integer oneWeekTotalOrders;

    /**
     * 未来7天的行程里程
     */
    private Double oneWeekTripMileage;

    /**
     * 未来7天的自驾订单
     */
    private Integer oneWeekSelfDrivingOrders;

    /**
     * 未来7天的指派订单
     */
    private Integer oneWeekAssignOrders;


    /**
     * 部门行程订单次数、部门行程里程
     */
    private List<DataScreenAnalysisData> departmentalTripAnalysis;


    /**
     * 司机行程订单次数、司机行程里程
     */
    private List<DataScreenAnalysisData> driverTripAnalysis;


    /**
     * 车辆行程订单次数、车辆行程里程
     */
    private List<DataScreenAnalysisData> carTripAnalysis;


    public Integer getTripNum() {
        return tripNum;
    }

    public void setTripNum(Integer tripNum) {
        this.tripNum = tripNum;
    }

    public Double getTripMileage() {
        return tripMileage;
    }

    public void setTripMileage(Double tripMileage) {
        this.tripMileage = tripMileage;
    }

    public Integer getDrivingTime() {
        return drivingTime;
    }

    public void setDrivingTime(Integer drivingTime) {
        this.drivingTime = drivingTime;
    }

    public Integer getPeakSingleDayCar() {
        return peakSingleDayCar;
    }

    public void setPeakSingleDayCar(Integer peakSingleDayCar) {
        this.peakSingleDayCar = peakSingleDayCar;
    }

    public Double getAverageMileage() {
        return averageMileage;
    }

    public void setAverageMileage(Double averageMileage) {
        this.averageMileage = averageMileage;
    }

    public Double getAverageTravelTime() {
        return averageTravelTime;
    }

    public void setAverageTravelTime(Double averageTravelTime) {
        this.averageTravelTime = averageTravelTime;
    }

    public Integer getAverageTripsPerDay() {
        return averageTripsPerDay;
    }

    public void setAverageTripsPerDay(Integer averageTripsPerDay) {
        this.averageTripsPerDay = averageTripsPerDay;
    }

    public List<DataScreenAnalysisData> getDepartmentalTripAnalysis() {
        return departmentalTripAnalysis;
    }

    public void setDepartmentalTripAnalysis(List<DataScreenAnalysisData> departmentalTripAnalysis) {
        this.departmentalTripAnalysis = departmentalTripAnalysis;
    }

    public List<DataScreenAnalysisData> getDriverTripAnalysis() {
        return driverTripAnalysis;
    }

    public void setDriverTripAnalysis(List<DataScreenAnalysisData> driverTripAnalysis) {
        this.driverTripAnalysis = driverTripAnalysis;
    }

    public List<DataScreenAnalysisData> getCarTripAnalysis() {
        return carTripAnalysis;
    }

    public void setCarTripAnalysis(List<DataScreenAnalysisData> carTripAnalysis) {
        this.carTripAnalysis = carTripAnalysis;
    }

    public Integer getTodayTotalOrders() {
        return todayTotalOrders;
    }

    public void setTodayTotalOrders(Integer todayTotalOrders) {
        this.todayTotalOrders = todayTotalOrders;
    }

    public Integer getInProgressOrder() {
        return inProgressOrder;
    }

    public void setInProgressOrder(Integer inProgressOrder) {
        this.inProgressOrder = inProgressOrder;
    }

    public Integer getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(Integer completeOrder) {
        this.completeOrder = completeOrder;
    }

    public Integer getInvalidOrder() {
        return invalidOrder;
    }

    public void setInvalidOrder(Integer invalidOrder) {
        this.invalidOrder = invalidOrder;
    }

    public Integer getTomorrowTotalOrders() {
        return tomorrowTotalOrders;
    }

    public void setTomorrowTotalOrders(Integer tomorrowTotalOrders) {
        this.tomorrowTotalOrders = tomorrowTotalOrders;
    }

    public Double getTomorrowTripMileage() {
        return tomorrowTripMileage;
    }

    public void setTomorrowTripMileage(Double tomorrowTripMileage) {
        this.tomorrowTripMileage = tomorrowTripMileage;
    }

    public Integer getTomorrowSelfDrivingOrders() {
        return tomorrowSelfDrivingOrders;
    }

    public void setTomorrowSelfDrivingOrders(Integer tomorrowSelfDrivingOrders) {
        this.tomorrowSelfDrivingOrders = tomorrowSelfDrivingOrders;
    }

    public Integer getTomorrowAssignOrders() {
        return tomorrowAssignOrders;
    }

    public void setTomorrowAssignOrders(Integer tomorrowAssignOrders) {
        this.tomorrowAssignOrders = tomorrowAssignOrders;
    }

    public Integer getOneWeekTotalOrders() {
        return oneWeekTotalOrders;
    }

    public void setOneWeekTotalOrders(Integer oneWeekTotalOrders) {
        this.oneWeekTotalOrders = oneWeekTotalOrders;
    }

    public Double getOneWeekTripMileage() {
        return oneWeekTripMileage;
    }

    public void setOneWeekTripMileage(Double oneWeekTripMileage) {
        this.oneWeekTripMileage = oneWeekTripMileage;
    }

    public Integer getOneWeekSelfDrivingOrders() {
        return oneWeekSelfDrivingOrders;
    }

    public void setOneWeekSelfDrivingOrders(Integer oneWeekSelfDrivingOrders) {
        this.oneWeekSelfDrivingOrders = oneWeekSelfDrivingOrders;
    }

    public Integer getOneWeekAssignOrders() {
        return oneWeekAssignOrders;
    }

    public void setOneWeekAssignOrders(Integer oneWeekAssignOrders) {
        this.oneWeekAssignOrders = oneWeekAssignOrders;
    }
}
