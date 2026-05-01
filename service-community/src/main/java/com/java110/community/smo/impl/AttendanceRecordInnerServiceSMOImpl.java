package com.java110.community.smo.impl;

import com.java110.community.dao.IAttendanceRecordServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.intf.community.IAttendanceRecordInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.attendanceRecord.ApiAttendanceRecordDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author hh
 * create date 2021/9/23
 */
@RestController
public class AttendanceRecordInnerServiceSMOImpl extends BaseServiceSMO implements IAttendanceRecordInnerServiceSMO {

    @Autowired
    private IAttendanceRecordServiceDao attendanceRecordServiceDaoImpl;

    @Override
    public int queryAttendanceRecordsCount(@RequestBody AttendanceRecordDto attendanceRecordDto) {
        return attendanceRecordServiceDaoImpl.queryAttendanceRecordsCount(attendanceRecordDto);
    }

    @Override
    public List<ApiAttendanceRecordDataVo> queryAttendanceRecords(@RequestBody AttendanceRecordDto attendanceRecordDto) {
        //校验是否传了 分页信息
        int page = attendanceRecordDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attendanceRecordDto.setPage((page - 1) * attendanceRecordDto.getRow());
        }
        List<ApiAttendanceRecordDataVo> apiAttendanceRecordDataVos = attendanceRecordServiceDaoImpl.queryAttendanceRecords(attendanceRecordDto);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);
        for (ApiAttendanceRecordDataVo apiAttendanceRecordDataVo : apiAttendanceRecordDataVos) {
            String attendanceDateStr = apiAttendanceRecordDataVo.getAttendanceDate();
            LocalDate attendanceDate = LocalDate.parse(attendanceDateStr, dateFormatter);
            int week = attendanceDate.getDayOfWeek().getValue();
            switch (week){
                case 1:
                case 2:
                case 3:
                case 4:
                case 5: apiAttendanceRecordDataVo.setIsWorkingDay(1);break;
                case 6:
                case 7: apiAttendanceRecordDataVo.setIsWorkingDay(0);break;
                default:
            }
        }
        return apiAttendanceRecordDataVos;
    }
}
