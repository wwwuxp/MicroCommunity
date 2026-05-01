package com.java110.vo.api.pubCarUser;

import java.io.Serializable;

public class ApiDepartmentStatisticsDataVo implements Serializable {

    private static final long serialVersionUID = 4291640385119958169L;

    private String departmentId;
    private String department;
    private Double mileage;
    private Integer duration;
    private Integer tripNum;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getTripNum() {
        return tripNum;
    }

    public void setTripNum(Integer tripNum) {
        this.tripNum = tripNum;
    }
}
