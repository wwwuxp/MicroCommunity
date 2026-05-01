package com.java110.dto.attendance;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/12/17
 */
public class AttendanceNoonRecordDto extends PageDto implements Serializable {

    private static final long serialVersionUID = 6517825746768898599L;

    private String name;
    private String department;
    private String position;
    private String startDate;
    private String endDate;
    private Integer type;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
