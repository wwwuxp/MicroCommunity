package com.java110.community.dao.impl;

import com.java110.community.dao.IAttendanceNoonRecordServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.attendance.AttendanceNoonRecordDto;
import com.java110.vo.api.attendanceNoonRecord.ApiAttendanceNoonRecordDataVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hh
 * @date 2021/12/17
 */
@Service("attendanceNoonRecordServiceDaoImpl")
public class AttendanceNoonRecordServiceDaoImpl extends BaseServiceDao implements IAttendanceNoonRecordServiceDao {

    @Override
    public int queryAttendanceNoonRecordsCount(AttendanceNoonRecordDto attendanceNoonRecordDto) {
        return sqlSessionTemplate.selectOne("attendanceNoonRecordServiceDaoImpl.queryAttendanceNoonRecordsCount", attendanceNoonRecordDto);
    }

    @Override
    public List<ApiAttendanceNoonRecordDataVo> queryAttendanceNoonRecords(AttendanceNoonRecordDto attendanceNoonRecordDto) {
        return sqlSessionTemplate.selectList("attendanceNoonRecordServiceDaoImpl.queryAttendanceNoonRecords", attendanceNoonRecordDto);
    }

}
