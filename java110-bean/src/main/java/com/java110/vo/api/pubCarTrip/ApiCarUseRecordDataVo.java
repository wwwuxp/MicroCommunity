package com.java110.vo.api.pubCarTrip;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2022/1/10
 */
public class ApiCarUseRecordDataVo implements Serializable {

    private static final long serialVersionUID = -1976812323071569262L;

    private String tripId;
    private String start;
    private String destination;
    private String startTime;
    private String endTime;
    private String createTime;
    private String brand;
    private String model;
    private String license;
    private Integer pass;
    private List<ProcessDataVo> process;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ProcessDataVo> getProcess() {
        return process;
    }

    public void setProcess(List<ProcessDataVo> process) {
        this.process = process;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }
}
