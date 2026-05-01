package com.java110.community.dao.impl;

import com.java110.community.dao.IAttendanceRecordServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.vo.api.attendanceRecord.ApiAttendanceRecordDataVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hh
 * create date 2021/9/24
 */
@Service("attendanceRecordServiceDaoImpl")
public class AttendanceRecordServiceDaoImpl extends BaseServiceDao implements IAttendanceRecordServiceDao {


    @Override
    public int queryAttendanceRecordsCount(AttendanceRecordDto attendanceRecordDto) {
        return sqlSessionTemplate.selectOne("attendanceRecordServiceDaoImpl.queryAttendanceRecordsCount", attendanceRecordDto);
    }

    @Override
    public List<ApiAttendanceRecordDataVo> queryAttendanceRecords(AttendanceRecordDto attendanceRecordDto) {
        return sqlSessionTemplate.selectList("attendanceRecordServiceDaoImpl.queryAttendanceRecords", attendanceRecordDto);
    }
}
