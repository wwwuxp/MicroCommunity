package com.java110.po.attendanceException;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author hh
 * @date 2021/9/27
 */
public class AttendanceExceptionPo implements Serializable {

    private static final long serialVersionUID = 3699567215397806644L;

    /**
     * 核实：是
     */
    public static final Integer VERIFY_TRUE = 1;

    /**
     * 核实：否
     */
    public static final Integer VERIFY_FALSE = 0;

    private String id;
    private String department;
    private String exceptionId;
    private String position;
    private String name;
    private String attendanceMonth;
    private Integer isVerify;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceExceptionPo that = (AttendanceExceptionPo) o;
        return department.equals(that.department) && name.equals(that.name) && attendanceMonth.equals(that.attendanceMonth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(department, name, attendanceMonth);
    }

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
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

    public String getAttendanceMonth() {
        return attendanceMonth;
    }

    public void setAttendanceMonth(String attendanceMonth) {
        this.attendanceMonth = attendanceMonth;
    }

    public Integer getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(Integer isVerify) {
        this.isVerify = isVerify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
