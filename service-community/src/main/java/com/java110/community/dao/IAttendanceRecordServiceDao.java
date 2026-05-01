package com.java110.community.dao;

import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.vo.api.attendanceRecord.ApiAttendanceRecordDataVo;

import java.util.List;

/**
 * @author hh
 * create date 2021/9/24
 */
public interface IAttendanceRecordServiceDao {

    int queryAttendanceRecordsCount(AttendanceRecordDto attendanceRecordDto);

    List<ApiAttendanceRecordDataVo> queryAttendanceRecords(AttendanceRecordDto attendanceRecordDto);

}
