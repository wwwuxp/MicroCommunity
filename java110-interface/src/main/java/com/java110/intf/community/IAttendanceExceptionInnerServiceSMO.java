package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendance.AttendanceExceptionDto;
import com.java110.po.attendanceException.AttendanceExceptionDataPo;
import com.java110.po.attendanceException.AttendanceExceptionPo;
import com.java110.vo.api.attendanceException.ApiAttendanceExceptionDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * @ClassName IAttendanceRecordInnerServiceSMO
 * @Description 考勤异常信息接口类
 * add by hh
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceExceptionApi")
public interface IAttendanceExceptionInnerServiceSMO {

    /**
     * 插入更新考勤异常员工基本信息表数据
     * @param attendanceExceptionPos 数据
     */
    @RequestMapping(value = "/upsertAttendanceExceptionBatch", method = RequestMethod.POST)
    void upsertAttendanceExceptionBatch(@RequestBody Set<AttendanceExceptionPo> attendanceExceptionPos);

    /**
     * 根据姓名，部门，职位批量获取exceptionId
     * @param name 姓名
     * @param department 部门
     * @param position 职位
     * @param attendanceMonth 考勤月份
     * @return String
     */
    @RequestMapping(value = "/getExceptionIdByNameAndDepartmentAndPosition", method = RequestMethod.GET)
    String getExceptionIdByNameAndDepartmentAndPositionAndAttendanceMonth(@RequestParam("name") String name,
                                                                           @RequestParam("department") String department,
                                                                           @RequestParam(value = "position", required = false) String position,
                                                                           @RequestParam("attendanceMonth") String attendanceMonth);

    /**
     * 批量插入考勤异常数据
     * @param attendanceExceptionDataPos 数据
     */
    @RequestMapping(value = "/insertAttendanceExceptionDataBatch", method = RequestMethod.POST)
    void insertAttendanceExceptionDataBatch(@RequestBody List<AttendanceExceptionDataPo> attendanceExceptionDataPos);

    /**
     * 查询考勤异常列表数量
     * @param attendanceExceptionDto 数据
     * @return int
     */
    @RequestMapping(value = "/queryAttendanceExceptionsCount", method = RequestMethod.POST)
    int queryAttendanceExceptionsCount(@RequestBody AttendanceExceptionDto attendanceExceptionDto);

    /**
     * 查询考勤异常列表
     * @param attendanceExceptionDto 数据
     * @return list
     */
    @RequestMapping(value = "/queryAttendanceExceptions", method = RequestMethod.POST)
    List<ApiAttendanceExceptionDataVo> queryAttendanceExceptions(@RequestBody AttendanceExceptionDto attendanceExceptionDto);

    /**
     * 核实考勤异常
     * @param id id
     * @param verifyCode 1:已核实，0:未核实
     * @return true or false
     */
    @RequestMapping(value = "/verifyAttendanceException", method = RequestMethod.POST)
    boolean verifyAttendanceException(@RequestParam("id") Integer id,
                                      @RequestParam("verifyCode") int verifyCode);

    /**
     * 考勤异常 - 修改类型
     * @param exceptionDataId 异常数据id
     * @param type 类型
     * @return true or false
     */
    @RequestMapping(value = "/modifyAttendanceExceptionDataType", method = RequestMethod.POST)
    boolean modifyAttendanceExceptionDataType(@RequestParam("exceptionDataId") String exceptionDataId,
                                              @RequestParam("type") Integer type);

    /**
     * 获取考勤异常信息
     * @param attendanceMonth 考勤月份
     * @return List
     */
    @RequestMapping(value = "/getAttendanceExceptionInfo", method = RequestMethod.GET)
    List<AttendanceExceptionDataPo> getAttendanceExceptionInfo(@RequestParam("attendanceMonth") String attendanceMonth);

    /**
     * 根据日期list获取考勤异常信息
     * @param dateStr 日期字符串 使用“,”分隔
     * @return List
     */
    @RequestMapping(value = "/getAttendanceExceptionInfoByDateStr", method = RequestMethod.GET)
    List<AttendanceExceptionDataPo> getAttendanceExceptionInfoByDateStr(@RequestParam("dateStr") String dateStr);

    /**
     * 根据id列表删除数据
     * @param ids 需要删除的id列表 使用“,”分隔
     * @return List
     */
    @RequestMapping(value = "/getAttendanceExceptionInfoByDateStr", method = RequestMethod.POST)
    int deleteAttendanceExceptionByIds(@RequestBody List<String> ids);
}
