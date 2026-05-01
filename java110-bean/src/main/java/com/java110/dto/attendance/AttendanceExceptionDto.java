package com.java110.dto.attendance;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author hh
 * @date 2021/10/8
 */
public class AttendanceExceptionDto extends PageDto implements Serializable {

    private static final long serialVersionUID = 3891008286494588430L;

    private String department;
    private String position;
    private String name;
    private Integer isVerify;
    private String attendanceMonth;
    private String attendanceEndMonth;
    private Set<String> range;

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

    public Integer getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(Integer isVerify) {
        this.isVerify = isVerify;
    }

    public String getAttendanceMonth() {
        return attendanceMonth;
    }

    public void setAttendanceMonth(String attendanceMonth) {
        this.attendanceMonth = attendanceMonth;
    }

    public String getAttendanceEndMonth() {
        return attendanceEndMonth;
    }

    public void setAttendanceEndMonth(String attendanceEndMonth) {
        this.attendanceEndMonth = attendanceEndMonth;
    }

    public Set<String> getRange() {
        return range;
    }

    public void setRange(Set<String> range) {
        this.range = range;
    }
}
