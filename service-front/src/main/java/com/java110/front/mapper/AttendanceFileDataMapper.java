package com.java110.front.mapper;

import com.java110.entity.attendance.ImportOverTime;
import com.java110.entity.attendance.ImportRest;
import com.java110.entity.attendance.ImportVacation;
import com.java110.po.attendanceException.AttendanceExceptionDataPo;
import com.java110.po.attendanceFile.AttendanceFilePo;

import java.time.LocalDate;
import java.util.List;

/**
 * 由于使用项目原有的sqlSessionTemplate方式进行导入数据，出现 ON DUPLICATE KEY UPDATE 方式总是报错，不能正常新增修改数据，
 * 故使用这种Mapper接口的方式
 * @author hh
 * @date 2021/10/19
 */
public interface AttendanceFileDataMapper {

    /**
     * 保存导入文件基本信息
     * @param attendanceFilePo 数据
     */
    void saveFileInfo(AttendanceFilePo attendanceFilePo);

    /**
     * 个人休假数据批量插入
     * @param cachedVacationDataList 数据列表
     */
    void saveVacationBatch(List<ImportVacation> cachedVacationDataList);

    /**
     * 个人调休数据批量插入
     * @param cachedRestDataList 数据列表
     */
    void saveRestBatch(List<ImportRest> cachedRestDataList);

    /**
     * 个人加班数据批量插入
     * @param cachedOverTimeDataList 数据列表
     */
    void saveOverTimeBatch(List<ImportOverTime> cachedOverTimeDataList);

    List<AttendanceExceptionDataPo> selectDataByActStartAndEndDate(LocalDate vacationActStartDate, LocalDate vacationActEndDate);

}
