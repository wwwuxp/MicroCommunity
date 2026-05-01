package com.java110.front.smo.attendanceManagement.impl;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.attendance.AttendanceFileDto;
import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.attendance.AttendanceExceptionMethodParam;
import com.java110.front.mapper.AttendanceExceptionMapper;
import com.java110.front.smo.attendanceManagement.IAttendanceException;
import com.java110.intf.community.IAttendanceExceptionInnerServiceSMO;
import com.java110.intf.community.IAttendanceFileInnerServiceSMO;
import com.java110.intf.community.IAttendanceNotWorkDateSMO;
import com.java110.intf.community.IAttendanceRecordInnerServiceSMO;
import com.java110.intf.pubcar.IOutGoingPersonnelInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.attendanceException.AttendanceExceptionDataPo;
import com.java110.po.attendanceException.AttendanceExceptionPo;
import com.java110.po.attendanceFile.AttendanceFilePo;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.attendanceRecord.ApiAttendanceRecordDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lmf
 * @date 2022/7/12
 */
@Component
public class AttendanceExceptionImpl implements IAttendanceException {

    private static final Logger log = LoggerFactory.getLogger(GenerateAttendanceExceptionTask.class);

    @Autowired
    private IAttendanceRecordInnerServiceSMO attendanceRecordInnerServiceSMOImpl;

    @Autowired
    private IAttendanceExceptionInnerServiceSMO attendanceExceptionInnerServiceSMOImpl;

    @Autowired
    private IOutGoingPersonnelInnerServiceSMO outGoingPersonnelInnerServiceSMOImpl;

    @Autowired
    private IAttendanceFileInnerServiceSMO attendanceFileInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private AttendanceExceptionMapper attendanceExceptionMapper;

    @Autowired
    private IAttendanceNotWorkDateSMO attendanceNotWorkDateSMOImpl;

    /**
     * 2022/07/05
     * 添加非工作日 非工作日的数据不生成考勤异常
     * @param strDate 日期字符串
     * @return 返回true表示为休息日，返回false表示未添加非工作日
     */
    private boolean isOffDay(String strDate){
        int isOffDay = attendanceNotWorkDateSMOImpl.whetherTheDateIsARestDay(strDate);
        return isOffDay > 0;
    }

    @Override
    public void deleteByDate(String startDate, String endDate) {
        attendanceExceptionMapper.deleteByDate(startDate,endDate);
    }


