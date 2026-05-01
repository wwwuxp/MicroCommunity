package com.java110.community.smo.impl;

import com.java110.community.dao.IAttendanceNoonRecordServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendance.AttendanceNoonRecordDto;
import com.java110.intf.community.IAttendanceNoonRecordInnerServiceSMO;
import com.java110.vo.api.attendanceNoonRecord.ApiAttendanceNoonRecordDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hh
 * @date 2021/12/17
 */
@RestController
public class AttendanceNoonRecordInnerServiceSMOImpl extends BaseServiceSMO implements IAttendanceNoonRecordInnerServiceSMO {

    @Autowired
    private IAttendanceNoonRecordServiceDao attendanceNoonRecordServiceDaoImpl;


    @Override
    public int queryAttendanceNoonRecordsCount(@RequestBody AttendanceNoonRecordDto attendanceNoonRecordDto) {
        return attendanceNoonRecordServiceDaoImpl.queryAttendanceNoonRecordsCount(attendanceNoonRecordDto);
    }

    @Override
    public List<ApiAttendanceNoonRecordDataVo> queryAttendanceNoonRecords(@RequestBody AttendanceNoonRecordDto attendanceNoonRecordDto) {
        //校验是否传了 分页信息
        int page = attendanceNoonRecordDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attendanceNoonRecordDto.setPage((page - 1) * attendanceNoonRecordDto.getRow());
        }
        return attendanceNoonRecordServiceDaoImpl.queryAttendanceNoonRecords(attendanceNoonRecordDto);
    }

}
