package com.java110.front.controller;

import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.front.smo.attendanceManagement.IAttendanceException;
import com.java110.utils.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * 由于考勤异常数据是定时任务每天晚上计算前一天的数据，如果要修改之前的数据，每次都修改一下代码，重启一次服务，太耗费时间，
 * 所以此类的作用是做到请求一下这个接口，能把n天前的考勤异常数据都计算出来
 * @author hh
 * @date 2022/1/21
 */
@RestController
@RequestMapping("/callComponent/attendanceData")
public class AttendaceExceptionController {


    @Autowired
    IAttendanceException attendanceException;

    @GetMapping("start")
    public String startGenerateData(@RequestParam("day") String fromDayStr , @RequestParam(required = false,name = "endDay") String endDay , @RequestParam(required = false,name = "isDel") String isDel){
        if (Objects.isNull(fromDayStr)){
            return "day参数不能为空";
        }
	if (!Objects.isNull(isDel)){
            attendanceException.deleteByDate(fromDayStr, endDay);
            return "删除成功";
        }
        LocalDate fromDay = LocalDate.parse(fromDayStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B)).plusDays(1);
        LocalDate today = StringUtils.isNotEmpty(endDay) ? LocalDate.parse(endDay).plusDays(1) : LocalDate.now().plusDays(1);
        long between = ChronoUnit.DAYS.between(fromDay, today);
        int count = 0;
        while (between >= 0) {

            // 查询考勤记录数据(实际情况只查询前一天的数据)
            AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();

            LocalDate beforeDay = fromDay.plusDays(count);
            attendanceException.process(beforeDay);
            count++;
            between--;
        }
        return "执行成功";
    }

    @GetMapping("amStart")
    public String AMProcess(@RequestParam("day") String day) {
        // 查询考勤记录数据(查询当前天0点-13点的数据)
        LocalDate localDate = LocalDate.parse(day);
        attendanceException.AMProcess(localDate);
        return "执行成功";
    }

}
