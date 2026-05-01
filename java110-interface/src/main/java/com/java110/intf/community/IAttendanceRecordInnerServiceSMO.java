package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.vo.api.attendanceRecord.ApiAttendanceRecordDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAttendanceRecordInnerServiceSMO
 * @Description 考勤记录信息接口类
 * add by hh
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceRecordApi")
public interface IAttendanceRecordInnerServiceSMO {

    /**
     * 查询考勤记录列表数量
     */
    @RequestMapping(value = "/queryAttendanceRecordsCount", method = RequestMethod.POST)
    int queryAttendanceRecordsCount(@RequestBody AttendanceRecordDto attendanceRecordDto);

    /**
     * 查询考勤记录列表
     */
    @RequestMapping(value = "/queryAttendanceRecords", method = RequestMethod.POST)
    List<ApiAttendanceRecordDataVo> queryAttendanceRecords(@RequestBody AttendanceRecordDto attendanceRecordDto);

}
