package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendance.AttendanceFileDto;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.vo.api.attendanceFile.ApiAttendanceFileDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName IAttendanceFileInnerServiceSMO
 * @Description 考勤管理文件导入信息接口类
 * add by hh
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceFileApi")
public interface IAttendanceFileInnerServiceSMO {

    /**
     * 查询导入文件列表数量
     */
    @RequestMapping(value = "/queryAttendanceFilesCount", method = RequestMethod.POST)
    int queryAttendanceFilesCount(@RequestBody AttendanceFileDto attendanceFileDto);

    /**
     * 查询导入文件列表
     */
    @RequestMapping(value = "/queryAttendanceFiles", method = RequestMethod.POST)
    List<ApiAttendanceFileDataVo> queryAttendanceFiles(@RequestBody AttendanceFileDto attendanceFileDto);

    /**
     * 根据id删除导入的文件
     */
    @RequestMapping(value = "/deleteAttendanceFileById", method = RequestMethod.DELETE)
    boolean deleteAttendanceFileById(@RequestParam("fileId") Integer id,
                                     @RequestParam("dataType") Integer dataType);

    /**
     * 根据姓名、部门、职位和考勤日期获取休假数据
     */
    @RequestMapping(value = "/listVacationTimesByUserInfo", method = RequestMethod.POST)
    List<AttendanceFileDto> listVacationOrRestTimesByUserInfo(@RequestBody UserInfoDto userInfoDto);

    /**
     * 根据姓名、部门、职位和考勤日期获取休假数据
     */
    @RequestMapping(value = "/listVacationAndTimesByUserInfo", method = RequestMethod.POST)
    List<AttendanceFileDto> listVacationAndRestTimesByUserInfo(@RequestBody UserInfoDto userInfoDto);


    /**
     * 暂时无用了
     * 当天上下班未打卡，是否在休假或调休
     */
    @RequestMapping(value = "/isVacationOrRest", method = RequestMethod.POST)
    boolean isVacationOrRest(@RequestBody UserInfoDto userInfoDto);
}
