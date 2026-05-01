package com.java110.front.smo.attendanceManagement.impl;

import com.java110.front.smo.attendanceManagement.IAttendanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;
import com.java110.utils.util.DateUtil;
import java.time.LocalDate;

/**
 * 生成考勤异常数据
 *
 * @author hh
 * @date 2021/9/27
 */
@Component
public class GenerateAttendanceExceptionTask {

    @Autowired
    IAttendanceException attendanceException;

    @Scheduled(cron = "0 0 3 * * ? ")
    protected void deleteData() {
        // logger.debug("开始生成考勤异常数据：" + taskDto.getTaskName());
        //
        LocalDate localDate = LocalDate.now().plusDays(-1);
        String beforeDayStr = localDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        attendanceException.deleteByDate(beforeDayStr,beforeDayStr);
    }   
 
    @Scheduled(cron = "0 0 5 * * ? ")
    protected void process() {
        // logger.debug("开始生成考勤异常数据：" + taskDto.getTaskName());

        // 查询考勤记录数据(实际情况只查询前一天的数据)
        LocalDate localDate = LocalDate.now();
        attendanceException.process(localDate);
    }

    // 上午执行的异常告警任务 只处理迟到的人 早退和旷工的人暂时不做处理
    @Scheduled(cron = "0 0 13 * * ? ")
    public void AMProcess() {
        // 查询考勤记录数据(查询当前天0点-13点的数据)
        LocalDate localDate = LocalDate.now();
        attendanceException.AMProcess(localDate);
    }

}
