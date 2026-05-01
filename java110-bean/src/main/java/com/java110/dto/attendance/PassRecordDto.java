package com.java110.dto.attendance;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @author hh
 * @date 2022/1/26
 */
public class PassRecordDto extends PageDto implements Serializable {

    private static final long serialVersionUID = -5670765600027811153L;

    private String smallTime;
    private String bigTime;
    private String smallDate;
    private String bigDate;
    private String name;
    private String department;
    private String position;

    public String getSmallTime() {
        return smallTime;
    }

    public void setSmallTime(String smallTime) {
        this.smallTime = smallTime;
    }

    public String getBigTime() {
        return bigTime;
    }

    public void setBigTime(String bigTime) {
        this.bigTime = bigTime;
    }

    public String getSmallDate() {
        return smallDate;
    }

    public void setSmallDate(String smallDate) {
        this.smallDate = smallDate;
    }

    public String getBigDate() {
        return bigDate;
    }

    public void setBigDate(String bigDate) {
        this.bigDate = bigDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
