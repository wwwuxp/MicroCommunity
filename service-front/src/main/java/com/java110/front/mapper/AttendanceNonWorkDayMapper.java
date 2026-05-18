package com.java110.front.mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

public interface AttendanceNonWorkDayMapper {
    Integer selectCountByDate(@Param("attendanceDate") LocalDate attendanceDate);
}
