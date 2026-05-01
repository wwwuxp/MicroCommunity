package com.java110.job.task.attendanceException;

import com.java110.dto.attendance.AttendanceFileDto;
import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.dto.task.TaskDto;
import com.java110.entity.attendance.AttendanceExceptionMethodParam;
import com.java110.intf.community.IAttendanceExceptionInnerServiceSMO;
import com.java110.intf.community.IAttendanceFileInnerServiceSMO;
import com.java110.intf.community.IAttendanceRecordInnerServiceSMO;
import com.java110.intf.pubcar.IOutGoingPersonnelInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.attendanceException.AttendanceExceptionDataPo;
import com.java110.po.attendanceException.AttendanceExceptionPo;
import com.java110.po.attendanceFile.AttendanceFilePo;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.attendanceRecord.ApiAttendanceRecordDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 生成考勤异常数据
 * 暂时没用了，已经迁移到front模块下
 * @author hh
 * @date 2021/9/27
 */
@Component
public class GenerateAttendanceExceptionTemplate extends TaskSystemQuartz {

    @Autowired
    private IAttendanceRecordInnerServiceSMO attendanceRecordInnerServiceSMOImpl;

    @Autowired
    private IAttendanceExceptionInnerServiceSMO attendanceExceptionInnerServiceSMOImpl;

    @Autowired
    private IOutGoingPersonnelInnerServiceSMO outGoingPersonnelInnerServiceSMOImpl;

    @Autowired
    private IAttendanceFileInnerServiceSMO attendanceFileInnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        // logger.debug("开始生成考勤异常数据：" + taskDto.getTaskName());

