package com.java110.vo.api.attendanceException;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/10/8
 */
public class AttendanceExceptionDetail implements Serializable {

    private static final long serialVersionUID = 5476221035246325520L;

    private String exceptionDataId;
    private String attendanceDate;
    private Integer isWorkingDay;
    private String actualStartWorkTime;
    private String actualEndWorkTime;
    private Integer type;

    public String getExceptionDataId() {
        return exceptionDataId;
    }

    public void setExceptionDataId(String exceptionDataId) {
        this.exceptionDataId = exceptionDataId;
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
