package com.java110.entity.attendance;

import com.java110.dto.attendance.UserInfoDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * GenerateAttendanceExceptionTemplate.java中 handleVacationAndRest方法参数的封装
 * @author hh
 * @date 2021/9/29
 */
public class AttendanceExceptionMethodParam {

    private DateTimeFormatter dateTimeFormatter;
    private DateTimeFormatter dateFormatter;
    private LocalTime fixedTimeAm;
    private LocalTime fixedTimePm;
    private LocalDate attendanceDate;
    private LocalTime actualStartWorkTime;
    private LocalTime actualEndWorkTime;
    private UserInfoDto userInfoDto;
    private List<Object> cachedFileDataList;

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }

    public void setDateFormatter(DateTimeFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public LocalTime getFixedTimeAm() {
        return fixedTimeAm;
    }

    public void setFixedTimeAm(LocalTime fixedTimeAm) {
        this.fixedTimeAm = fixedTimeAm;
    }

    public LocalTime getFixedTimePm() {
        return fixedTimePm;
    }

    public void setFixedTimePm(LocalTime fixedTimePm) {
        this.fixedTimePm = fixedTimePm;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalTime getActualStartWorkTime() {
        return actualStartWorkTime;
    }

    public void setActualStartWorkTime(LocalTime actualStartWorkTime) {
        this.actualStartWorkTime = actualStartWorkTime;
    }

    public LocalTime getActualEndWorkTime() {
        return actualEndWorkTime;
    }

    public void setActualEndWorkTime(LocalTime actualEndWorkTime) {
        this.actualEndWorkTime = actualEndWorkTime;
    }

    public UserInfoDto getUserInfoDto() {
        return userInfoDto;
    }

    public void setUserInfoDto(UserInfoDto userInfoDto) {
        this.userInfoDto = userInfoDto;
    }

    public List<Object> getCachedFileDataList() {
        return cachedFileDataList;
    }

    public void setCachedFileDataList(List<Object> cachedFileDataList) {
        this.cachedFileDataList = cachedFileDataList;
    }
}
