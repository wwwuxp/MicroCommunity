package com.java110.vo.api.attendanceException;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/10/8
 */
public class ApiAttendanceExceptionDataVo implements Serializable {

    private static final long serialVersionUID = -1481940327751901742L;

    private String id;
    private String department;
    private String position;
    private String name;
    private String attendanceMonth;
    private Integer exceptionCount;
    private Integer isVerify;

    private List<AttendanceExceptionDetail> exceptionData;

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

    public Integer getExceptionCount() {
        return exceptionCount;
    }

    public void setExceptionCount(Integer exceptionCount) {
        this.exceptionCount = exceptionCount;
    }

    public Integer getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(Integer isVerify) {
        this.isVerify = isVerify;
    }

    public List<AttendanceExceptionDetail> getExceptionData() {
        return exceptionData;
    }

    public void setExceptionData(List<AttendanceExceptionDetail> exceptionData) {
        this.exceptionData = exceptionData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
