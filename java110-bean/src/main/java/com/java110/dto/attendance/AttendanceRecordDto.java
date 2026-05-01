package com.java110.dto.attendance;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * create date 2021/9/23
 */
public class AttendanceRecordDto extends PageDto implements Serializable {

    private static final long serialVersionUID = -5607580801505513120L;

    private String department;
    private String position;
    private String name;
    private Integer type;

    private String lessAttendanceDate;
    private String moreAttendanceDate;
    private String fixedStartWorkTime;
    private String fixedEndWorkTime;

    private List<String> userNameList;

    public List<String> getUserNameList() {
        return userNameList;
    }

    public void setUserNameList(List<String> userNameList) {
        this.userNameList = userNameList;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLessAttendanceDate() {
        return lessAttendanceDate;
    }

    public void setLessAttendanceDate(String lessAttendanceDate) {
        this.lessAttendanceDate = lessAttendanceDate;
    }

    public String getMoreAttendanceDate() {
        return moreAttendanceDate;
    }

    public void setMoreAttendanceDate(String moreAttendanceDate) {
        this.moreAttendanceDate = moreAttendanceDate;
    }

    public String getFixedStartWorkTime() {
        return fixedStartWorkTime;
    }

    public void setFixedStartWorkTime(String fixedStartWorkTime) {
        this.fixedStartWorkTime = fixedStartWorkTime;
    }

    public String getFixedEndWorkTime() {
        return fixedEndWorkTime;
    }

    public void setFixedEndWorkTime(String fixedEndWorkTime) {
        this.fixedEndWorkTime = fixedEndWorkTime;
    }
}
