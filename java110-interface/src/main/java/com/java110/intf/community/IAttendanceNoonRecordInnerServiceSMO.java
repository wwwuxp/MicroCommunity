package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendance.AttendanceNoonRecordDto;
import com.java110.vo.api.attendanceNoonRecord.ApiAttendanceNoonRecordDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author hh
 * @date 2021/12/17
 */
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceNoonRecordApi")
public interface IAttendanceNoonRecordInnerServiceSMO {

    /**
     * 查询中午考勤数量
     * @param attendanceNoonRecordDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/queryAttendanceNoonRecordsCount", method = RequestMethod.POST)
    int queryAttendanceNoonRecordsCount(@RequestBody AttendanceNoonRecordDto attendanceNoonRecordDto);

    /**
     * 查询中午考勤列表
     * @param attendanceNoonRecordDto 请求参数
     * @return List
     */
    @RequestMapping(value = "/queryAttendanceNoonRecords", method = RequestMethod.POST)
    List<ApiAttendanceNoonRecordDataVo> queryAttendanceNoonRecords(@RequestBody AttendanceNoonRecordDto attendanceNoonRecordDto);

}
