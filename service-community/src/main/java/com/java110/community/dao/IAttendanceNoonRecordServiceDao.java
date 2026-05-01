package com.java110.community.dao;

import com.java110.dto.attendance.AttendanceNoonRecordDto;
import com.java110.vo.api.attendanceNoonRecord.ApiAttendanceNoonRecordDataVo;

import java.util.List;

/**
 * @author hh
 * @date 2021/12/17
 */
public interface IAttendanceNoonRecordServiceDao {

    /**
     * 查询中午考勤数量
     * @param attendanceNoonRecordDto 请求参数
     * @return int
     */
    int queryAttendanceNoonRecordsCount(AttendanceNoonRecordDto attendanceNoonRecordDto);

    /**
     * 查询中午考勤列表
     * @param attendanceNoonRecordDto 请求参数
     * @return List
     */
    List<ApiAttendanceNoonRecordDataVo> queryAttendanceNoonRecords(AttendanceNoonRecordDto attendanceNoonRecordDto);

}
