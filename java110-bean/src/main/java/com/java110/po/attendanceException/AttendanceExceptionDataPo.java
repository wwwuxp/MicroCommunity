package com.java110.po.attendanceException;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/9/27
 */
public class AttendanceExceptionDataPo implements Serializable {

    private static final long serialVersionUID = 7958009615308150917L;

    private String id;
    private String attendanceDate;
    private String actualStartWorkTime;
    private String actualEndWorkTime;
    // 1：正常，2：迟到，3：早退，4：旷工
    private Integer type;
    private String exceptionId;

    private String name;
    private String department;
    private String position;
    private String attendanceMonth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
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

    public String getAttendanceMonth() {
        return attendanceMonth;
    }

    public void setAttendanceMonth(String attendanceMonth) {
        this.attendanceMonth = attendanceMonth;
    }
}
