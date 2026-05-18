package com.java110.front.mapper;

import com.java110.po.attendanceException.AttendanceExceptionDataPo;
import com.java110.po.attendanceException.AttendanceExceptionPo;
import org.apache.ibatis.annotations.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * 由于使用项目原有的sqlSessionTemplate方式进行导入数据，出现 ON DUPLICATE KEY UPDATE 方式总是报错，不能正常新增修改数据，
 * 故使用这种Mapper接口的方式
 * @author hh
 * @date 2021/11/15
 */
public interface AttendanceExceptionMapper {

    /**
     * 插入更新考勤异常员工基本信息表数据
     * @param attendanceExceptionPos 数据
     */
    void upsertAttendanceExceptionBatch(Set<AttendanceExceptionPo> attendanceExceptionPos);

    /**
     * 批量插入考勤异常数据
     * @param attendanceExceptionDataPos 数据
     */
    void insertAttendanceExceptionDataBatch(List<AttendanceExceptionDataPo> attendanceExceptionDataPos);

   /**
     * 删除固定的某一段时间的考勤异常
     * @param startDate
     * @param endDate
     */
    void deleteByDate(@Param("startDate") String startDate,@Param("endDate") String endDate);

    /**
     * 删除指定的数据
     * @param needRemoveExceptionDataIds 数据
     */
    void deleteByIds(List<String> needRemoveExceptionDataIds);

    List<AttendanceExceptionDataPo> selectDataByActStartAndEndDate(@Param("name") String name,
                                                                   @Param("department") String department,
                                                                   @Param("startDate") LocalDate vacationActStartDate,
                                                                   @Param("endDate") LocalDate vacationActEndDate);

    List<AttendanceExceptionDataPo> selectByIds(@Param("ids") List<String> needRemoveExceptionDataIds);

    void batchInsert(@Param("list") List<AttendanceExceptionDataPo> pos);
}
