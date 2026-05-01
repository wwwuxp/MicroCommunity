package com.java110.community.dao;

import com.java110.dto.attendance.AttendanceFileDto;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.vo.api.attendanceFile.ApiAttendanceFileDataVo;

import java.util.List;

/**
 * @author hh
 * create date 2021/9/24
 */
public interface IAttendanceFileServiceDao {

    /**
     * 查询导入文件数量
     * @param attendanceFileDto 查询条件
     * @return int
     */
    int queryAttendanceFilesCount(AttendanceFileDto attendanceFileDto);

    /**
     * 查询导入文件列表
     * @param attendanceFileDto 查询条件
     * @return list
     */
    List<ApiAttendanceFileDataVo> listAttendanceFiles(AttendanceFileDto attendanceFileDto);

    /**
     * 删除导入的文件
     * @param fileId 文件id
     * @return true or false
     */
    boolean deleteAttendanceFileById(Integer fileId);

    /**
     * 删除休假数据
     * @param fileId 文件id
     * @param tableName 表名
     * @return true or false
     */
    boolean deleteAttendanceFileDataByFileId(Integer fileId, String tableName);

    /**
     * 根据姓名、部门、职位和考勤日期获取休假数据
     * @param userInfoDto 用户基本信息
     * @return startTime endTime
     */
    List<AttendanceFileDto> listVacationOrRestTimesByUserInfo(UserInfoDto userInfoDto);


    /**
     * 根据姓名、部门、职位和考勤日期获取休假数据 （包含在提供的日期中即查出那条休假数据）
     * @param userInfoDto 用户基本信息
     * @return startTime endTime
     */
    List<AttendanceFileDto> listVacationAndRestTimesByUserInfo(UserInfoDto userInfoDto);

    /**
     * 当天上下班未打卡，是否是在休假或调休
     * @param userInfoDto 查询条件
     * @return integer
     */
    Integer isVacationOrRest(UserInfoDto userInfoDto);

}
