package com.java110.community.dao;

import com.java110.dto.attendance.AttendanceExceptionDto;
import com.java110.po.attendanceException.AttendanceExceptionDataPo;
import com.java110.po.attendanceException.AttendanceExceptionPo;
import com.java110.vo.api.attendanceException.ApiAttendanceExceptionDataVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hh
 * @date 2021/9/27
 */
public interface IAttendanceExceptionServiceDao {

    /**
     * 插入更新考勤异常员工基本信息表数据
     * @param attendanceExceptionPos data
     */
    void upsertAttendanceExceptionBatch(Set<AttendanceExceptionPo> attendanceExceptionPos);

    /**
     * 根据姓名，部门，职位批量获取exceptionId
     * @param map map
     * @return String
     */
    String getExceptionIdByNameAndDepartmentAndPositionAndAttendanceMonth(Map<String, Object> map);


    /**
     * 批量插入考勤异常数据
     * @param attendanceExceptionDataPos 考勤异常数据
     */
    void insertAttendanceExceptionDataBatch(List<AttendanceExceptionDataPo> attendanceExceptionDataPos);

    /**
     * 查询考勤异常列表数量
     * @param attendanceExceptionDto 查询条件
     * @return int
     */
    int queryAttendanceExceptionsCount(AttendanceExceptionDto attendanceExceptionDto);

    /**
     * 查询考勤异常列表
     * @param attendanceExceptionDto 查询条件
     * @return list
     */
    List<ApiAttendanceExceptionDataVo> listAttendanceExceptions(AttendanceExceptionDto attendanceExceptionDto);

    /**
     * 核实考勤异常
     * @param map 参数
     * @return true or false
     */
    boolean verifyAttendanceException(Map<String, Object> map);

    /**
     * 考勤异常 - 修改类型
     * @param map 参数
     * @return true or false
     */
    boolean modifyAttendanceExceptionDataType(Map<String, Object> map);

    /**
     * 获取考勤异常信息
     * @param map 参数
     * @return List
     */
    List<AttendanceExceptionDataPo> getAttendanceExceptionInfo(Map<String, Object> map);

    /**
     * 根据日期列表获取考勤异常数据
     * @param dateList
     * @return
     */
    List<AttendanceExceptionDataPo> getAttendanceExceptionInfoByDateStr(List<String> dateList);

    /**
     * 根据id删除异常数据
     * @param idList 需要删除的 id list
     * @return
     */
    int deleteAttendanceExceptionByIds(List<String> idList);
}
