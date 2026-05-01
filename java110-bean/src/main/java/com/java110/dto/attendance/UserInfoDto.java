package com.java110.dto.attendance;

import java.io.Serializable;

/**
 * 员工姓名、部门、职位、考勤日期信息传输类
 * @author hh
 * @date 2021/9/28
 */
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 669596051915490120L;

    private String name;
    private String department;
    private String position;
    private String attendanceDate;
    private String tableName;

    private String fixedStartWorkDateTime;
    private String fixedEndWorkDateTime;

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

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFixedStartWorkDateTime() {
        return fixedStartWorkDateTime;
    }

    public void setFixedStartWorkDateTime(String fixedStartWorkDateTime) {
        this.fixedStartWorkDateTime = fixedStartWorkDateTime;
    }

    public String getFixedEndWorkDateTime() {
        return fixedEndWorkDateTime;
    }

    public void setFixedEndWorkDateTime(String fixedEndWorkDateTime) {
        this.fixedEndWorkDateTime = fixedEndWorkDateTime;
    }

}
