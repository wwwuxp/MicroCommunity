package com.java110.front.mapper;

import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.po.attendance.AttendanceRecordPo;
import com.java110.po.attendance.SysAllDatePo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRecordMapper {
    int updateRepairTypeByNameAndDepartAndDate(@Param("repair_type") Integer repairType,@Param("name") String name,@Param("department") String department,@Param("attendanceDate") String attendanceDate);

    int updateRemarkByNameAndDepartAndDate(@Param("remark") String remark, @Param("name") String name,@Param("department") String department,@Param("attendanceDate") String attendanceDate);

    List<AttendanceRecordDto> getByNameAndDate(@Param("name") String name, @Param("attendanceDate") String attendanceDate);

    AttendanceRecordPo selectLatestByDepartmentAndNameAndDate(@Param("department") String department, @Param("name") String name, @Param("date") LocalDate dateStr);

    void insertRecord(@Param("attendanceRecord") AttendanceRecordPo attendanceRecord);

    List<SysAllDatePo> getDateBeetStartAndEndDate(@Param("startDate") LocalDate vacationActStartDate, @Param("endDate") LocalDate vacationActEndDate);

    List<AttendanceRecordPo> selectByDepartmentAndNameAndDate(@Param("department") String department, @Param("name") String name, @Param("date") LocalDate dateStr);

}
