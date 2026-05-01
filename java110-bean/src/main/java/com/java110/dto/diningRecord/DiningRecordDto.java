package com.java110.dto.diningRecord;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/11/11
 */
public class DiningRecordDto extends PageDto implements Serializable {

    private static final long serialVersionUID = 6376480925800877775L;

    private String personId;
    private String name;
    private String department;
    private String position;
    private String startDate;
    private String endDate;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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
}