    @Override
    public void process(LocalDate localDate) {
        // logger.debug("开始生成考勤异常数据：" + taskDto.getTaskName());

        // 查询考勤记录数据(实际情况只查询前一天的数据)
        AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
        // LocalDate localDate = LocalDate.now();
        LocalDate beforeDay = localDate.plusDays(-1);
        String beforeDayStr = beforeDay.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
	//attendanceExceptionMapper.deleteByDate(beforeDayStr,beforeDayStr);
        if(isOffDay(beforeDayStr)){
            log.error("{} 生成考勤异常定时任务运行，为休息日，不记录考勤异常 ", beforeDayStr);
            return;
        }
        // 获取到所有用户
        List<UserDto> allUsers = userInnerServiceSMOImpl.getAllUsers();
        //只查询系统中存在的人员，根据名称来进行查询
        List<String> userNameList = allUsers.stream().map(r -> r.getName()).collect(Collectors.toList());
        Map<String,String> ponsitionMap = allUsers.stream().collect(Collectors.toMap(k->k.getName(),v->v.getPosition(),(v1,v2)->v1));
	attendanceRecordDto.setUserNameList(userNameList);
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

        String am = "09:05:59";
        String pm = "17:55:00";
        LocalTime fixedTimeAm = LocalTime.parse(am, timeFormatter);
        LocalTime fixedTimePm = LocalTime.parse(pm, timeFormatter);

        // TO DO 非工作日的

        // 分析考勤记录
        AttendanceExceptionPo attendanceExceptionPo;
        for (ApiAttendanceRecordDataVo attendanceRecord : attendanceRecords) {
            // 员工基本信息
            attendanceExceptionPo = new AttendanceExceptionPo();
            String generatorId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ATTENDANCE_EXCEPTION_ID);
            attendanceExceptionPo.setId(generatorId);
            attendanceExceptionPo.setExceptionId(generatorId);
            attendanceExceptionPo.setDepartment(attendanceRecord.getDepartment());
            attendanceExceptionPo.setPosition(ponsitionMap.get(attendanceRecord.getName()));
            attendanceExceptionPo.setName(attendanceRecord.getName());

            String attendanceDateStr = attendanceRecord.getAttendanceDate();
            LocalDate attendanceDate = LocalDate.parse(attendanceDateStr, dateFormatter);
            String attendanceMonth = attendanceDate.getYear() + "-" + attendanceDate.getMonthValue() + "-01";

            attendanceExceptionPo.setAttendanceMonth(attendanceMonth);
            attendanceExceptionPo.setIsVerify(AttendanceExceptionPo.VERIFY_FALSE);

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
            }else {
                // 是正常
                continue;
            }
            putExceptionDataToList(attendanceExceptionDataPos, attendanceRecord, attendanceMonth);
        }

        // modified by hh 2022/04/16
        // 获取到所有用户
        // List<UserDto> allUsers = userInnerServiceSMOImpl.getAllUsers();
        //到这里已经排除了调休，休假的考勤记录
        //根据人员名称分类，这里是为了避免同名不同部门的考勤混乱
        Map<String, List<UserDto>> userMap = allUsers.stream().collect(Collectors.groupingBy(r -> r.getName()));
        // 遍历存在的考勤记录，修改考勤人员的部门
        attendanceExceptionPos.forEach(r->{
            String name = r.getName();
            List<UserDto> userDtoList = userMap.get(name);
            modifyDepartment(r, userDtoList);
        });
        List<String> nameAndDepartmentExceptionList = attendanceExceptionPos.stream().map(t -> t.getName() + ":" + t.getDepartment()).collect(Collectors.toList());
        for (UserDto userDto : allUsers) {
            if (Objects.isNull(userDto.getOrgName())){
                userDto.setOrgName("无");
            }
            if (Objects.isNull(userDto.getPosition())){
                userDto.setPosition("无");
            }
            if (userDto.getOrgName().contains("物业")){
                continue;
            }
            String nameAndOrgName = userDto.getName() + ":" + userDto.getOrgName();
            if (nameAndDepartmentExceptionList.contains(nameAndOrgName)){
                continue;
            }
            // 不包含，说明此人没有过闸，当天没来
            attendanceExceptionPo = new AttendanceExceptionPo();
            String exceptionId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ATTENDANCE_EXCEPTION_ID);
            attendanceExceptionPo.setId(exceptionId);
            attendanceExceptionPo.setExceptionId(exceptionId);
            attendanceExceptionPo.setDepartment(userDto.getOrgName());
            attendanceExceptionPo.setPosition(userDto.getPosition());
            attendanceExceptionPo.setName(userDto.getName());

            String attendanceMonth = beforeDay.getYear() + "-" + beforeDay.getMonthValue() + "-01";

            attendanceExceptionPo.setAttendanceMonth(attendanceMonth);
            attendanceExceptionPo.setIsVerify(AttendanceExceptionPo.VERIFY_FALSE);

            attendanceExceptionPos.add(attendanceExceptionPo);

            AttendanceExceptionDataPo attendanceExceptionDataPo = new AttendanceExceptionDataPo();
            attendanceExceptionDataPo.setName(userDto.getName());
            attendanceExceptionDataPo.setDepartment(userDto.getOrgName());
            attendanceExceptionDataPo.setPosition(userDto.getPosition());
            attendanceExceptionDataPo.setAttendanceMonth(attendanceMonth);
            String tempExceptionId = getExceptionId(attendanceExceptionDataPo);
            if (StringUtils.isBlank(tempExceptionId)){
                attendanceExceptionDataPo.setExceptionId(exceptionId);
            }else {
                attendanceExceptionDataPo.setExceptionId(tempExceptionId);
            }
            attendanceExceptionDataPo.setAttendanceDate(beforeDayStr);
            // 4:矿工
            attendanceExceptionDataPo.setType(4);
            attendanceExceptionDataPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ATTENDANCE_EXCEPTION_DATA_ID));
            attendanceExceptionDataPos.add(attendanceExceptionDataPo);

        }

        try {
            // 根据唯一索引，有则更新，没有则插入(前提是数据库中已经创建了唯一索引)
            if (CollectionUtils.isNotEmpty(attendanceExceptionPos)){
                attendanceExceptionMapper.upsertAttendanceExceptionBatch(attendanceExceptionPos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (AttendanceExceptionDataPo exceptionDataPo : attendanceExceptionDataPos) {
            if (Objects.nonNull(exceptionDataPo.getExceptionId()) && Objects.nonNull(exceptionDataPo.getId())){
                continue;
            }
            //这里做一个标记，到时候方便查询是什么地方出错
            String mark = exceptionDataPo.getDepartment() + exceptionDataPo.getName();
            List<UserDto> userDtoList = userMap.get(exceptionDataPo.getName());
            modifyDepartment(exceptionDataPo,userDtoList);

            String exceptionId = getExceptionId(exceptionDataPo);

            exceptionDataPo.setExceptionId(exceptionId != null ? exceptionId : mark);
            exceptionDataPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ATTENDANCE_EXCEPTION_DATA_ID));

        }

        // 批量插入考勤异常数据
        if (CollectionUtils.isNotEmpty(attendanceExceptionDataPos)){
            attendanceExceptionMapper.insertAttendanceExceptionDataBatch(attendanceExceptionDataPos);
        }

        //批量删除正常的数据
        List<AttendanceExceptionDataPo> attendanceExceptionInfos = attendanceExceptionInnerServiceSMOImpl.getAttendanceExceptionInfo(beforeDay.getYear() + "-" + beforeDay.getMonthValue() + "-01");
        Map<String, AttendanceExceptionDataPo> dbExceptionInfoKeyAndValueMap = attendanceExceptionInfos
                .stream()
                .collect(Collectors.toMap(t -> t.getName() + t.getDepartment() /*+ t.getAttendanceDate()*/, t -> t, (k1, k2) -> k2));
        List<String> deleteAttendanceExceptionDataIds = new ArrayList<>();
        for (Map.Entry<String, AttendanceExceptionDataPo> item : dbExceptionInfoKeyAndValueMap.entrySet()){
            if (StringUtils.isBlank(item.getValue().getActualStartWorkTime()) || StringUtils.isBlank(item.getValue().getActualStartWorkTime())){
                continue;
            }
            if (LocalTime.parse(item.getValue().getActualStartWorkTime(), timeFormatter).isBefore(fixedTimeAm)
                    && LocalTime.parse(item.getValue().getActualEndWorkTime(), timeFormatter).isAfter(fixedTimePm)){
                deleteAttendanceExceptionDataIds.add(item.getValue().getId());
            }
        }

        if (CollectionUtils.isNotEmpty(deleteAttendanceExceptionDataIds)){
            attendanceExceptionMapper.deleteByIds(deleteAttendanceExceptionDataIds);
        }

    }

    public void modifyDepartment(Object obj, List<UserDto> userDtoList){
        if(obj instanceof AttendanceExceptionDataPo){
            AttendanceExceptionDataPo exceptionDataPo = (AttendanceExceptionDataPo)obj;
            if(userDtoList != null && !userDtoList.isEmpty()){
                //这里遍历查询，如果存在呢那么就表示这条数据没有问题
                boolean flag = false;
                for (UserDto v : userDtoList) {
                    if (v.getName().equals(exceptionDataPo.getName()) && v.getOrgName().equals(exceptionDataPo.getDepartment())) {
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    exceptionDataPo.setDepartment(userDtoList.get(0).getOrgName());
                }
            }
        }else if(obj instanceof AttendanceExceptionPo){
            AttendanceExceptionPo exceptionPo = (AttendanceExceptionPo)obj;
            if(userDtoList != null && !userDtoList.isEmpty()){
                //这里遍历查询，如果存在呢那么就表示这条数据没有问题
                boolean flag = false;
                for (UserDto v : userDtoList) {
                    if (v.getName().equals(exceptionPo.getName()) && v.getOrgName().equals(exceptionPo.getDepartment())) {
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    exceptionPo.setDepartment(userDtoList.get(0).getOrgName());
                }
            }
        }

    }

    // 上午执行的异常告警任务 只处理迟到的人 早退和旷工的人暂时不做处理
    @Override
    public void AMProcess(LocalDate date) {
        if(date == null){
            return;
        }
        // 查询考勤记录数据(查询当前天0点-13点的数据)
        AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
        LocalDate localDate = date;

        // LocalDate beforeDay = localDate.plusDay(-1);
        String beforeDayStr = localDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));

        if(isOffDay(beforeDayStr)){
            log.error("{} 上午执行的异常告警任务，为休息日，不记录考勤异常 ", beforeDayStr);
            return;
        }
        // modified by hh 2022/04/16
        // 获取到所有用户
        List<UserDto> allUsers = userInnerServiceSMOImpl.getAllUsers();
        Map<String,String> ponsitionMap = allUsers.stream().collect(Collectors.toMap(k->k.getName(),v->v.getPosition(),(v1,v2)->v1));
	//只查询系统中存在的人员，根据名称来进行查询
        List<String> userNameList = allUsers.stream().map(r -> r.getName()).collect(Collectors.toList());
        attendanceRecordDto.setUserNameList(userNameList);
        attendanceRecordDto.setLessAttendanceDate(beforeDayStr);
        attendanceRecordDto.setMoreAttendanceDate(beforeDayStr);
        List<ApiAttendanceRecordDataVo> attendanceRecords =
                attendanceRecordInnerServiceSMOImpl.queryAttendanceRecords(attendanceRecordDto);
        System.out.println(attendanceRecords);
        // 存放异常数据
        Set<AttendanceExceptionPo> attendanceExceptionPos = new HashSet<>();
        List<AttendanceExceptionDataPo> attendanceExceptionDataPos = new ArrayList<>();
        //
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M);

        String am = "09:00:59";
        // String pm = "13:00:00";
        LocalTime fixedTimeAm = LocalTime.parse(am, timeFormatter);
        // LocalTime fixedTimePm = LocalTime.parse(pm, timeFormatter);
        //
        // 分析考勤记录
        AttendanceExceptionPo attendanceExceptionPo;
        for (ApiAttendanceRecordDataVo attendanceRecord : attendanceRecords) {
            // 员工基本信息
            attendanceExceptionPo = new AttendanceExceptionPo();
            String exceptionId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ATTENDANCE_EXCEPTION_ID);
            attendanceExceptionPo.setId(exceptionId);
            attendanceExceptionPo.setExceptionId(exceptionId);
            attendanceExceptionPo.setDepartment(attendanceRecord.getDepartment());
            attendanceExceptionPo.setPosition(ponsitionMap.get(attendanceRecord.getName()));
            attendanceExceptionPo.setName(attendanceRecord.getName());

            String attendanceDateStr = attendanceRecord.getAttendanceDate();
            LocalDate attendanceDate = LocalDate.parse(attendanceDateStr, dateFormatter);
            String attendanceMonth = attendanceDate.getYear() + "-" + attendanceDate.getMonthValue() + "-01";

            attendanceExceptionPo.setAttendanceMonth(attendanceMonth);
            attendanceExceptionPo.setIsVerify(AttendanceExceptionPo.VERIFY_FALSE);

            attendanceExceptionPos.add(attendanceExceptionPo);

            /*
                哪些考勤记录会被判断为异常：
                1、实际上班时间大于9:05 , 且该时间段并未成功申请外出登记, 且该时间段并未申请请假或调休；
                2、当天道闸没有进出时间, 且当天并未进行外出申请, 且当天没工作日, 且该时间段并未申请请假或者调休。
             */
            String actualStartWorkTimeStr = attendanceRecord.getActualStartWorkTime();
            // String actualEndWorkTimeStr = attendanceRecord.getActualEndWorkTime();
            LocalTime actualStartWorkTime = null;
            LocalTime actualEndWorkTime = null;
            if (StringUtils.isNotBlank(actualStartWorkTimeStr)) {
                actualStartWorkTime = LocalTime.parse(actualStartWorkTimeStr + ":00", timeFormatter);
            }
            // if (StringUtils.isNotBlank(actualEndWorkTimeStr)) {
            //     actualEndWorkTime = LocalTime.parse(actualEndWorkTimeStr + ":00", timeFormatter);
            // }

            if (Objects.isNull(actualStartWorkTime)) {
                // 当天道闸没有进时间(当天上午没有打卡)
                if (notClockAm(am , attendanceRecord, attendanceDate)){
                    continue;
                }

            } else if (actualStartWorkTime.isAfter(fixedTimeAm)) {
                // 当天上班打卡，且实际上班时间大于9:05
                if (clockAm(dateTimeFormatter,dateFormatter, fixedTimeAm, attendanceRecord, attendanceDate, actualStartWorkTime)){
                    continue;
                }

            }else {
                // 是正常
                continue;
            }
            putExceptionDataToList(attendanceExceptionDataPos, attendanceRecord, attendanceMonth);
        }


        //根据人员名称分类，这里是为了避免同名不同部门的考勤混乱
        Map<String, List<UserDto>> userMap = allUsers.stream().collect(Collectors.groupingBy(r -> r.getName()));
        // 遍历存在的考勤记录，修改考勤人员的部门
        attendanceExceptionPos.forEach(r->{
            String name = r.getName();
            List<UserDto> userDtoList = userMap.get(name);
            modifyDepartment(r, userDtoList);
        });
        List<String> nameAndDepartmentExceptionList = attendanceExceptionPos.stream().map(t -> t.getName() + ":" + t.getDepartment()).collect(Collectors.toList());
        for (UserDto userDto : allUsers) {
            if (Objects.isNull(userDto.getOrgName())){
                userDto.setOrgName("无");
            }
            if (Objects.isNull(userDto.getPosition())){
                userDto.setPosition("无");
            }
            if (userDto.getOrgName().contains("物业")){
                continue;
            }
            String nameAndOrgName = userDto.getName() + ":" + userDto.getOrgName();
            if (nameAndDepartmentExceptionList.contains(nameAndOrgName)){
                continue;
            }
            // 不包含，说明此人没有过闸，上午没来 上午告警没来的人先认定迟到，如果一天都没来凌晨3点定时任务会进行处理
            attendanceExceptionPo = new AttendanceExceptionPo();
            String exceptionId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ATTENDANCE_EXCEPTION_ID);
            attendanceExceptionPo.setId(exceptionId);
            attendanceExceptionPo.setExceptionId(exceptionId);
            attendanceExceptionPo.setDepartment(userDto.getOrgName());
            attendanceExceptionPo.setPosition(userDto.getPosition());
            attendanceExceptionPo.setName(userDto.getName());

            String attendanceMonth = localDate.getYear() + "-" + localDate.getMonthValue() + "-01";

            attendanceExceptionPo.setAttendanceMonth(attendanceMonth);
            attendanceExceptionPo.setIsVerify(AttendanceExceptionPo.VERIFY_FALSE);

            attendanceExceptionPos.add(attendanceExceptionPo);

            AttendanceExceptionDataPo attendanceExceptionDataPo = new AttendanceExceptionDataPo();
            attendanceExceptionDataPo.setName(userDto.getName());
            attendanceExceptionDataPo.setDepartment(userDto.getOrgName());
            attendanceExceptionDataPo.setPosition(userDto.getPosition());
            attendanceExceptionDataPo.setAttendanceMonth(attendanceMonth);
            String tempExceptionId = getExceptionId(attendanceExceptionDataPo);
            if (StringUtils.isBlank(tempExceptionId)){
                attendanceExceptionDataPo.setExceptionId(exceptionId);
            }else {
                attendanceExceptionDataPo.setExceptionId(tempExceptionId);
            }
            attendanceExceptionDataPo.setAttendanceDate(beforeDayStr);
            // 标记为迟到
            attendanceExceptionDataPo.setType(2);
            attendanceExceptionDataPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ATTENDANCE_EXCEPTION_DATA_ID));
            attendanceExceptionDataPos.add(attendanceExceptionDataPo);
        }

        try {
            // 根据唯一索引，有则更新，没有则插入(前提是数据库中已经创建了唯一索引)
            if (CollectionUtils.isNotEmpty(attendanceExceptionPos)){
                attendanceExceptionMapper.upsertAttendanceExceptionBatch(attendanceExceptionPos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (AttendanceExceptionDataPo exceptionDataPo : attendanceExceptionDataPos) {
            if (Objects.nonNull(exceptionDataPo.getExceptionId()) && Objects.nonNull(exceptionDataPo.getId())){
                continue;
            }
            //这里做一个标记，到时候方便查询是什么地方出错
            String mark = exceptionDataPo.getDepartment() + exceptionDataPo.getName();
            List<UserDto> userDtoList = userMap.get(exceptionDataPo.getName());
            modifyDepartment(exceptionDataPo,userDtoList);
            String exceptionId = getExceptionId(exceptionDataPo);

            exceptionDataPo.setExceptionId(exceptionId != null ? exceptionId : mark);
            exceptionDataPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ATTENDANCE_EXCEPTION_DATA_ID));

        }

        // 批量插入考勤异常数据
        if (CollectionUtils.isNotEmpty(attendanceExceptionDataPos)){
            attendanceExceptionMapper.insertAttendanceExceptionDataBatch(attendanceExceptionDataPos);
        }
    }

    private String getExceptionId(AttendanceExceptionDataPo exceptionDataPo) {
        String name = exceptionDataPo.getName();
        String department = exceptionDataPo.getDepartment();
        String position = exceptionDataPo.getPosition();
        String attendanceMonth = exceptionDataPo.getAttendanceMonth();
        // 获取到exceptionId
        return attendanceExceptionInnerServiceSMOImpl.getExceptionIdByNameAndDepartmentAndPositionAndAttendanceMonth(name, department, position, attendanceMonth);
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
     * 上午打卡是否在正常范围内打卡
     * @param dateTimeFormatter dateTimeFormatter
     * @param dateFormatter dateFormatter
     * @param fixedTimeAm fixedTimeAm
     * @param attendanceRecord attendanceRecord
     * @param attendanceDate attendanceDate
     * @param actualStartWorkTime actualStartWorkTime
     * @return true:有外出、请假或调休,false:没有外出、请假或调休
     */
    private boolean clockAm(DateTimeFormatter dateTimeFormatter, DateTimeFormatter dateFormatter, LocalTime fixedTimeAm,
                            ApiAttendanceRecordDataVo attendanceRecord, LocalDate attendanceDate, LocalTime actualStartWorkTime) {
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

                // 下午不进行判断
                // if (actualEndWorkTime.isBefore(fixedTimePm)) {
                //     LocalDateTime actualEndWorkDateTime = LocalDateTime.of(attendanceDate, actualEndWorkTime);
                //     boolean boolPm5 = (actualEndWorkDateTime.isAfter(outGoingStartDateTime) || actualEndWorkDateTime.isEqual(outGoingStartDateTime))
                //             && actualEndWorkDateTime.isBefore(outGoingEndDateTime);
                //     if (boolPm5) {
                //         outGoingFlag = true;
                //         break;
                //     }
                // }
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
        methodParam.setFixedTimePm(fixedTimeAm);
        methodParam.setAttendanceDate(attendanceDate);
        methodParam.setActualStartWorkTime(actualStartWorkTime);
        methodParam.setActualEndWorkTime(actualStartWorkTime);
        methodParam.setUserInfoDto(userInfoDto);

        return handleVacationAndRestV2(methodParam);
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
     * 上班未打卡
     * @param dateTimeFormatter dateTimeFormatter
     * @param fixedTimeAm fixedTimeAm
     * @param attendanceRecord attendanceRecord
     * @param attendanceDate attendanceDate
     * @param actualStartWorkTime actualStartWorkTime
     * @return true:有外出、请假或调休,false:没有外出、请假或调休
     */
    private boolean clockAm(DateTimeFormatter dateTimeFormatter, LocalTime fixedTimeAm,
                            ApiAttendanceRecordDataVo attendanceRecord, LocalDate attendanceDate,
                            LocalTime actualStartWorkTime) {
        // 该时间段是否外出
        String attendanceDateStr = attendanceRecord.getAttendanceDate();
        UserInfoDto userInfoDto = getUserInfoDto(attendanceRecord, attendanceDateStr);
        List<OutGoingDto> outGoingTimes = outGoingPersonnelInnerServiceSMOImpl.listOutGoingTimesByUserInfo(userInfoDto);

        if (CollectionUtils.isNotEmpty(outGoingTimes)) {
            for (OutGoingDto outGoingTime : outGoingTimes) {
                if (commonHandleClockAmV2(dateTimeFormatter, fixedTimeAm,fixedTimeAm, attendanceDate, actualStartWorkTime, actualStartWorkTime, outGoingTime.getStartTime(), outGoingTime.getEndTime())) {
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
                if (commonHandleClockAmV2(dateTimeFormatter, fixedTimeAm, fixedTimeAm, attendanceDate, actualStartWorkTime, actualStartWorkTime, restTime.getStartTime(), restTime.getEndTime())){
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
     * 统一处理班未打卡 （这里下班暂时不进行处理）
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
    private boolean commonHandleClockAmV2(DateTimeFormatter dateTimeFormatter, LocalTime fixedTimeAm, LocalTime fixedTimePm, LocalDate attendanceDate,
                                          LocalTime actualStartWorkTime, LocalTime actualEndWorkTime, String startTime, String endTime) {
        LocalDateTime fixedStartWorkDateTime = LocalDateTime.of(attendanceDate, fixedTimeAm);
        // LocalDateTime fixedEndWorkDateTime = LocalDateTime.of(attendanceDate, fixedTimePm);

        LocalDateTime outGoingOrRestStartTime = LocalDateTime.parse(startTime, dateTimeFormatter);
        LocalDateTime outGoingOrRestEndTime = LocalDateTime.parse(endTime, dateTimeFormatter);

        if (Objects.isNull(actualStartWorkTime)) {
            // 上班没打卡。外出或调休开始时间在9:05之前或与9:05相等 且 外出或调休结束时间在9:05之后
            boolean boolAm1 = outGoingOrRestEndTime.isAfter(fixedStartWorkDateTime) && (outGoingOrRestStartTime.isBefore(fixedStartWorkDateTime)
                    || outGoingOrRestStartTime.isEqual(fixedStartWorkDateTime));
            if (boolAm1) {
                return true;
            }

            // 下班打卡，是异常打卡 下班不进行处理
            // if (actualEndWorkTime.isBefore(fixedTimePm)) {
            //     // 实际打卡时间是否在外出或调休时间段内,在外出或调休时间段外，加入异常
            //     LocalDateTime actualEndWorkDateTime = LocalDateTime.of(attendanceDate, actualEndWorkTime);
            //     return (actualEndWorkDateTime.isAfter(outGoingOrRestStartTime) || actualEndWorkDateTime.isEqual(outGoingOrRestStartTime))
            //             && actualEndWorkDateTime.isBefore(outGoingOrRestEndTime);
            // }

        } else {
            // // 下班没打卡。外出或调休开始时间在18:00之前 且 外出或调休结束时间在18:00之后或与18:00相等 下班不进行处理
            // boolean boolPm2 = outGoingOrRestStartTime.isBefore(fixedEndWorkDateTime) && (outGoingOrRestEndTime.isAfter(fixedEndWorkDateTime)
            //         || outGoingOrRestEndTime.isEqual(fixedEndWorkDateTime));
            // if (boolPm2) {
            //     return true;
            // }

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
     * 上班没有打卡
     *
     * @param am                         上午
     * @param attendanceRecord           考勤记录
     * @param attendanceDate             考勤日期类型
     */
    private boolean notClockAm(String am, ApiAttendanceRecordDataVo attendanceRecord, LocalDate attendanceDate) {
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
     * 该时间段是否申请请假或调休 (只判断上午)
     *
     * @param methodParam 参数封装
     */
    private boolean handleVacationAndRestV2(AttendanceExceptionMethodParam methodParam) {
        // 该时间段是否申请请假
        UserInfoDto userInfoDto = methodParam.getUserInfoDto();
        DateTimeFormatter dateFormatter = methodParam.getDateFormatter();
        LocalDate attendanceDate = methodParam.getAttendanceDate();

        userInfoDto.setTableName(AttendanceFilePo.TABLE_VACATION);
        List<AttendanceFileDto> vacationTimes = attendanceFileInnerServiceSMOImpl.listVacationAndRestTimesByUserInfo(userInfoDto);
        if (CollectionUtils.isNotEmpty(vacationTimes)) {
            for (AttendanceFileDto vacationTime : vacationTimes) {

                String vacationStartDateStr = vacationTime.getStartTime();
                String vacationEndDateStr = vacationTime.getEndTime();
                LocalDate vacationStartDate = LocalDate.parse(vacationStartDateStr, dateFormatter);
                LocalDate vacationEndDate = LocalDate.parse(vacationEndDateStr, dateFormatter);

                // 开始休假日期或结束休假日期和考勤异常日期相等，不进入考勤异常 ?? 为什么要相等这里难道不是在这个之间吗？
                // 开始休假日期或结束休假日期和考勤异常日期相等和考勤异常日志在开始休假日期或结束休假日期之间，不进入考勤异常
                if (attendanceDate.isEqual(vacationStartDate) || attendanceDate.isEqual(vacationEndDate)
                        || (attendanceDate.isAfter(vacationStartDate) && attendanceDate.isBefore(vacationEndDate))) {
                    return true;
                }
            }
        }

        // 该时间段是否申请调休
        DateTimeFormatter dateTimeFormatter = methodParam.getDateTimeFormatter();
        LocalTime fixedTimeAm = methodParam.getFixedTimeAm();
        // LocalTime fixedTimePm = methodParam.getFixedTimePm();
        LocalTime actualStartWorkTime = methodParam.getActualStartWorkTime();
        // LocalTime actualEndWorkTime = methodParam.getActualEndWorkTime();

        userInfoDto.setTableName(AttendanceFilePo.TABLE_REST);
        List<AttendanceFileDto> restTimes = attendanceFileInnerServiceSMOImpl.listVacationAndRestTimesByUserInfo(userInfoDto);
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

                // 下班打卡暂时不进行判断
                // if (actualEndWorkTime.isBefore(fixedTimePm)) {
                //     // 实际打卡时间是否在调休时间段内，在调休时间段外，加入异常
                //     LocalDateTime actualEndWorkDateTime = LocalDateTime.of(attendanceDate, actualEndWorkTime);
                //     boolean boolPm6 = (actualEndWorkDateTime.isAfter(restStartDateTime) || actualEndWorkDateTime.isEqual(restStartDateTime))
                //             && actualEndWorkDateTime.isBefore(restEndDateTime);
                //     if (boolPm6) {
                //         return true;
                //     }
                // }
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
        userInfoDto.setPosition(attendanceRecord.getPosition());
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
        attendanceExceptionDataPo.setPosition(attendanceRecord.getPosition());
        attendanceExceptionDataPo.setAttendanceMonth(attendanceMonth);

        attendanceExceptionDataPos.add(attendanceExceptionDataPo);
    }

}
