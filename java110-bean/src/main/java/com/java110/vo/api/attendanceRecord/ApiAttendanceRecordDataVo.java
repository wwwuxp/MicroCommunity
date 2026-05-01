package com.java110.vo.api.attendanceRecord;

import java.io.Serializable;

/**
 * @author hh
 * create date 2021/9/24
 */
public class ApiAttendanceRecordDataVo implements Serializable {

    private static final long serialVersionUID = -5311121047415906368L;

    private String recordId;
    private String department;
    private String position;
    private String name;
    private String attendanceDate;
    private Integer isWorkingDay;

    private String actualStartWorkTime;
    private String actualEndWorkTime;

    private Integer type;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getActualStartWorkTime() {
        return actualStartWorkTime;
    }

    public void setActualStartWorkTime(String actualStartWorkTime) {
        this.actualStartWorkTime = actualStartWorkTime;
    }

    public String getActualEndWorkTime() {
        return actualEndWorkTime;
    }

    public void setActualEndWorkTime(String actualEndWorkTime) {
        this.actualEndWorkTime = actualEndWorkTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsWorkingDay() {
        return isWorkingDay;
    }

    public void setIsWorkingDay(Integer isWorkingDay) {
        this.isWorkingDay = isWorkingDay;
    }
}
