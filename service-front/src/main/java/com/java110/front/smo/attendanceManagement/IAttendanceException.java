package com.java110.front.smo.attendanceManagement;

import java.time.LocalDate;

/**
 * @author lmf
 * @date 2022/7/12
 */
public interface IAttendanceException {


    void deleteByDate(String startDate, String endDate);

    /**
     * 生成考勤异常记录
     * @param localDate
     */
    void process(LocalDate localDate);

    /**
     * 生成上午考勤异常记录
     * @param date
     */
    void AMProcess(LocalDate date);
}