        // 查询考勤记录数据(实际情况只查询前一天的数据)
        AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
        LocalDate localDate = LocalDate.now();
        LocalDate beforeDay = localDate.plusDays(-1);
        String beforeDayStr = beforeDay.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));

        attendanceRecordDto.setLessAttendanceDate(beforeDayStr);
        attendanceRecordDto.setMoreAttendanceDate(beforeDayStr);
        List<ApiAttendanceRecordDataVo> attendanceRecords =
                attendanceRecordInnerServiceSMOImpl.queryAttendanceRecords(attendanceRecordDto);

        // 存放异常数据
        Set<AttendanceExceptionPo> attendanceExceptionPos = new HashSet<>();
        List<AttendanceExceptionDataPo> attendanceExceptionDataPos = new ArrayList<>();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M);
        // 拒绝硬编码，抽取此时间到redis缓存中
        String am = "09:05:00";
        String pm = "18:00:00";
        LocalTime fixedTimeAm = LocalTime.parse(am, timeFormatter);
        LocalTime fixedTimePm = LocalTime.parse(pm, timeFormatter);

        // 分析考勤记录
        AttendanceExceptionPo attendanceExceptionPo;
        for (ApiAttendanceRecordDataVo attendanceRecord : attendanceRecords) {
            // 员工基本信息
            attendanceExceptionPo = new AttendanceExceptionPo();
            attendanceExceptionPo.setDepartment(attendanceRecord.getDepartment());
            // attendanceExceptionPo.setPosition(attendanceRecord.getPosition());
            attendanceExceptionPo.setName(attendanceRecord.getName());

            String attendanceDateStr = attendanceRecord.getAttendanceDate();
            LocalDate attendanceDate = LocalDate.parse(attendanceDateStr, dateFormatter);
            String attendanceMonth = attendanceDate.getYear() + "-" + attendanceDate.getMonthValue() + "-01";

            attendanceExceptionPo.setAttendanceMonth(attendanceMonth);
            attendanceExceptionPo.setIsVerify(AttendanceExceptionPo.VERIFY_FALSE);
            attendanceExceptionPo.setExceptionId(attendanceRecord.getRecordId());
            attendanceExceptionPos.add(attendanceExceptionPo);

            /*
                哪些考勤记录会被判断为异常：
                1、实际上班时间大于9:05 或 最后一个离开道闸的时间小于18点, 且该时间段并未成功申请外出登记, 且该时间段并未申请请假或调休；
                2、当天道闸没有进出时间, 且当天并未进行外出申请, 且当天没工作日, 且该时间段并未申请请假或者调休。
             */
            String actualStartWorkTimeStr = attendanceRecord.getActualStartWorkTime();
            String actualEndWorkTimeStr = attendanceRecord.getActualEndWorkTime();
            LocalTime actualStartWorkTime = null;
            LocalTime actualEndWorkTime = null;
            if (StringUtils.isNotBlank(actualStartWorkTimeStr)) {
                actualStartWorkTime = LocalTime.parse(actualStartWorkTimeStr + ":00", timeFormatter);
            }
            if (StringUtils.isNotBlank(actualEndWorkTimeStr)) {
                actualEndWorkTime = LocalTime.parse(actualEndWorkTimeStr + ":00", timeFormatter);
            }

            if (Objects.isNull(actualStartWorkTime) && Objects.isNull(actualEndWorkTime)) {
                // 当天道闸没有进出时间(当天上下午都没有打卡)
                if (notClockAmAndPm(am, pm, attendanceRecord, attendanceDate)){
                    continue;
                }

            } else if (Objects.isNull(actualStartWorkTime) || Objects.isNull(actualEndWorkTime)) {
                // 当天上班未打卡，下班打卡 或 当天上班打卡，下班未打卡
                if (clockAmOrPm(dateTimeFormatter, fixedTimeAm, fixedTimePm, attendanceRecord, attendanceDate, actualStartWorkTime, actualEndWorkTime)){
                    continue;
                }

            } else if (actualStartWorkTime.isAfter(fixedTimeAm) || actualEndWorkTime.isBefore(fixedTimePm)) {
                // 上下班都打卡，且实际上班时间大于9:05 或 最后一个离开道闸的时间小于18点
                if (clockAmAndPm(dateTimeFormatter, dateFormatter, fixedTimeAm, fixedTimePm, attendanceRecord, attendanceDate, actualStartWorkTime, actualEndWorkTime)){
                    continue;
                }

            }
            putExceptionDataToList(attendanceExceptionDataPos, attendanceRecord, attendanceMonth);
        }

        try {
            // 根据唯一索引，有则更新，没有则插入(前提是数据库中已经创建了唯一索引)
            if (CollectionUtils.isNotEmpty(attendanceExceptionPos)){
                attendanceExceptionInnerServiceSMOImpl.upsertAttendanceExceptionBatch(attendanceExceptionPos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (AttendanceExceptionDataPo exceptionDataPo : attendanceExceptionDataPos) {
            String name = exceptionDataPo.getName();
            String department = exceptionDataPo.getDepartment();
            String position = exceptionDataPo.getPosition();
            String attendanceMonth = exceptionDataPo.getAttendanceMonth();
            // 获取到exceptionId
            String exceptionId = attendanceExceptionInnerServiceSMOImpl.getExceptionIdByNameAndDepartmentAndPositionAndAttendanceMonth(name, department, position, attendanceMonth);

            exceptionDataPo.setExceptionId(exceptionId);

        }

        // 批量插入考勤异常数据
        if (CollectionUtils.isNotEmpty(attendanceExceptionDataPos)){
            attendanceExceptionInnerServiceSMOImpl.insertAttendanceExceptionDataBatch(attendanceExceptionDataPos);
        }

    }

    /**
     * 上午下午都打卡
     * @param dateTimeFormatter dateTimeFormatter
     * @param dateFormatter dateFormatter
     * @param fixedTimeAm fixedTimeAm
     * @param fixedTimePm fixedTimePm
     * @param attendanceRecord attendanceRecord
     * @param attendanceDate attendanceDate
     * @param actualStartWorkTime actualStartWorkTime
     * @param actualEndWorkTime actualEndWorkTime
     * @return true:有外出、请假或调休,false:没有外出、请假或调休
     */
    private boolean clockAmAndPm(DateTimeFormatter dateTimeFormatter, DateTimeFormatter dateFormatter, LocalTime fixedTimeAm,LocalTime fixedTimePm,
                                 ApiAttendanceRecordDataVo attendanceRecord, LocalDate attendanceDate, LocalTime actualStartWorkTime, LocalTime actualEndWorkTime) {
        UserInfoDto userInfoDto = getUserInfoDto(attendanceRecord, attendanceRecord.getAttendanceDate());
        List<OutGoingDto> outGoingTimes = outGoingPersonnelInnerServiceSMOImpl.listOutGoingTimesByUserInfo(userInfoDto);

        // 判断外出时间是否在异常打卡时间范围内
        if (CollectionUtils.isNotEmpty(outGoingTimes)) {
            boolean outGoingFlag = false;
            for (OutGoingDto outGoingTime : outGoingTimes) {
                String outGoingStartDateTimeStr = outGoingTime.getStartTime();
                String outGoingEndDateTimeStr = outGoingTime.getEndTime();
                LocalDateTime outGoingStartDateTime = LocalDateTime.parse(outGoingStartDateTimeStr, dateTimeFormatter);
                LocalDateTime outGoingEndDateTime = LocalDateTime.parse(outGoingEndDateTimeStr, dateTimeFormatter);

                // 上午打卡异常
                if (actualStartWorkTime.isAfter(fixedTimeAm)) {
                    LocalDateTime actualStartWorkDateTime = LocalDateTime.of(attendanceDate, actualStartWorkTime);
                    boolean boolAm5 = actualStartWorkDateTime.isAfter(outGoingStartDateTime) && (actualStartWorkDateTime.isBefore(outGoingEndDateTime)
                            || actualStartWorkDateTime.isEqual(outGoingEndDateTime));
                    if (boolAm5) {
                        outGoingFlag = true;
                        break;
                    }
                }

                // 下午打卡异常
                if (actualEndWorkTime.isBefore(fixedTimePm)) {
                    LocalDateTime actualEndWorkDateTime = LocalDateTime.of(attendanceDate, actualEndWorkTime);
                    boolean boolPm5 = (actualEndWorkDateTime.isAfter(outGoingStartDateTime) || actualEndWorkDateTime.isEqual(outGoingStartDateTime))
                            && actualEndWorkDateTime.isBefore(outGoingEndDateTime);
                    if (boolPm5) {
                        outGoingFlag = true;
                        break;
                    }
                }
            }

            // 经过过滤，有符合条件的，进入考勤异常
            if (outGoingFlag) {
                return true;
            }
        }

        // 该时间段是否申请请假或调休
        AttendanceExceptionMethodParam methodParam = new AttendanceExceptionMethodParam();
        methodParam.setDateTimeFormatter(dateTimeFormatter);
        methodParam.setDateFormatter(dateFormatter);
        methodParam.setFixedTimeAm(fixedTimeAm);
        methodParam.setFixedTimePm(fixedTimePm);
        methodParam.setAttendanceDate(attendanceDate);
        methodParam.setActualStartWorkTime(actualStartWorkTime);
        methodParam.setActualEndWorkTime(actualEndWorkTime);
        methodParam.setUserInfoDto(userInfoDto);

        return handleVacationAndRest(methodParam);
    }

    /**
     * 上班或下班未打卡
     * @param dateTimeFormatter dateTimeFormatter
     * @param fixedTimeAm fixedTimeAm
     * @param fixedTimePm fixedTimePm
     * @param attendanceRecord attendanceRecord
     * @param attendanceDate attendanceDate
     * @param actualStartWorkTime actualStartWorkTime
     * @param actualEndWorkTime actualEndWorkTime
     * @return true:有外出、请假或调休,false:没有外出、请假或调休
     */
    private boolean clockAmOrPm(DateTimeFormatter dateTimeFormatter, LocalTime fixedTimeAm, LocalTime fixedTimePm,
                                ApiAttendanceRecordDataVo attendanceRecord, LocalDate attendanceDate,
                                LocalTime actualStartWorkTime, LocalTime actualEndWorkTime) {
        // 该时间段是否外出
        String attendanceDateStr = attendanceRecord.getAttendanceDate();
        UserInfoDto userInfoDto = getUserInfoDto(attendanceRecord, attendanceDateStr);
        List<OutGoingDto> outGoingTimes = outGoingPersonnelInnerServiceSMOImpl.listOutGoingTimesByUserInfo(userInfoDto);

        if (CollectionUtils.isNotEmpty(outGoingTimes)) {
            for (OutGoingDto outGoingTime : outGoingTimes) {
                if (commonHandleClockAmOrPm(dateTimeFormatter, fixedTimeAm, fixedTimePm, attendanceDate, actualStartWorkTime, actualEndWorkTime, outGoingTime.getStartTime(), outGoingTime.getEndTime())) {
                    return true;
                }
            }
        }

        // 该时间段是否申请休假（不进行判断，因为休假数据中开始时间和结束时间是以天为单位）

        // 该时间段是否申请调休
        userInfoDto.setTableName(AttendanceFilePo.TABLE_REST);
        List<AttendanceFileDto> restTimes = attendanceFileInnerServiceSMOImpl.listVacationOrRestTimesByUserInfo(userInfoDto);

        if (CollectionUtils.isNotEmpty(restTimes)) {
            for (AttendanceFileDto restTime : restTimes) {
                if (commonHandleClockAmOrPm(dateTimeFormatter, fixedTimeAm, fixedTimePm, attendanceDate, actualStartWorkTime, actualEndWorkTime, restTime.getStartTime(), restTime.getEndTime())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 统一处理上班打卡，下班未打卡 或 上班未打卡，下班打卡
     * @param dateTimeFormatter dateTimeFormatter
     * @param fixedTimeAm fixedTimeAm
     * @param fixedTimePm fixedTimePm
     * @param attendanceDate attendanceDate
     * @param actualStartWorkTime actualStartWorkTime
     * @param actualEndWorkTime actualEndWorkTime
     * @param startTime startTime
     * @param endTime endTime
     * @return true or false
     */
    private boolean commonHandleClockAmOrPm(DateTimeFormatter dateTimeFormatter, LocalTime fixedTimeAm, LocalTime fixedTimePm, LocalDate attendanceDate,
                                            LocalTime actualStartWorkTime, LocalTime actualEndWorkTime, String startTime, String endTime) {
        LocalDateTime fixedStartWorkDateTime = LocalDateTime.of(attendanceDate, fixedTimeAm);
        LocalDateTime fixedEndWorkDateTime = LocalDateTime.of(attendanceDate, fixedTimePm);

        LocalDateTime outGoingOrRestStartTime = LocalDateTime.parse(startTime, dateTimeFormatter);
        LocalDateTime outGoingOrRestEndTime = LocalDateTime.parse(endTime, dateTimeFormatter);

        if (Objects.isNull(actualStartWorkTime)) {
            // 上班没打卡。外出或调休开始时间在9:05之前或与9:05相等 且 外出或调休结束时间在9:05之后
            boolean boolAm1 = outGoingOrRestEndTime.isAfter(fixedStartWorkDateTime) && (outGoingOrRestStartTime.isBefore(fixedStartWorkDateTime)
                    || outGoingOrRestStartTime.isEqual(fixedStartWorkDateTime));
            if (boolAm1) {
                return true;
            }

            // 下班打卡，是异常打卡
            if (actualEndWorkTime.isBefore(fixedTimePm)) {
                // 实际打卡时间是否在外出或调休时间段内,在外出或调休时间段外，加入异常
                LocalDateTime actualEndWorkDateTime = LocalDateTime.of(attendanceDate, actualEndWorkTime);
                return (actualEndWorkDateTime.isAfter(outGoingOrRestStartTime) || actualEndWorkDateTime.isEqual(outGoingOrRestStartTime))
                        && actualEndWorkDateTime.isBefore(outGoingOrRestEndTime);
            }

        } else {
            // 下班没打卡。外出或调休开始时间在18:00之前 且 外出或调休结束时间在18:00之后或与18:00相等
            boolean boolPm2 = outGoingOrRestStartTime.isBefore(fixedEndWorkDateTime) && (outGoingOrRestEndTime.isAfter(fixedEndWorkDateTime)
                    || outGoingOrRestEndTime.isEqual(fixedEndWorkDateTime));
            if (boolPm2) {
                return true;
            }

            // 上班打卡，是异常打卡
            if (actualStartWorkTime.isAfter(fixedTimeAm)) {
                // 实际打卡时间是否在外出或调休时间段内,在外出或调休时间段外，加入异常
                LocalDateTime actualStartWorkDateTime = LocalDateTime.of(attendanceDate, actualStartWorkTime);
                return actualStartWorkDateTime.isAfter(outGoingOrRestStartTime) && (actualStartWorkDateTime.isBefore(outGoingOrRestEndTime)
                        || actualStartWorkDateTime.isEqual(outGoingOrRestEndTime));
            }
        }
        return false;
    }

    /**
     * 上下班都没有打卡
     *
     * @param am                         上午
     * @param pm                         下午
     * @param attendanceRecord           考勤记录
     * @param attendanceDate             考勤日期类型
     */
    private boolean notClockAmAndPm(String am, String pm, ApiAttendanceRecordDataVo attendanceRecord, LocalDate attendanceDate) {
        // 当天是否是工作日
        int week = attendanceDate.getDayOfWeek().getValue();
        // 当天非周六周天（是否是国家节假日未实现）
        if (week == 6 || week == 7) {
            return true;
        }

        // 该时间段是否成功申请外出登记
        String attendanceDateStr = attendanceRecord.getAttendanceDate();
        UserInfoDto userInfoDto = getUserInfoDto(attendanceRecord, attendanceDateStr);
        userInfoDto.setAttendanceDate(null);
        userInfoDto.setFixedStartWorkDateTime(attendanceDateStr + " " + am);
        userInfoDto.setFixedEndWorkDateTime(attendanceDateStr + " " + pm);
        boolean isOutGoing = outGoingPersonnelInnerServiceSMOImpl.isOutGoing(userInfoDto);
        if (isOutGoing) {
            return true;
        }

        // 当天未打卡是否处于调休中
        userInfoDto.setTableName(AttendanceFilePo.TABLE_REST);
        boolean isRest = attendanceFileInnerServiceSMOImpl.isVacationOrRest(userInfoDto);
        if (isRest) {
            return true;
        }

        // 当天未打卡是否处于休假中
        userInfoDto.setTableName(AttendanceFilePo.TABLE_VACATION);
        userInfoDto.setAttendanceDate(attendanceDateStr);
        userInfoDto.setFixedStartWorkDateTime(null);
        userInfoDto.setFixedEndWorkDateTime(null);
        return attendanceFileInnerServiceSMOImpl.isVacationOrRest(userInfoDto);
    }


    /**
     * 该时间段是否申请请假或调休
     *
     * @param methodParam 参数封装
     */
    private boolean handleVacationAndRest(AttendanceExceptionMethodParam methodParam) {
        // 该时间段是否申请请假
        UserInfoDto userInfoDto = methodParam.getUserInfoDto();
        DateTimeFormatter dateFormatter = methodParam.getDateFormatter();
        LocalDate attendanceDate = methodParam.getAttendanceDate();

        userInfoDto.setTableName(AttendanceFilePo.TABLE_VACATION);
        List<AttendanceFileDto> vacationTimes = attendanceFileInnerServiceSMOImpl.listVacationOrRestTimesByUserInfo(userInfoDto);
        if (CollectionUtils.isNotEmpty(vacationTimes)) {
            for (AttendanceFileDto vacationTime : vacationTimes) {

                String vacationStartDateStr = vacationTime.getStartTime();
                String vacationEndDateStr = vacationTime.getEndTime();
                LocalDate vacationStartDate = LocalDate.parse(vacationStartDateStr, dateFormatter);
                LocalDate vacationEndDate = LocalDate.parse(vacationEndDateStr, dateFormatter);

                // 开始休假日期或结束休假日期和考勤异常日期相等，不进入考勤异常
                if (attendanceDate.isEqual(vacationStartDate) || attendanceDate.isEqual(vacationEndDate)) {
                    return true;
                }
            }
        }

        // 该时间段是否申请调休
        DateTimeFormatter dateTimeFormatter = methodParam.getDateTimeFormatter();
        LocalTime fixedTimeAm = methodParam.getFixedTimeAm();
        LocalTime fixedTimePm = methodParam.getFixedTimePm();
        LocalTime actualStartWorkTime = methodParam.getActualStartWorkTime();
        LocalTime actualEndWorkTime = methodParam.getActualEndWorkTime();

        userInfoDto.setTableName(AttendanceFilePo.TABLE_REST);
        List<AttendanceFileDto> restTimes = attendanceFileInnerServiceSMOImpl.listVacationOrRestTimesByUserInfo(userInfoDto);
        if (CollectionUtils.isNotEmpty(restTimes)) {
            for (AttendanceFileDto restTime : restTimes) {

                String restStartDateTimeStr = restTime.getStartTime();
                String restEndDateTimeStr = restTime.getEndTime();
                LocalDateTime restStartDateTime = LocalDateTime.parse(restStartDateTimeStr, dateTimeFormatter);
                LocalDateTime restEndDateTime = LocalDateTime.parse(restEndDateTimeStr, dateTimeFormatter);

                // 上班打卡异常
                if (actualStartWorkTime.isAfter(fixedTimeAm)) {
                    // 实际打卡时间是否在调休时间段内，在调休时间段外，加入异常
                    LocalDateTime actualStartWorkDateTime = LocalDateTime.of(attendanceDate, actualStartWorkTime);
                    boolean boolAm6 = actualStartWorkDateTime.isAfter(restStartDateTime) && (actualStartWorkDateTime.isBefore(restEndDateTime)
                            || actualStartWorkDateTime.isEqual(restEndDateTime));
                    if (boolAm6) {
                        return true;
                    }
                }

                // 下班打卡异常
                if (actualEndWorkTime.isBefore(fixedTimePm)) {
                    // 实际打卡时间是否在调休时间段内，在调休时间段外，加入异常
                    LocalDateTime actualEndWorkDateTime = LocalDateTime.of(attendanceDate, actualEndWorkTime);
                    boolean boolPm6 = (actualEndWorkDateTime.isAfter(restStartDateTime) || actualEndWorkDateTime.isEqual(restStartDateTime))
                            && actualEndWorkDateTime.isBefore(restEndDateTime);
                    if (boolPm6) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 创建UserInfoDto对象
     *
     * @param attendanceRecord  考勤记录
     * @param attendanceDateStr 考勤日期字符串
     * @return UserInfoDto
     */
    private UserInfoDto getUserInfoDto(ApiAttendanceRecordDataVo attendanceRecord, String attendanceDateStr) {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setName(attendanceRecord.getName());
        userInfoDto.setDepartment(attendanceRecord.getDepartment());
        // userInfoDto.setPosition(attendanceRecord.getPosition());
        userInfoDto.setAttendanceDate(attendanceDateStr);
        return userInfoDto;
    }

    /**
     * 将考勤记录中异常数据放置异常数据列表中
     *
     * @param attendanceExceptionDataPos 异常数据列表
     * @param attendanceRecord           考勤记录
     */
    private void putExceptionDataToList(List<AttendanceExceptionDataPo> attendanceExceptionDataPos, ApiAttendanceRecordDataVo attendanceRecord, String attendanceMonth) {
        AttendanceExceptionDataPo attendanceExceptionDataPo = new AttendanceExceptionDataPo();
        attendanceExceptionDataPo.setAttendanceDate(attendanceRecord.getAttendanceDate());
        attendanceExceptionDataPo.setActualStartWorkTime(attendanceRecord.getActualStartWorkTime());
        attendanceExceptionDataPo.setActualEndWorkTime(attendanceRecord.getActualEndWorkTime());
        attendanceExceptionDataPo.setType(attendanceRecord.getType());

        // 以下三个字段是attendance_exception_data表中没有的，只为了能够在数据库中批量获取到exceptionId
        attendanceExceptionDataPo.setName(attendanceRecord.getName());
        attendanceExceptionDataPo.setDepartment(attendanceRecord.getDepartment());
        // attendanceExceptionDataPo.setPosition(attendanceRecord.getPosition());
        attendanceExceptionDataPo.setAttendanceMonth(attendanceMonth);

        attendanceExceptionDataPos.add(attendanceExceptionDataPo);
    }

}
