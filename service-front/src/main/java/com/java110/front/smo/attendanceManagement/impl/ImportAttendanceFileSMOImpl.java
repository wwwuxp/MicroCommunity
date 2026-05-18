package com.java110.front.smo.attendanceManagement.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.attendance.AttendanceFileDto;
import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.entity.attendance.*;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.enums.AttendanceType;
import com.java110.front.mapper.AttendanceExceptionMapper;
import com.java110.front.mapper.AttendanceFileDataMapper;
import com.java110.front.mapper.AttendanceNonWorkDayMapper;
import com.java110.front.mapper.AttendanceRecordMapper;
import com.java110.front.smo.attendanceManagement.IImportAttendanceFileSMO;
import com.java110.intf.community.IAttendanceExceptionInnerServiceSMO;
import com.java110.intf.community.IAttendanceRecordInnerServiceSMO;
import com.java110.intf.pubcar.IOutGoingPersonnelInnerServiceSMO;
import com.java110.po.attendance.AttendanceRecordPo;
import com.java110.po.attendance.SysAllDatePo;
import com.java110.po.attendanceException.AttendanceExceptionDataPo;
import com.java110.po.attendanceException.AttendanceExceptionPo;
import com.java110.po.attendanceFile.AttendanceFilePo;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ConstantUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.attendanceRecord.ApiAttendanceRecordDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考勤管理文件导入
 * @author hh
 * @date 2021/9/24
 */
@Service("importAttendanceFileSMOImpl")
public class ImportAttendanceFileSMOImpl extends BaseComponentSMO implements IImportAttendanceFileSMO {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AttendanceFileDataMapper attendanceFileDataMapper;

    @Autowired
    private AttendanceExceptionMapper attendanceExceptionMapper;

    @Autowired
    private AttendanceRecordMapper attendanceRecordMapper;

    @Autowired
    private AttendanceNonWorkDayMapper attendanceNonWorkDayMapper;

    @Autowired
    private IAttendanceRecordInnerServiceSMO attendanceRecordInnerServiceSMOImpl;

    @Autowired
    private IAttendanceExceptionInnerServiceSMO attendanceExceptionInnerServiceSMOImpl;

    @Autowired
    private IOutGoingPersonnelInnerServiceSMO outGoingPersonnelInnerServiceSMOImpl;

    @Autowired
    private ImportAttendanceFileSMOImpl importAttendanceFileSMOImpl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception{
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "fileName", "文件名不能为空");
        Assert.hasKeyAndValue(reqJson, "fileTime", "文件时间不能为空");
        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

        try(InputStream poiInputStream = uploadFile.getInputStream();
            InputStream easyExcelInputStream = uploadFile.getInputStream()) {

            String fileName = uploadFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);

            Workbook workbook = null;
            if(fileType.equals("xls")){
                workbook = new HSSFWorkbook(poiInputStream);
            }else{
                workbook = new XSSFWorkbook(poiInputStream);
            }



            // 获取第一个工作簿
            Sheet sheet = workbook.getSheetAt(0);
            // 获取表头
            Row headerRow = sheet.getRow(0);
            // 获取表头一共多少列
            short columnCount = headerRow.getLastCellNum();
            int dataType;
            switch (columnCount){
                case 11:
                    // 休假统计
                    dataType = AttendanceFilePo.DATATYPE_COMP_VACATION;break;
                case 13:
                    // 休假统计 兼容个人休假最新的模板
                    dataType = AttendanceFilePo.DATATYPE_VACATION;break;
                case 7:
                    // 调休统计
                    dataType = AttendanceFilePo.DATATYPE_REST;break;
                case 8:
                    // 加班统计
                    dataType = AttendanceFilePo.DATATYPE_OVERTIME;break;
                default:
                    throw new SMOException("导入失败，数据格式有误");
            }

            // 保存文件基本信息
//            String fileTimeStr = reqJson.getString("fileTime").trim();
//            // 处理时间，防止前端传递的数据是非正常时间格式
//            if (fileTimeStr.contains("-")){
//                String[] fileTimeStrSplit = fileTimeStr.split("-");
//                String month = fileTimeStrSplit[1];
//                if (month.length() == 1){
//                    month = "0" + month;
//                }
//                fileTimeStr = fileTimeStrSplit[0] + "-" + month + "-" + fileTimeStrSplit[2];
//            }

            AttendanceFilePo attendanceFilePo = new AttendanceFilePo();
            attendanceFilePo.setFileName(reqJson.getString("fileName"));
//            attendanceFilePo.setFileTime(fileTimeStr);
            attendanceFilePo.setFileTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A)));
            attendanceFilePo.setDataType(dataType);
            attendanceFilePo.setImportTime(new Date());
            attendanceFilePo.setUserId(result.getUserId());
            attendanceFileDataMapper.saveFileInfo(attendanceFilePo);

            Integer fileId = attendanceFilePo.getId();
            // 获取文件时间在的月份中所有异常打卡数据
            // AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
//            LocalDate fileTime = LocalDate.parse(fileTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
//            String attendanceMonth = fileTime.getYear() + "-" + fileTime.getMonthValue() + "-01";

            // attendanceRecordDto.setLessAttendanceDate(yearMonth + "-01");
            // attendanceRecordDto.setMoreAttendanceDate(yearMonth + "-31");
            // attendanceRecordDto.setFixedStartWorkTime(ConstantUtil.FIXED_START_TIME);
            // attendanceRecordDto.setFixedEndWorkTime(ConstantUtil.FIXED_END_TIME);
            // List<ApiAttendanceRecordDataVo> attendanceRecordDatas = attendanceRecordInnerServiceSMOImpl.queryAttendanceRecords(attendanceRecordDto);

            // 获取数据库中考勤月份下所有的考勤异常数据
//            List<AttendanceExceptionDataPo> attendanceExceptionInfos = attendanceExceptionInnerServiceSMOImpl.getAttendanceExceptionInfo(attendanceMonth);
//            Map<String, AttendanceExceptionDataPo> dbExceptionInfoKeyAndValueMap = attendanceExceptionInfos
//                    .stream()
//                    .collect(Collectors.toMap(t -> t.getName() + t.getDepartment() + t.getAttendanceDate(), t -> t, (k1, k2) -> k2));

            switch (dataType){
//                case AttendanceFilePo.DATATYPE_VACATION:
////                    handleVacation(easyExcelInputStream, fileId, dbExceptionInfoKeyAndValueMap);
//                    handleVacation(easyExcelInputStream, fileId);
//                    break;
                case AttendanceFilePo.DATATYPE_COMP_VACATION: //综合休假
//                    handleCompVacation(easyExcelInputStream, fileId, dbExceptionInfoKeyAndValueMap);
                    handleCompVacation(easyExcelInputStream, fileId);
                    break;
                case AttendanceFilePo.DATATYPE_REST:
//                    handleRest(easyExcelInputStream, fileId, dbExceptionInfoKeyAndValueMap);
                    handleRestNew(easyExcelInputStream, fileId);
                    break;
//                case AttendanceFilePo.DATATYPE_OVERTIME:
//                    handleOverTime(easyExcelInputStream, fileId);
//                    break;
                default:
            }

        } catch (Exception e) {
            e.printStackTrace();
//            throw new SMOException("导入失败，可能是格式不符合要求或某列数据为空了");
            throw new SMOException(e.getMessage());
        }

        return ResultVo.success();
    }

    private void handleRest(InputStream inputStream, Integer fileId) {
        EasyExcel.read(inputStream, ImportRest.class, new ReadListener<ImportRest>() {

            private List<ImportRest> cachedRestDataList = new ArrayList<>(BATCH_COUNT);

            private List<String> needRemoveExceptionDataIds = new ArrayList<>();

            @Override
            public void invoke(ImportRest data, AnalysisContext analysisContext) {
                String department = data.getDepartment();
                String keyStr = "深圳市税务局_稽查局_";
                if (department.contains(keyStr)){
                    department = department.replace(keyStr, "");
                    data.setDepartment(department);
                }
                data.setFileId(fileId);
                cachedRestDataList.add(data);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);
//                if (data instanceof ImportRest){
                // 时间formatter
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_O);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M);
                // 上下班时间
                LocalTime fixedStartTime = LocalTime.parse(ConstantUtil.FIXED_START_TIME, timeFormatter);
                LocalTime fixedEndTime = LocalTime.parse(ConstantUtil.FIXED_END_TIME, timeFormatter);

                String name = data.getName();
                LocalDateTime restStartDateTime = LocalDateTime.parse(data.getStartTime(), dateTimeFormatter);
                LocalDateTime restEndDateTime = LocalDateTime.parse(data.getEndTime(), dateTimeFormatter);
                LocalDate restStartDate = restStartDateTime.toLocalDate();
                LocalDate restEndDate = restEndDateTime.toLocalDate();
                LocalTime restStartTime = restStartDateTime.toLocalTime();
                LocalTime restEndTime = restEndDateTime.toLocalTime();

                //获取到调休假的异常考勤
                List<AttendanceExceptionDataPo> exceptionDataPos = attendanceExceptionMapper.selectDataByActStartAndEndDate(name, null, restStartDate, restEndDate);
//                if (CollectionUtils.isEmpty(exceptionDataPos)){
//                    throw new BusinessException(ResponseConstant.RESULT_CODE_ERROR, data.getDepartment()+"“"+data.getName()+"”在"+data.getStartTime()+"至"+data.getEndTime()+"未找到异常考勤数据，请核对科室是否正确");
//                }
                Map<String, AttendanceExceptionDataPo> dbExceptionInfoKeyAndValueMap = exceptionDataPos
                        .stream()
                        .collect(Collectors.toMap(t -> t.getName() +/* t.getDepartment() +*/ t.getAttendanceDate(), t -> t, (k1, k2) -> k2));

                LocalDate tempStartDate = restStartDateTime.toLocalDate();
                while (tempStartDate.isBefore(restEndDate) || tempStartDate.isEqual(restEndDate)){
                    String key = name + /*department +*/ tempStartDate.format(dateFormatter);

                    if (dbExceptionInfoKeyAndValueMap.containsKey(key)){
                        AttendanceExceptionDataPo attendanceExceptionDataPo = dbExceptionInfoKeyAndValueMap.get(key);
                        String actualStartWorkTimeStr = attendanceExceptionDataPo.getActualStartWorkTime();
                        String actualEndWorkTimeStr = attendanceExceptionDataPo.getActualEndWorkTime();
                        LocalTime actualStartWorkTime = null;
                        LocalTime actualEndWorkTime = null;
                        if (StringUtils.isNotBlank(actualStartWorkTimeStr)){
                            actualStartWorkTime = LocalTime.parse(actualStartWorkTimeStr, timeFormatter);
                        }
                        if (StringUtils.isNotBlank(actualEndWorkTimeStr)){
                            actualEndWorkTime = LocalTime.parse(actualEndWorkTimeStr, timeFormatter);
                        }

                        boolean allNull = Objects.isNull(actualStartWorkTime) && Objects.isNull(actualEndWorkTime);
                        boolean allNonNull = Objects.nonNull(actualStartWorkTime) && Objects.nonNull(actualEndWorkTime);
                        // 调休起始时间在同一天
                        if (restStartDate.isEqual(restEndDate)){
                            // 2022-07-05 修改问题 调休时间如果为上午调休 上班时间修改为14:05:59
                            if(ConstantUtil.MORNING_WORKING_HOURS.equals(restStartTime)
                                    && ConstantUtil.MORNING_OFF_HOURS.equals(restEndTime)){
                                fixedStartTime = LocalTime.parse(ConstantUtil.NOON_FIXED_START_TIME, timeFormatter);
                            }else // 如果不是那么上班时间修改为调休结束始时间减5分钟
                                if(ConstantUtil.MORNING_WORKING_HOURS.equals(restStartTime)){
                                    fixedStartTime = restEndTime.minusMinutes(5);
                                }
                            // 调休时间如果为下午调休 调休整个下午下班时间设置为 11:55
                            if(ConstantUtil.WORKING_HOURS_IN_THE_AFTERNOON.equals(restStartTime)
                                    && ConstantUtil.AFTERNOON_GET_OFF_WORK_HOURS.equals(restEndTime)){
                                fixedEndTime = fixedStartTime = LocalTime.parse(ConstantUtil.MORNING_FIXED_END_TIME);
                            }else // 如果不是那么下班时间修改为调休开始时间减5分钟
                                if(ConstantUtil.AFTERNOON_GET_OFF_WORK_HOURS.equals(restEndTime)){
                                    fixedEndTime = restStartTime.minusMinutes(5);
                                }
                            // 旷工
                            if (allNull){
                                //全都没有打卡，调休时间涵盖住了上班时间则设定为正常
                                boolean s = restStartTime.isBefore(fixedStartTime) || restStartTime.equals(fixedStartTime);
                                boolean e = restEndTime.isAfter(fixedEndTime) || restEndTime.equals(fixedEndTime);
                                if (s && e){
                                    //增加需要移除的异常id和更新考勤表的修复类型
                                    needRemoveExceptionDataIds.add(attendanceExceptionDataPo.getId());
                                    updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), attendanceExceptionDataPo.getAttendanceDate());
                                }else {
                                    String remark = "全天没有打卡，有调休假（"+data.getStartTime()+"~"+data.getEndTime()+"），无法涵盖全天。";
                                    updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), attendanceExceptionDataPo.getAttendanceDate());
                                }
                            }
                            // 异常表中此人开始时间和结束时间不为空
                            else if (allNonNull) {
                                boolean a = false, b = false;
                                // 迟到
                                if (actualStartWorkTime.isAfter(fixedStartTime)) {
                                    // 判断是否在调休
                                    boolean s = (restStartTime.isBefore(fixedStartTime) || restStartTime.equals(fixedStartTime))
                                            && (restEndTime.isAfter(actualStartWorkTime) || restEndTime.equals(actualStartWorkTime));
                                    if (s) {
                                        a = true;
                                    }
                                } else {
                                    a = true;
                                }

                                // 早退
                                if (actualEndWorkTime.isBefore(fixedEndTime)) {
                                    // 是否在调休
                                    boolean e = (restStartTime.isBefore(actualEndWorkTime) || restStartTime.equals(actualEndWorkTime))
                                            && (restEndTime.isAfter(fixedEndTime) || restEndTime.equals(fixedEndTime));
                                    if (e) {
                                        b = true;
                                    }
                                } else {
                                    b = true;
                                }

                                if (a && b) {
                                    // 加入删除列表
                                    needRemoveExceptionDataIds.add(attendanceExceptionDataPo.getId());
                                    updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), attendanceExceptionDataPo.getAttendanceDate());
                                } else {
                                    String remark = "有调休假（"+data.getStartTime()+"~"+data.getEndTime()+"），无法涵盖打卡异常所有时间。";
                                    updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), attendanceExceptionDataPo.getAttendanceDate());
                                }
                            }
                            //开始时间和结束时间有其中一个为空的
                            else {
                                String remark = "开始时间或结束时间中有一个为空";
                                updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), attendanceExceptionDataPo.getAttendanceDate());
                            }
                        }else {
                            // 不在同一天
                            LocalDateTime fixedStartDateTime = LocalDateTime.of(tempStartDate, fixedStartTime);
                            LocalDateTime fixedEndDateTime = LocalDateTime.of(tempStartDate, fixedEndTime);
                            LocalDateTime actualStartWorkDateTime = null;
                            LocalDateTime actualEndWorkDateTime = null;
                            if (Objects.nonNull(actualStartWorkTime)){
                                actualStartWorkDateTime = LocalDateTime.of(tempStartDate, actualStartWorkTime);
                            }
                            if (Objects.nonNull(actualEndWorkTime)){
                                actualEndWorkDateTime = LocalDateTime.of(tempStartDate, actualEndWorkTime);
                            }
                            // 实际开始时间和实际结束时间都为空，旷工
                            if (allNull){
                                // 时间是否合法
                                boolean s = restStartDateTime.isBefore(fixedStartDateTime) || restStartDateTime.equals(fixedStartDateTime);
                                if (s){
                                    needRemoveExceptionDataIds.add(attendanceExceptionDataPo.getId());
                                    updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), attendanceExceptionDataPo.getAttendanceDate());
                                }else {
                                    String remark = "全天没有打卡，有调休假（"+data.getStartTime()+"~"+data.getEndTime()+"），无法涵盖全天。";
                                    updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), attendanceExceptionDataPo.getAttendanceDate());
                                }
                            }
                            // 实际开始时间和结束时间都不为空
                            else if (allNonNull){
                                // 时间是否合法
                                boolean a = false, b = false;
                                // 迟到
                                if (actualStartWorkTime.isAfter(fixedStartTime)){
                                    boolean s = (restStartDateTime.isBefore(fixedStartDateTime) || restStartDateTime.equals(fixedStartDateTime))
                                            && (restEndDateTime.isAfter(actualStartWorkDateTime) || restEndDateTime.equals(actualStartWorkDateTime));
                                    if (s){
                                        a = true;
                                    }
                                }else {
                                    a = true;
                                }

                                // 早退
                                if (actualEndWorkTime.isBefore(fixedEndTime)){
                                    boolean e = (restStartDateTime.isBefore(actualEndWorkDateTime) || restStartDateTime.isEqual(actualEndWorkDateTime))
                                            && (restEndDateTime.isAfter(fixedEndDateTime) || restEndDateTime.isEqual(fixedEndDateTime));
                                    if (e){
                                        b = true;
                                    }
                                }else {
                                    b = true;
                                }

                                if (a && b){
                                    // 加入删除列表
                                    needRemoveExceptionDataIds.add(attendanceExceptionDataPo.getId());
                                    updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), attendanceExceptionDataPo.getAttendanceDate());
                                }else {
                                    String remark = "有调休假（"+data.getStartTime()+"~"+data.getEndTime()+"），无法涵盖打卡异常所有时间。";
                                    updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), attendanceExceptionDataPo.getAttendanceDate());
                                }
                            }//开始时间和结束时间有其中一个为空的
                            else {
                                String remark = "开始时间或结束时间中有一个为空";
                                updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), attendanceExceptionDataPo.getAttendanceDate());
                            }
                        }
                    }
                    tempStartDate = tempStartDate.plusDays(1);
                }
//                    }else if (data instanceof ImportVacation){
//                        // 休假
//                        ImportVacation importVacation = (ImportVacation) data;
//                        String name = importVacation.getName();
//                        String department = importVacation.getDepartment();
//
//                        LocalDate vacationActStartDate = LocalDate.parse(importVacation.getActStartTime(), dateFormatter);
//                        LocalDate vacationActEndDate = LocalDate.parse(importVacation.getActEndTime(), dateFormatter);
//                        LocalDate tempActStartDate = vacationActStartDate;
//                        while (tempActStartDate.isBefore(vacationActEndDate) || tempActStartDate.isEqual(vacationActEndDate)){
//                            String key = name + department + tempActStartDate.format(dateFormatter);
//                            if (dbExceptionInfoKeyAndValueMap.containsKey(key)) {
//                                AttendanceExceptionDataPo attendanceExceptionDataPo = dbExceptionInfoKeyAndValueMap.get(key);
//                                // 1：正常，2：迟到，3：早退，4：旷工
//                                Integer type = attendanceExceptionDataPo.getType();
//                                if (!Objects.equals(type, 1)) {
//                                    needRemoveExceptionDataIds.add(attendanceExceptionDataPo.getId());
//                                }
//                            }
//
//                            tempActStartDate = tempActStartDate.plusDays(1);
//                        }
//                    }


                // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
                if (cachedRestDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // handleAttendanceExceptionData(attendanceRecordDatas, cachedRestDataList);
                    // 存储完成清理 list
                    cachedRestDataList = new ArrayList<>(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                saveData();
                deleteData();
                // handleAttendanceExceptionData(attendanceRecordDatas, cachedRestDataList);
            }

            @Override
            public void onException(Exception exception, AnalysisContext analysisContext) throws Exception {
                throw exception;
            }

            @Override
            public void invokeHead(Map map, AnalysisContext analysisContext) {}

            @Override
            public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {}

            @Override
            public boolean hasNext(AnalysisContext analysisContext) {
                return true;
            }

            private void saveData() {
                attendanceFileDataMapper.saveRestBatch(cachedRestDataList);
            }

            private void deleteData() {
                if (CollectionUtils.isNotEmpty(needRemoveExceptionDataIds)) {
                    List<AttendanceExceptionDataPo> pos = attendanceExceptionMapper.selectByIds(needRemoveExceptionDataIds);
                    attendanceExceptionMapper.batchInsert(pos);
                    attendanceExceptionMapper.deleteByIds(needRemoveExceptionDataIds);
                }
            }

        }).sheet().doRead();
    }

    private void handleRestNew(InputStream inputStream, Integer fileId) {
        EasyExcel.read(inputStream, ImportRest.class, new ReadListener<ImportRest>() {

            private List<ImportRest> cachedRestDataList = new ArrayList<>(BATCH_COUNT);

            private List<String> needRemoveExceptionDataIds = new ArrayList<>();

            @Override
            public void invoke(ImportRest data, AnalysisContext analysisContext) {
                String department = data.getDepartment();
                String keyStr = "深圳市税务局_稽查局_";
                if (department.contains(keyStr)){
                    department = department.replace(keyStr, "");
                    data.setDepartment(department);
                }
                data.setFileId(fileId);
                cachedRestDataList.add(data);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);
//                if (data instanceof ImportRest){
                // 时间formatter
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_O);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M);
                // 上下班时间
                LocalTime fixedStartTime = LocalTime.parse(ConstantUtil.FIXED_START_TIME, timeFormatter);
                LocalTime fixedEndTime = LocalTime.parse(ConstantUtil.FIXED_END_TIME, timeFormatter);

                String name = data.getName();
                LocalDateTime restStartDateTime = LocalDateTime.parse(data.getStartTime(), dateTimeFormatter);
                LocalDateTime restEndDateTime = LocalDateTime.parse(data.getEndTime(), dateTimeFormatter);
                LocalDate restStartDate = restStartDateTime.toLocalDate();
                LocalDate restEndDate = restEndDateTime.toLocalDate();
                LocalTime restStartTime = restStartDateTime.toLocalTime();
                LocalTime restEndTime = restEndDateTime.toLocalTime();

                for (int i = 0; restStartDate.isBefore(restEndDate) || restStartDate.equals(restEndDate); i++){
                    //获取考勤情况
                    List<AttendanceRecordPo> attendanceRecordPos = attendanceRecordMapper.selectByDepartmentAndNameAndDate(department, name, restStartDate);
                    if (CollectionUtils.isEmpty(attendanceRecordPos)){
                        attendanceRecordPos = attendanceRecordMapper.selectByDepartmentAndNameAndDate(null, name, restStartDate);
                    }
                    if (CollectionUtils.isNotEmpty(attendanceRecordPos) && attendanceRecordPos.size() > 1){
                        System.out.println(name + restStartDate+" 有两条打开数据，特殊情况，需要人工处理");
                        restStartDate = restStartDate.plusDays(1);
                        continue;
                    }
                    String restDateStr = restStartDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
                    //第一天要考虑是否全天调休，还要考虑是不是同一天
                    if (restStartDate.equals(restEndDate) && i==0){
                        //调休一整天
                        if (!restStartTime.isAfter(fixedStartTime) && !restEndTime.isBefore(fixedEndTime)){
                            if (CollectionUtils.isNotEmpty(attendanceRecordPos)){
                                //有打卡记录，直接设置为调休
                                updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                            }
                            //没有打卡记录插入一条
                            else {
                                genrateAndinsertAttendanceRecord(data, restDateStr, AttendanceType.TXJ.getCode());
                            }
                        }else {
                            //
                            if (CollectionUtils.isEmpty(attendanceRecordPos)){
                                //调休时间不在上班时间之后，则是上午调休
                                if (!restStartTime.isAfter(fixedStartTime)){
                                    genrateAndinsertAttendanceRecord(data, restDateStr, AttendanceType.ZT.getCode());
                                }else {
                                    genrateAndinsertAttendanceRecord(data, restDateStr, AttendanceType.CD.getCode());
                                }
                            }else {
                                if (StringUtils.isBlank(attendanceRecordPos.get(0).getActualStartWorkTime()) && attendanceRecordPos.get(0).getRemark().contains("没有打卡记录，由程序补卡")){
                                    restStartDate = restStartDate.plusDays(1);
                                    continue;
                                }
                                LocalTime actualStartWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getActualStartWorkTime(), timeFormatter);
                                LocalTime actualEndWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getActualEndWorkTime(), timeFormatter);
                                //调休时间不在上班时间之后，则是上午调休
                                if (!restStartTime.isAfter(fixedStartTime)){
                                    //实际上班打卡时间不晚于调休结束时间
                                    if (!actualStartWorkTime.isAfter(restEndTime)){
                                        //实际下班时间不早于下班打卡时间
                                        if (!actualEndWorkTime.isBefore(fixedEndTime)){
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                        }else {
                                            String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据早退。";
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.ZT.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                            updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                        }
                                    }else {
                                        //上午上班时间
                                        LocalTime amOffWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" 12:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                                        LocalTime pmWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" 14:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                                        if (!restEndTime.isBefore(amOffWorkTime) && !actualStartWorkTime.isAfter(pmWorkTime)){
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                        }else {
                                            String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据仍然迟到。";
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.CD.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                            updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                        }
                                    }
                                }
                                //下午调休
                                else {
                                    //实际上班打卡时间不晚于调休结束时间，说明是正常的考勤
                                    if (fixedStartTime.isBefore(actualStartWorkTime)){
                                        String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据迟到。";
                                        updateRepairTypeByNameAndDepartAndDate(AttendanceType.CD.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                        updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                    }else if (actualEndWorkTime.isAfter(restStartTime)){
                                        updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                    }else {
                                        //下午上班时间
                                        LocalTime pmWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" "+ConstantUtil.NOON_FIXED_START_TIME, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                                        //上午上班时间
                                        LocalTime amOffWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" "+ConstantUtil.MORNING_FIXED_END_TIME, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                                        if (restStartTime.isAfter(amOffWorkTime) && actualEndWorkTime.isBefore(pmWorkTime)){
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                        }else {
                                            String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据早退。";
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.ZT.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                            updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        if (i == 0){
                            //调休开始时间不晚于指定上班开始时间，说明第一天是全天调休
                            if (!restStartTime.isAfter(fixedStartTime)){
                                if (CollectionUtils.isNotEmpty(attendanceRecordPos)){
                                    //有打卡记录，直接设置为调休
                                    updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                }
                                //没有打卡记录插入一条
                                else {
                                    genrateAndinsertAttendanceRecord(data, restDateStr, AttendanceType.TXJ.getCode());
                                }
                            }else {
                                if (CollectionUtils.isNotEmpty(attendanceRecordPos)){
                                    if (StringUtils.isBlank(attendanceRecordPos.get(0).getActualStartWorkTime()) && attendanceRecordPos.get(0).getRemark().contains("没有打卡记录，由程序补卡")){
                                        restStartDate = restStartDate.plusDays(1);
                                        continue;
                                    }
                                    LocalTime actualStartWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getActualStartWorkTime(), timeFormatter);
                                    LocalTime actualEndWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getActualEndWorkTime(), timeFormatter);
                                    if (!actualStartWorkTime.isBefore(fixedStartTime)){
                                        String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据迟到。";
                                        updateRepairTypeByNameAndDepartAndDate(AttendanceType.CD.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                        updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                    }else if (!actualEndWorkTime.isBefore(restStartTime)){
                                        String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据早退。";
                                        updateRepairTypeByNameAndDepartAndDate(AttendanceType.ZT.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                        updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                    }else {
                                        updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                    }
                                }else {
                                    genrateAndinsertAttendanceRecord(data, restDateStr, AttendanceType.CD.getCode());
                                }

                            }
                        }
                        //最后一天
                        else if (restStartDate.equals(restEndDate)){
                            if (CollectionUtils.isNotEmpty(attendanceRecordPos)){
                                if (!restEndTime.isBefore(fixedEndTime)){
                                    updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                }else {
                                    if (StringUtils.isBlank(attendanceRecordPos.get(0).getActualStartWorkTime()) && attendanceRecordPos.get(0).getRemark().contains("没有打卡记录，由程序补卡")){
                                        restStartDate = restStartDate.plusDays(1);
                                        continue;
                                    }
                                    LocalTime actualStartWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getActualStartWorkTime(), timeFormatter);
                                    LocalTime actualEndWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getActualEndWorkTime(), timeFormatter);
                                    //下午上班时间
                                    LocalTime pmWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" "+ConstantUtil.NOON_FIXED_START_TIME, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                                    //上午上班时间
                                    LocalTime amOffWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" "+ConstantUtil.MORNING_FIXED_END_TIME, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                                    LocalTime pmOffWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" "+ConstantUtil.FIXED_END_TIME, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                                    if (!restEndTime.isBefore(pmWorkTime)){
                                        if (!actualStartWorkTime.isBefore(restEndTime)){
                                            String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据迟到。";
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.CD.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                            updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                        }else if (actualEndWorkTime.isBefore(pmOffWorkTime)){
                                            String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据早退。";
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.ZT.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                            updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                        }else {
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                        }
                                    }else if (restEndTime.isBefore(amOffWorkTime)){
                                        if (!actualStartWorkTime.isAfter(restEndTime)){
                                            if (actualEndWorkTime.isBefore(pmOffWorkTime)){
                                                String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据早退。";
                                                updateRepairTypeByNameAndDepartAndDate(AttendanceType.ZT.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                                updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                            }else {
                                                updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                            }
                                        }else {
                                            String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据迟到。";
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.CD.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                            updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                        }
                                    }else {
                                        if (!actualStartWorkTime.isAfter(pmOffWorkTime)){
                                            if (actualEndWorkTime.isBefore(pmOffWorkTime)){
                                                String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据早退。";
                                                updateRepairTypeByNameAndDepartAndDate(AttendanceType.ZT.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                                updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                            }else {
                                                updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                            }
                                        }else {
                                            String remark = "有调休（" +data.getStartTime()+"~"+data.getEndTime()+"），但考勤数据迟到。";
                                            updateRepairTypeByNameAndDepartAndDate(AttendanceType.CD.getCode(), data.getName(), data.getDepartment(), restDateStr);
                                            updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), restDateStr);
                                        }
                                    }
                                }
                            }else {
                                if (!restEndTime.isBefore(fixedEndTime)){
                                    genrateAndinsertAttendanceRecord(data, restDateStr, AttendanceType.TXJ.getCode());
                                }else {
                                    genrateAndinsertAttendanceRecord(data, restDateStr, AttendanceType.ZT.getCode());
                                }
                            }
                        }else {
                            if (CollectionUtils.isNotEmpty(attendanceRecordPos)){
                                updateRepairTypeByNameAndDepartAndDate(AttendanceType.TXJ.getCode(), data.getName(), data.getDepartment(), restDateStr);
                            }else {
                                genrateAndinsertAttendanceRecord(data, restDateStr, AttendanceType.TXJ.getCode());
                            }
                        }
                    }
                    restStartDate = restStartDate.plusDays(1);
                }
                // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
                if (cachedRestDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // handleAttendanceExceptionData(attendanceRecordDatas, cachedRestDataList);
                    // 存储完成清理 list
                    cachedRestDataList = new ArrayList<>(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                saveData();
                deleteData();
                // handleAttendanceExceptionData(attendanceRecordDatas, cachedRestDataList);
            }

            @Override
            public void onException(Exception exception, AnalysisContext analysisContext) throws Exception {
                throw exception;
            }

            @Override
            public void invokeHead(Map map, AnalysisContext analysisContext) {}

            @Override
            public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {}

            @Override
            public boolean hasNext(AnalysisContext analysisContext) {
                return true;
            }

            private void saveData() {
                attendanceFileDataMapper.saveRestBatch(cachedRestDataList);
            }

            private void deleteData() {
                if (CollectionUtils.isNotEmpty(needRemoveExceptionDataIds)) {
                    List<AttendanceExceptionDataPo> pos = attendanceExceptionMapper.selectByIds(needRemoveExceptionDataIds);
                    attendanceExceptionMapper.batchInsert(pos);
                    attendanceExceptionMapper.deleteByIds(needRemoveExceptionDataIds);
                }
            }

        }).sheet().doRead();
    }

    private void genrateAndinsertAttendanceRecord(ImportRest data, String restDateStr, Integer type) {
        AttendanceRecordPo attendanceRecord = new AttendanceRecordPo();
        // 根据部门名称和名字查询最新一条考勤记录
        AttendanceRecordPo latestByDeptAndName = attendanceRecordMapper.selectLatestByDepartmentAndNameAndDate(data.getDepartment(), data.getName(),null);
        if (latestByDeptAndName == null) {
            latestByDeptAndName = attendanceRecordMapper.selectLatestByDepartmentAndNameAndDate(null, data.getName(), null);
        }
        //插入考勤记录
        if (latestByDeptAndName != null){
            attendanceRecord.setPersonId(latestByDeptAndName.getPersonId());
            attendanceRecord.setName(data.getName());
            attendanceRecord.setDepartment(data.getDepartment());
            attendanceRecord.setAttendanceDate(restDateStr);
            attendanceRecord.setCreateTime(new Date());
            attendanceRecord.setType(type);
            attendanceRecord.setRemark("调休假，没有打卡记录，由程序补卡，调休时间为："+data.getStartTime()+"至"+data.getEndTime());
            attendanceRecordMapper.insertRecord(attendanceRecord);
        }
    }


    /**
     * 综合休假数据处理
     * @param inputStream
     * @param fileId
     */
    private void handleCompVacation(InputStream inputStream, Integer fileId) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);

        EasyExcel.read(inputStream, ImportCompVacation.class, new ReadListener<ImportCompVacation>() {

            /**
             * 缓存的数据
             */
            private List<ImportVacation> cachedVacationDataList = new ArrayList<>(BATCH_COUNT);


            private List<String> needRemoveExceptionDataIds = new ArrayList<>();

            @Override
            public void invoke(ImportCompVacation data, AnalysisContext analysisContext) {

                String department = data.getDepartment();
                String keyStr = "深圳市税务局_稽查局_";
                if (department.contains(keyStr)){
                    department = department.replace(keyStr, "");
                    data.setDepartment(department);
                }
                data.setFileId(fileId);
                ImportVacation importVacation = new ImportVacation();
                importVacation.setDepartment(data.getDepartment());
                importVacation.setFileId(fileId);;
                importVacation.setActEndTime(data.getActEndTime());
                importVacation.setActStartTime(data.getActStartTime());
                importVacation.setApprovalStatus(data.getApprovalStatus());
                importVacation.setEndTime(data.getEndTime());
                importVacation.setLeaveDay(data.getLeaveDay());
                importVacation.setName(data.getName());
                importVacation.setPosition(data.getPositionLevel());
                importVacation.setStartTime(data.getStartTime());
                importVacation.setVacationType(data.getVacationType());
                cachedVacationDataList.add(importVacation);
                String name = data.getName();

                LocalDate vacationActStartDate = LocalDate.parse(data.getActStartTime(), dateFormatter);
                LocalDate vacationActEndDate = LocalDate.parse(data.getActEndTime(), dateFormatter);
                //获取到请假的异常考勤
                List<SysAllDatePo> sysAllDatePos = attendanceRecordMapper.getDateBeetStartAndEndDate(vacationActStartDate, vacationActEndDate);
//                List<AttendanceExceptionDataPo> exceptionDataPos = attendanceExceptionMapper.selectDataByActStartAndEndDate(name, null, vacationActStartDate, vacationActEndDate);
                //最后一天休假是否没有打卡
                boolean firstDayHasCard = false;
                //实际休假时间
                BigDecimal actualLeavyDay = BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP);
                for (int i=0; i<sysAllDatePos.size(); i++){
                    SysAllDatePo datePo = sysAllDatePos.get(i);
//                    AttendanceExceptionDataPo exceptionDataPo = exceptionDataPos.get(i);

                    //判断是否节假日，如果是则无需处理
                    if (attendanceNonWorkDayMapper.selectCountByDate(LocalDate.parse(datePo.getDateStr(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))) > 0){
                        continue;
                    }
                    Integer repairType = AttendanceType.getCodeByName(data.getVacationType());

                    //按日期获取考勤
                    List<AttendanceRecordPo> attendanceRecordPos = attendanceRecordMapper.selectByDepartmentAndNameAndDate(department, name, LocalDate.parse(datePo.getDateStr(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    if (CollectionUtils.isEmpty(attendanceRecordPos)){
                        attendanceRecordPos = attendanceRecordMapper.selectByDepartmentAndNameAndDate(null, name, LocalDate.parse(datePo.getDateStr(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }

                    //没有打卡
                    if (CollectionUtils.isEmpty(attendanceRecordPos)){
                        actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
                        AttendanceRecordPo attendanceRecord = new AttendanceRecordPo();
                        // 根据部门名称和名字查询最新一条考勤记录
                        AttendanceRecordPo latestByDeptAndName = attendanceRecordMapper.selectLatestByDepartmentAndNameAndDate(data.getDepartment(), data.getName(),null);
                        if (latestByDeptAndName == null) {
                            latestByDeptAndName = attendanceRecordMapper.selectLatestByDepartmentAndNameAndDate(null, data.getName(), null);
                        }
                        //插入考勤记录
                        if (latestByDeptAndName != null){
                            attendanceRecord.setPersonId(latestByDeptAndName.getPersonId());
                            attendanceRecord.setName(data.getName());
                            attendanceRecord.setDepartment(data.getDepartment());
                            attendanceRecord.setAttendanceDate(datePo.getDateStr());
                            attendanceRecord.setCreateTime(new Date());
                            attendanceRecord.setPosition(data.getPosition());
                            if (BigDecimal.valueOf(data.getLeaveDay()).compareTo(BigDecimal.ONE) >=0){
                                attendanceRecord.setType(repairType);
                                attendanceRecord.setRemark(data.getVacationType()+"，没有打卡记录，由程序补卡，请假时间为："+vacationActStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"至"+vacationActEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//                                attendanceRecord.setActualStartWorkTime("09:00:00");
//                                attendanceRecord.setActualEndWorkTime("18:00:00");
                            }else {
                                attendanceRecord.setType(AttendanceType.KG.getCode());
                                attendanceRecord.setRemark(data.getVacationType()+"时间为"+vacationActStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"，不足一天，缺少考勤时间，视为旷工");
                            }
                            attendanceRecordMapper.insertRecord(attendanceRecord);
                        }

                        if (i == sysAllDatePos.size() -1 && firstDayHasCard){
                            //如果实际休假时长小于请假时长，则是正常休假
                            if (actualLeavyDay.compareTo(BigDecimal.valueOf(data.getLeaveDay()).setScale(1, RoundingMode.HALF_UP)) <= 0){
                                updateRepairTypeByNameAndDepartAndDate(repairType, data.getName(), data.getDepartment(), datePo.getDateStr());
                            }else {
                                //第一天异常休假
                                String remark = "有"+data.getVacationType()+"（"+data.getActStartTime()+"至"+data.getActEndTime()+"，总"+data.getLeaveDay()+"天），其中第一天实际休假时间超过0.5天。";
                                updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), sysAllDatePos.get(0).getDateStr());
                            }
                        }
                    }
                    //有打卡
                    else if (attendanceRecordPos.size() == 1){
                        if (attendanceRecordPos.get(0).getType() > 4 && StringUtils.isBlank(attendanceRecordPos.get(0).getActualStartWorkTime()) && attendanceRecordPos.get(0).getRemark().contains("没有打卡记录，由程序补卡")){
                            actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
                            continue;
                        }
                        //上午上班时间
                        LocalTime amWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" "+ConstantUtil.FIXED_START_TIME, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                        //下午上班时间
                        LocalTime pmWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" "+ConstantUtil.NOON_FIXED_START_TIME, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                        //上午上班时间
                        LocalTime amOffWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" "+ConstantUtil.MORNING_FIXED_END_TIME, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                        LocalTime pmOffWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" "+ConstantUtil.FIXED_END_TIME, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                        //如果是第一天
                        if (i == 0) {
                            firstDayHasCard = true;
                            Boolean isPmRest = false;
                            LocalTime actualStartWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate() + " " + attendanceRecordPos.get(0).getActualStartWorkTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                            LocalTime actualEndWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate() + " " + attendanceRecordPos.get(0).getActualEndWorkTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                            if (actualEndWorkTime.isAfter(pmOffWorkTime)){
                                if (actualStartWorkTime.isBefore(pmWorkTime)){
                                    actualLeavyDay = actualLeavyDay.add(new BigDecimal("0.5"));
                                }else {
                                    actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
                                }
                            }else if (actualEndWorkTime.isAfter(amOffWorkTime)){
//                                isPmRest = true;
                                if (actualStartWorkTime.isBefore(amWorkTime)){
                                    actualLeavyDay = actualLeavyDay.add(new BigDecimal("0.5"));
                                }else {
                                    actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
                                }
                            }else {
                                actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
                            }
                            //只请了一天内的假
//                            if (BigDecimal.valueOf(data.getLeaveDay()).compareTo(BigDecimal.ONE) == 0) {
                                //判断上班时间请假还是下班时间请假
//                                LocalDateTime actualStartWorkTime = LocalDateTime.parse(attendanceRecordPos.get(0).getAttendanceDate() + " " + attendanceRecordPos.get(0).getActualStartWorkTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                                //实际上班时间早于规定上班时间，则为下午请假
//                                if (actualStartWorkTime.isBefore(amWorkTime)) {
//
//                                } else if (actualStartWorkTime.isBefore(pmWorkTime)) {
//                                    //在下午上班前打卡，请假为半天
//                                    actualLeavyDay = actualLeavyDay.add(new BigDecimal("0.5"));
//                                } else {
//                                    //在下午上班后打卡，请假为一天
//                                    actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
//                                }
//                            }else {

//                            }
//                            if (isPmRest) {
//                                //下班时间是在上午下班之前，则定义为请假一天
//                                if (actualEndWorkTime.isBefore(amOffWorkTime)) {
//                                    actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
//                                } else {
//                                    actualLeavyDay = actualLeavyDay.add(new BigDecimal("0.5"));
//                                }
//                            }

                            //只请了一天内的假，且日期是同一天，也有可能是两个日期，一个日期半天
                            if (data.getLeaveDay().intValue() <= 1 && sysAllDatePos.size() == 1) {
                                //实际休假的天数<=请假时长，则是正常考勤，直接移除异常数据并置为请假类型
                                if (actualLeavyDay.compareTo(BigDecimal.valueOf(data.getLeaveDay()).setScale(1, RoundingMode.HALF_UP)) <= 0) {
                                    updateRepairTypeByNameAndDepartAndDate(repairType, attendanceRecordPos.get(0).getName(), attendanceRecordPos.get(0).getDepartment(), attendanceRecordPos.get(0).getAttendanceDate());
                                } else {
                                    String remark = "有" + data.getVacationType() + "（" + data.getLeaveDay() + "天），但考勤数据超过" + data.getLeaveDay() + "天。";
                                    updateRemarkByNameAndDepartAndDate(remark, attendanceRecordPos.get(0).getName(), attendanceRecordPos.get(0).getDepartment(), attendanceRecordPos.get(0).getAttendanceDate());
                                }
                            }else {
                                updateRepairTypeByNameAndDepartAndDate(repairType, attendanceRecordPos.get(0).getName(), attendanceRecordPos.get(0).getDepartment(), attendanceRecordPos.get(0).getAttendanceDate());
                            }
                        }
                        //如果是最后一天
                        else if (i == sysAllDatePos.size()-1){
                            //如果是程序自动生成的打卡，则不处理
                            if (attendanceRecordPos.get(0).getType() > 4 && StringUtils.isBlank(attendanceRecordPos.get(0).getActualStartWorkTime()) && attendanceRecordPos.get(0).getRemark().contains("没有打卡记录，由程序补卡")){
                                actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
                            }else {
                                //上班时间在下午上班之前,则定义为请假半天
                                LocalTime actualStartWorkTime = LocalTime.parse(attendanceRecordPos.get(0).getAttendanceDate()+" "+attendanceRecordPos.get(0).getActualStartWorkTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                                if (actualStartWorkTime.isBefore(pmWorkTime)){
                                    actualLeavyDay = actualLeavyDay.add(new BigDecimal("0.5"));
                                }else if (actualStartWorkTime.isAfter(amWorkTime)){
                                    actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
                                }
                                //如果实际休假时长小于请假时长，则是正常休假
                                if (actualLeavyDay.compareTo(BigDecimal.valueOf(data.getLeaveDay()).setScale(1, RoundingMode.HALF_UP)) <= 0){
                                    updateRepairTypeByNameAndDepartAndDate(repairType, attendanceRecordPos.get(0).getName(), attendanceRecordPos.get(0).getDepartment(), attendanceRecordPos.get(0).getAttendanceDate());
                                }else {
                                    //异常休假，则要判断是处理第一天还是最后一天
                                    if (firstDayHasCard){
                                        //如果最后一天的实际休假时长在半天内，我们处理第一天，如果最后一天休假超过半天，我们处理最后一天
                                        //下午上班后才打卡，则视为最后一天上班异常
                                        if (actualStartWorkTime.isAfter(pmWorkTime)){
                                            String remark = "有"+data.getVacationType()+"（"+data.getActStartTime()+"至"+data.getActEndTime()+"，总"+data.getLeaveDay()+"天），其中最后一天实际休假时间超过0.5天。";
                                            updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), datePo.getDateStr());
                                        }else {
                                            String remark = "有"+data.getVacationType()+"（"+data.getActStartTime()+"至"+data.getActEndTime()+"，总"+data.getLeaveDay()+"天），其中第一天实际休假时间超过0.5天。";
                                            updateRemarkByNameAndDepartAndDate(remark, attendanceRecordPos.get(0).getName(), attendanceRecordPos.get(0).getDepartment(), sysAllDatePos.get(0).getDateStr());
                                        }
                                    }
                                    //第一天如果没有打卡，则处理最后一天
                                    else {
                                        //最后一天异常休假
                                        String remark = "有"+data.getVacationType()+"（"+data.getActStartTime()+"至"+data.getActEndTime()+"，总"+data.getLeaveDay()+"天），其中最后一天实际休假时间超过0.5天。";
                                        updateRemarkByNameAndDepartAndDate(remark, attendanceRecordPos.get(0).getName(), attendanceRecordPos.get(0).getDepartment(), datePo.getDateStr());
                                    }
                                }
                            }
                        }else {
                            actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
                            updateRepairTypeByNameAndDepartAndDate(repairType, attendanceRecordPos.get(0).getName(), attendanceRecordPos.get(0).getDepartment(), attendanceRecordPos.get(0).getAttendanceDate());
                        }
                    }
                    //打卡数据超过一条
                    else {
                        System.out.println(name+datePo.getDateStr()+"打卡数据超过一调，特殊情况不处理，等人工处理");
                        //特殊情况不处理，等人工处理
                        continue;
                    }
                    //考勤数据的标准是有开始时间就有结束时间，有可能两个时间数据一样，但不可能一个存在一个不存在
                    //当开始时间为空则说明考勤记录缺卡
//                    if (StringUtils.isBlank(exceptionDataPo.getActualStartWorkTime())){
//                        actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
//                        needRemoveExceptionDataIds.add(exceptionDataPo.getId());
//                        AttendanceRecordPo attendanceRecord = new AttendanceRecordPo();
//                        // 根据部门名称和名字查询最新一条考勤记录
//                        AttendanceRecordPo latestByDeptAndName = attendanceRecordMapper.selectLatestByDepartmentAndNameAndDate(data.getDepartment(), data.getName(),null);
//                        if (latestByDeptAndName == null) {
//                            latestByDeptAndName = attendanceRecordMapper.selectLatestByDepartmentAndNameAndDate(null, data.getName(), null);
//                        }
//                        //插入考勤记录
//                        if (latestByDeptAndName != null){
//                            attendanceRecord.setPersonId(latestByDeptAndName.getPersonId());
//                            attendanceRecord.setName(data.getName());
//                            attendanceRecord.setDepartment(data.getDepartment());
//                            attendanceRecord.setAttendanceDate(LocalDate.parse(exceptionDataPo.getAttendanceDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//                            attendanceRecord.setType(repairType);
//                            attendanceRecord.setRepairType(repairType);
//                            attendanceRecord.setRemark(data.getVacationType()+"，没有打卡记录，由程序补卡");
//                            attendanceRecord.setCreateTime(new Date());
//                            attendanceRecord.setPosition(data.getPosition());
//                            attendanceRecord.setActualEndWorkTime(LocalTime.parse("09:00:00", DateTimeFormatter.ofPattern("HH:mm:ss")));
//                            attendanceRecord.setActualStartWorkTime(LocalTime.parse("18:00:00", DateTimeFormatter.ofPattern("HH:mm:ss")));
//                            attendanceRecordMapper.insertRecord(attendanceRecord);
//                        }
//                        updateRepairTypeByNameAndDepartAndDate(repairType, data.getName(), data.getDepartment(), exceptionDataPo.getAttendanceDate());
//                        //如果最后一天是完整的休假一天，则要判断第一天是否有打卡，有的话要判断是否正常休假
//                        if (i == exceptionDataPos.size() -1 && firstDayHasCard){
//                            //如果实际休假时长小于请假时长，则是正常休假
//                            if (actualLeavyDay.compareTo(BigDecimal.valueOf(data.getLeaveDay()).setScale(1, RoundingMode.HALF_UP)) <= 0){
//                                needRemoveExceptionDataIds.add(exceptionDataPo.getId());
//                                updateRepairTypeByNameAndDepartAndDate(repairType, data.getName(), data.getDepartment(), exceptionDataPo.getAttendanceDate());
//                            }else {
//                                //第一天异常休假
//                                String remark = "有"+data.getVacationType()+"（"+data.getActStartTime()+"至"+data.getActEndTime()+"，总"+data.getLeaveDay()+"天），其中第一天实际休假时间超过0.5天。";
//                                updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), exceptionDataPos.get(0).getAttendanceDate());
//                            }
//                        }
//                    }else {
//                        //上午上班时间
//                        LocalDateTime amWorkTime = LocalDateTime.parse(exceptionDataPo.getAttendanceDate()+" 09:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
//                        //下午上班时间
//                        LocalDateTime pmWorkTime = LocalDateTime.parse(exceptionDataPo.getAttendanceDate()+" 14:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
//                        //上午上班时间
//                        LocalDateTime amOffWorkTime = LocalDateTime.parse(exceptionDataPo.getAttendanceDate()+" 12:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
//                        //下午上班时间
//                        LocalDateTime pmOffWorkTime = LocalDateTime.parse(exceptionDataPo.getAttendanceDate()+" 18:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
//                        //如果是第一天
//                        if (i == 0){
//                            firstDayHasCard = true;
//                            //只请了一天内的假
//                            Boolean isPmRest = false;
//                            if (exceptionDataPos.size() == 1){
//                                //判断上班时间请假还是下班时间请假
//                                LocalDateTime actualStartWorkTime = LocalDateTime.parse(exceptionDataPo.getAttendanceDate() + " " + exceptionDataPo.getActualStartWorkTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
//                                //实际上班时间早于规定上班时间，则为下午请假
//                                if (actualStartWorkTime.isBefore(amWorkTime)){
//                                    isPmRest = true;
//                                }else if (actualStartWorkTime.isBefore(pmWorkTime)){
//                                    //在下午上班前打卡，请假为半天
//                                    actualLeavyDay = actualLeavyDay.add(new BigDecimal("0.5"));
//                                }else {
//                                    //在下午上班后打卡，请假为一天
//                                    actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
//                                }
//                            }
//                            if (isPmRest){
//                                //下班时间是在上午下班之前，则定义为请假一天
//                                LocalDateTime actualEndWorkTime = LocalDateTime.parse(exceptionDataPo.getAttendanceDate() + " " + exceptionDataPo.getActualEndWorkTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
//                                if (actualEndWorkTime.isBefore(amOffWorkTime)) {
//                                    actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
//                                } else {
//                                    actualLeavyDay = actualLeavyDay.add(new BigDecimal("0.5"));
//                                }
//                            }
//
//                            //只请了一天内的假，且日期是同一天，也有可能是两个日期，一个日期半天
//                            if (data.getLeaveDay().intValue() <= 1 && exceptionDataPos.size() == 1){
//                                //实际休假的天数<=请假时长，则是正常考勤，直接移除异常数据并置为请假类型
//                                if (actualLeavyDay.compareTo(BigDecimal.valueOf(data.getLeaveDay()).setScale(1, RoundingMode.HALF_UP)) <=0){
//                                    needRemoveExceptionDataIds.add(exceptionDataPo.getId());
//                                    updateRepairTypeByNameAndDepartAndDate(repairType, data.getName(), data.getDepartment(), exceptionDataPo.getAttendanceDate());
//                                }else {
//                                    String remark = "有"+data.getVacationType()+"（"+data.getLeaveDay()+"天），但考勤数据超过"+data.getLeaveDay()+"天。";
//                                    updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), exceptionDataPo.getAttendanceDate());
//                                }
//                            }
//                        }
//                        //如果是最后一天
//                        else if (i == exceptionDataPos.size()-1){
//                            //上班时间在下午上班之前,则定义为请假半天
//                            LocalDateTime actualStartWorkTime = LocalDateTime.parse(exceptionDataPo.getAttendanceDate()+" "+exceptionDataPo.getActualStartWorkTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
//                            if (actualStartWorkTime.isBefore(pmWorkTime)){
//                                actualLeavyDay = actualLeavyDay.add(new BigDecimal("0.5"));
//                            }else if (actualStartWorkTime.isAfter(amWorkTime)){
//                                actualLeavyDay = actualLeavyDay.add(BigDecimal.ONE);
//                            }
//                            //如果实际休假时长小于请假时长，则是正常休假
//                            if (actualLeavyDay.compareTo(BigDecimal.valueOf(data.getLeaveDay()).setScale(1, RoundingMode.HALF_UP)) <= 0){
//                                needRemoveExceptionDataIds.add(exceptionDataPo.getId());
//                                updateRepairTypeByNameAndDepartAndDate(repairType, data.getName(), data.getDepartment(), exceptionDataPo.getAttendanceDate());
//                            }else {
//                                //异常休假，则要判断是处理第一天还是最后一天
//                                if (firstDayHasCard){
//                                    //如果最后一天的实际休假时长在半天内，我们处理第一天，如果最后一天休假超过半天，我们处理最后一天
//                                    //下午上班后才打卡，则视为最后一天上班异常
//                                    if (actualStartWorkTime.isAfter(pmWorkTime)){
//                                        String remark = "有"+data.getVacationType()+"（"+data.getActStartTime()+"至"+data.getActEndTime()+"，总"+data.getLeaveDay()+"天），其中最后一天实际休假时间超过0.5天。";
//                                        updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), exceptionDataPo.getAttendanceDate());
//                                    }else {
//                                        String remark = "有"+data.getVacationType()+"（"+data.getActStartTime()+"至"+data.getActEndTime()+"，总"+data.getLeaveDay()+"天），其中第一天实际休假时间超过0.5天。";
//                                        updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), exceptionDataPos.get(0).getAttendanceDate());
//                                    }
//                                }
//                                //第一天如果没有打卡，则处理最后一天
//                                else {
//                                    //最后一天异常休假
//                                    String remark = "有"+data.getVacationType()+"（"+data.getActStartTime()+"至"+data.getActEndTime()+"，总"+data.getLeaveDay()+"天），其中最后一天实际休假时间超过0.5天。";
//                                    updateRemarkByNameAndDepartAndDate(remark, data.getName(), data.getDepartment(), exceptionDataPo.getAttendanceDate());
//                                }
//
//                            }
//                        }
//                    }
                }


                // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
                if (cachedVacationDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 当前读取到的数据结合考勤记录中数据计算出是否是考勤异常数据
                    // handleAttendanceExceptionData(attendanceRecordDatas, cachedVacationDataList);

                    // 存储完成清理 list
                    cachedVacationDataList = new ArrayList<>(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                // 这里也要保存数据，确保最后遗留的数据也存储到数据库
                saveData();
                deleteData();
                // handleAttendanceExceptionData(attendanceRecordDatas, cachedVacationDataList);
            }

            @Override
            public void onException(Exception exception, AnalysisContext analysisContext) throws Exception {
                throw exception;
            }

            @Override
            public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {}

            @Override
            public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {}

            @Override
            public boolean hasNext(AnalysisContext analysisContext) {
                return true;
            }

            private void saveData() {
                attendanceFileDataMapper.saveVacationBatch(cachedVacationDataList);
            }

            private void deleteData() {
                if (CollectionUtils.isNotEmpty(needRemoveExceptionDataIds)) {
                    List<AttendanceExceptionDataPo> pos = attendanceExceptionMapper.selectByIds(needRemoveExceptionDataIds);
                    attendanceExceptionMapper.batchInsert(pos);
                    attendanceExceptionMapper.deleteByIds(needRemoveExceptionDataIds);
                }
            }

        }).sheet().doRead();
    }

    private void updateRemarkByNameAndDepartAndDate(String remark, String name, String department, String attendanceDate) {
        List<AttendanceRecordDto> attendanceRecords = attendanceRecordMapper.getByNameAndDate(name, attendanceDate);
        if (attendanceRecords.size() > 0){
            if (attendanceRecords.size() == 1){
                attendanceRecordMapper.updateRemarkByNameAndDepartAndDate(remark, name, null, attendanceDate);
            }else {
                /*int count = */attendanceRecordMapper.updateRemarkByNameAndDepartAndDate(remark, name, department, attendanceDate);
//                if (count < 1) {
//                    throw new BusinessException(ResponseConstant.RESULT_CODE_ERROR, "更新" + department + "“" + name + "”的"+attendanceDate+"考勤数据失败，请核查是否科室发生了变更");
//                }
            }
        }
    }

    private void updateRepairTypeByNameAndDepartAndDate(Integer repairType, String name, String department, String attendanceDate) {
        List<AttendanceRecordDto> attendanceRecords = attendanceRecordMapper.getByNameAndDate(name, attendanceDate);
        if (attendanceRecords.size() > 0){
            if (attendanceRecords.size() == 1){
                attendanceRecordMapper.updateRepairTypeByNameAndDepartAndDate(repairType, name, null, attendanceDate);
            }else {
                attendanceRecordMapper.updateRepairTypeByNameAndDepartAndDate(repairType, name, department, attendanceDate);
            }

        }
    }

    /**
     *
     * @param inputStream
     * @param fileId
     */
    private void handleVacation(InputStream inputStream, Integer fileId) {

        EasyExcel.read(inputStream, ImportVacation.class, new ReadListener<ImportVacation>() {

            /**
             * 缓存的数据
             */
            private List<ImportVacation> cachedVacationDataList = new ArrayList<>(BATCH_COUNT);

            private List<String> needRemoveExceptionDataIds = new ArrayList<>();

            @Override
            public void invoke(ImportVacation data, AnalysisContext analysisContext) {
                String department = data.getDepartment();
                String keyStr = "深圳市税务局_稽查局_";
                if (department.contains(keyStr)){
                    department = department.replace(keyStr, "");
                    data.setDepartment(department);
                }
                data.setFileId(fileId);
                cachedVacationDataList.add(data);

//                handleAttendanceExceptionData2(data, dbExceptionInfoKeyAndValueMap, needRemoveExceptionDataIds);

                // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
                if (cachedVacationDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 当前读取到的数据结合考勤记录中数据计算出是否是考勤异常数据
                    // handleAttendanceExceptionData(attendanceRecordDatas, cachedVacationDataList);

                    // 存储完成清理 list
                    cachedVacationDataList = new ArrayList<>(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                // 这里也要保存数据，确保最后遗留的数据也存储到数据库
                saveData();
                deleteData();
                // handleAttendanceExceptionData(attendanceRecordDatas, cachedVacationDataList);
            }

            @Override
            public void onException(Exception exception, AnalysisContext analysisContext) throws Exception {
                throw exception;
            }

            @Override
            public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {}

            @Override
            public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {}

            @Override
            public boolean hasNext(AnalysisContext analysisContext) {
                return true;
            }

            private void saveData() {
                attendanceFileDataMapper.saveVacationBatch(cachedVacationDataList);
            }

            private void deleteData() {
                if (CollectionUtils.isNotEmpty(needRemoveExceptionDataIds)) {
                    attendanceExceptionMapper.deleteByIds(needRemoveExceptionDataIds);
                }
            }

        }).sheet().doRead();
    }

    /**
     * 个人休假统计
     * @param inputStream inputStream
     */
    private void handleVacation(InputStream inputStream, Integer fileId, Map<String, AttendanceExceptionDataPo> dbExceptionInfoKeyAndValueMap) {

        EasyExcel.read(inputStream, ImportVacation.class, new ReadListener<ImportVacation>() {

            /**
             * 缓存的数据
             */
            private List<ImportVacation> cachedVacationDataList = new ArrayList<>(BATCH_COUNT);

            private List<String> needRemoveExceptionDataIds = new ArrayList<>();

            @Override
            public void invoke(ImportVacation data, AnalysisContext analysisContext) {
                String department = data.getDepartment();
                String keyStr = "深圳市税务局_稽查局_";
                if (department.contains(keyStr)){
                    department = department.replace(keyStr, "");
                    data.setDepartment(department);
                }
                data.setFileId(fileId);
                cachedVacationDataList.add(data);

                handleAttendanceExceptionData2(data, dbExceptionInfoKeyAndValueMap, needRemoveExceptionDataIds);

                // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
                if (cachedVacationDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 当前读取到的数据结合考勤记录中数据计算出是否是考勤异常数据
                    // handleAttendanceExceptionData(attendanceRecordDatas, cachedVacationDataList);

                    // 存储完成清理 list
                    cachedVacationDataList = new ArrayList<>(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                // 这里也要保存数据，确保最后遗留的数据也存储到数据库
                saveData();
                deleteData();
                // handleAttendanceExceptionData(attendanceRecordDatas, cachedVacationDataList);
            }

            @Override
            public void onException(Exception exception, AnalysisContext analysisContext) throws Exception {
                throw exception;
            }

            @Override
            public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {}

            @Override
            public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {}

            @Override
            public boolean hasNext(AnalysisContext analysisContext) {
                return true;
            }

            private void saveData() {
                attendanceFileDataMapper.saveVacationBatch(cachedVacationDataList);
            }

            private void deleteData() {
                if (CollectionUtils.isNotEmpty(needRemoveExceptionDataIds)) {
                    attendanceExceptionMapper.deleteByIds(needRemoveExceptionDataIds);
                }
            }

        }).sheet().doRead();
    }

    /**
     * 综合休假统计
     * @param inputStream inputStream
     */
    private void handleCompVacation(InputStream inputStream, Integer fileId, Map<String, AttendanceExceptionDataPo> dbExceptionInfoKeyAndValueMap) {

        EasyExcel.read(inputStream, ImportCompVacation.class, new ReadListener<ImportCompVacation>() {

            /**
             * 缓存的数据
             */
            private List<ImportVacation> cachedVacationDataList = new ArrayList<>(BATCH_COUNT);



            private List<String> needRemoveExceptionDataIds = new ArrayList<>();

            @Override
            public void invoke(ImportCompVacation data, AnalysisContext analysisContext) {
                String department = data.getDepartment();
                String keyStr = "深圳市税务局_稽查局_";
                if (department.contains(keyStr)){
                    department = department.replace(keyStr, "");
                    data.setDepartment(department);
                }
                data.setFileId(fileId);
                ImportVacation importVacation = new ImportVacation();
                importVacation.setPosition(data.getPosition());
                importVacation.setVacationType(data.getVacationType());
                importVacation.setActEndTime(data.getActEndTime());
                importVacation.setActStartTime(data.getActStartTime());
                importVacation.setFileId(data.getFileId());
                importVacation.setDepartment(data.getDepartment());
                importVacation.setApprovalStatus(data.getApprovalStatus());
                importVacation.setEndTime(data.getEndTime());
                importVacation.setStartTime(data.getStartTime());
                importVacation.setLeaveDay(0D);
                importVacation.setName(data.getName());
                // importVacation.setReturnTime();

                cachedVacationDataList.add(importVacation);

                handleAttendanceExceptionData2(importVacation, dbExceptionInfoKeyAndValueMap, needRemoveExceptionDataIds);

                // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
                if (cachedVacationDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 当前读取到的数据结合考勤记录中数据计算出是否是考勤异常数据
                    // handleAttendanceExceptionData(attendanceRecordDatas, cachedVacationDataList);

                    // 存储完成清理 list
                    cachedVacationDataList = new ArrayList<>(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                // 这里也要保存数据，确保最后遗留的数据也存储到数据库
                saveData();
                deleteData();
                // handleAttendanceExceptionData(attendanceRecordDatas, cachedVacationDataList);
            }

            @Override
            public void onException(Exception exception, AnalysisContext analysisContext) throws Exception {
                throw exception;
            }

            @Override
            public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {}

            @Override
            public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {}

            @Override
            public boolean hasNext(AnalysisContext analysisContext) {
                return true;
            }

            private void saveData() {
                attendanceFileDataMapper.saveVacationBatch(cachedVacationDataList);
            }

            private void deleteData() {
                if (CollectionUtils.isNotEmpty(needRemoveExceptionDataIds)) {
                    attendanceExceptionMapper.deleteByIds(needRemoveExceptionDataIds);
                }
            }

        }).sheet().doRead();
    }

    /**
     * 调休统计
     * @param inputStream inputStream
     */
    private void handleRest(InputStream inputStream, Integer fileId, Map<String, AttendanceExceptionDataPo> dbExceptionInfoKeyAndValueMap) {
        EasyExcel.read(inputStream, ImportRest.class, new ReadListener<ImportRest>() {

            private List<ImportRest> cachedRestDataList = new ArrayList<>(BATCH_COUNT);

            private List<String> needRemoveExceptionDataIds = new ArrayList<>();

            @Override
            public void invoke(ImportRest data, AnalysisContext analysisContext) {
                String department = data.getDepartment();
                String keyStr = "深圳市税务局_稽查局_";
                if (department.contains(keyStr)){
                    department = department.replace(keyStr, "");
                    data.setDepartment(department);
                }
                data.setFileId(fileId);
                cachedRestDataList.add(data);

                handleAttendanceExceptionData2(data, dbExceptionInfoKeyAndValueMap, needRemoveExceptionDataIds);

                // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
                if (cachedRestDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // handleAttendanceExceptionData(attendanceRecordDatas, cachedRestDataList);
                    // 存储完成清理 list
                    cachedRestDataList = new ArrayList<>(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                saveData();
                deleteData();
                // handleAttendanceExceptionData(attendanceRecordDatas, cachedRestDataList);
            }

            @Override
            public void onException(Exception exception, AnalysisContext analysisContext) throws Exception {
                throw exception;
            }

            @Override
            public void invokeHead(Map map, AnalysisContext analysisContext) {}

            @Override
            public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {}

            @Override
            public boolean hasNext(AnalysisContext analysisContext) {
                return true;
            }

            private void saveData() {
                attendanceFileDataMapper.saveRestBatch(cachedRestDataList);
            }

            private void deleteData() {
                if (CollectionUtils.isNotEmpty(needRemoveExceptionDataIds)) {
                    attendanceExceptionMapper.deleteByIds(needRemoveExceptionDataIds);
                }
            }

        }).sheet().doRead();
    }

    /**
     * 加班统计
     * @param inputStream inputStream
     */
    private void handleOverTime(InputStream inputStream, Integer fileId) {
        EasyExcel.read(inputStream, ImportOverTime.class, new ReadListener<ImportOverTime>() {

            private List<ImportOverTime> cachedOverTimeDataList = new ArrayList<>(BATCH_COUNT);

            @Override
            public void invoke(ImportOverTime data, AnalysisContext analysisContext) {
                data.setFileId(fileId);
                cachedOverTimeDataList.add(data);
                // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
                if (cachedOverTimeDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedOverTimeDataList = new ArrayList<>(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                saveData();
            }

            @Override
            public void onException(Exception exception, AnalysisContext analysisContext) throws Exception {
                throw exception;
            }

            @Override
            public void invokeHead(Map map, AnalysisContext analysisContext) {}

            @Override
            public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {}

            @Override
            public boolean hasNext(AnalysisContext analysisContext) {
                return true;
            }

            private void saveData() {
                attendanceFileDataMapper.saveOverTimeBatch(cachedOverTimeDataList);
            }
        }).sheet().doRead();
    }

    /**
     * 分析文件，删除考勤异常数据
     * @param data data
     * @param dbExceptionInfoKeyAndValueMap attendanceExceptionInfos
     * @param needRemoveExceptionDataIds needRemoveExceptionDataIds
     */
    private void handleAttendanceExceptionData2(Object data, Map<String, AttendanceExceptionDataPo> dbExceptionInfoKeyAndValueMap, List<String> needRemoveExceptionDataIds) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);
        if (data instanceof ImportRest){
            // 时间formatter
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_O);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M);
            // 上下班时间
            LocalTime fixedStartTime = LocalTime.parse(ConstantUtil.FIXED_START_TIME, timeFormatter);
            LocalTime fixedEndTime = LocalTime.parse(ConstantUtil.FIXED_END_TIME, timeFormatter);

            ImportRest importRest = (ImportRest) data;
            String name = importRest.getName();
            String department = importRest.getDepartment();
            LocalDateTime restStartDateTime = LocalDateTime.parse(importRest.getStartTime(), dateTimeFormatter);
            LocalDateTime restEndDateTime = LocalDateTime.parse(importRest.getEndTime(), dateTimeFormatter);
            LocalDate restStartDate = restStartDateTime.toLocalDate();
            LocalDate restEndDate = restEndDateTime.toLocalDate();
            LocalTime restStartTime = restStartDateTime.toLocalTime();
            LocalTime restEndTime = restEndDateTime.toLocalTime();

            LocalDate tempStartDate = restStartDateTime.toLocalDate();
            while (tempStartDate.isBefore(restEndDate) || tempStartDate.isEqual(restEndDate)){
                String key = name + department + tempStartDate.format(dateFormatter);
                if (dbExceptionInfoKeyAndValueMap.containsKey(key)){
                    AttendanceExceptionDataPo attendanceExceptionDataPo = dbExceptionInfoKeyAndValueMap.get(key);
                    String actualStartWorkTimeStr = attendanceExceptionDataPo.getActualStartWorkTime();
                    String actualEndWorkTimeStr = attendanceExceptionDataPo.getActualEndWorkTime();
                    LocalTime actualStartWorkTime = null;
                    LocalTime actualEndWorkTime = null;
                    if (StringUtils.isNotBlank(actualStartWorkTimeStr)){
                        actualStartWorkTime = LocalTime.parse(actualStartWorkTimeStr, timeFormatter);
                    }
                    if (StringUtils.isNotBlank(actualEndWorkTimeStr)){
                        actualEndWorkTime = LocalTime.parse(actualEndWorkTimeStr, timeFormatter);
                    }

                    boolean allNull = Objects.isNull(actualStartWorkTime) && Objects.isNull(actualEndWorkTime);
                    boolean allNonNull = Objects.nonNull(actualStartWorkTime) && Objects.nonNull(actualEndWorkTime);
                    // 调休起始时间在同一天
                    if (restStartDate.isEqual(restEndDate)){
                        // 2022-07-05 修改问题 调休时间如果为上午调休 上班时间修改为14:05:59
                        if(ConstantUtil.MORNING_WORKING_HOURS.equals(restStartTime)
                                && ConstantUtil.MORNING_OFF_HOURS.equals(restEndTime)){
                            fixedStartTime = LocalTime.parse(ConstantUtil.NOON_FIXED_START_TIME, timeFormatter);
                        }else // 如果不是那么上班时间修改为调休结束始时间减5分钟
                            if(ConstantUtil.MORNING_WORKING_HOURS.equals(restStartTime)){
                            fixedStartTime = restEndTime.minusMinutes(5);
                        }
                        // 调休时间如果为下午调休 调休整个下午下班时间设置为 11:55
                        if(ConstantUtil.WORKING_HOURS_IN_THE_AFTERNOON.equals(restStartTime)
                                && ConstantUtil.AFTERNOON_GET_OFF_WORK_HOURS.equals(restEndTime)){
                            fixedEndTime = fixedStartTime = LocalTime.parse(ConstantUtil.MORNING_FIXED_END_TIME);
                        }else // 如果不是那么下班时间修改为调休开始时间减5分钟
                            if(ConstantUtil.AFTERNOON_GET_OFF_WORK_HOURS.equals(restEndTime)){
                            fixedEndTime = restStartTime.minusMinutes(5);
                        }
                        // 旷工
                        if (allNull){
                            boolean s = restStartTime.isBefore(fixedStartTime) || restStartTime.equals(fixedStartTime);
                            boolean e = restEndTime.isAfter(fixedEndTime) || restEndTime.equals(fixedEndTime);
                            if (s && e){
                                needRemoveExceptionDataIds.add(attendanceExceptionDataPo.getId());
                            }
                        }else
                        // 异常表中此人开始时间和结束时间不为空
                        if (allNonNull){
                            boolean a = false, b = false;
                            // 迟到
                            if (actualStartWorkTime.isAfter(fixedStartTime)){
                                // 判断是否在调休
                                boolean s = (restStartTime.isBefore(fixedStartTime) || restStartTime.equals(fixedStartTime))
                                        && (restEndTime.isAfter(actualStartWorkTime) || restEndTime.equals(actualStartWorkTime));
                                if (s){
                                    a = true;
                                }
                            }else {
                                a = true;
                            }

                            // 早退
                            if (actualEndWorkTime.isBefore(fixedEndTime)){
                                // 是否在调休
                                boolean e = (restStartTime.isBefore(actualEndWorkTime) || restStartTime.equals(actualEndWorkTime))
                                        && (restEndTime.isAfter(fixedEndTime) || restEndTime.equals(fixedEndTime));
                                if (e){
                                    b = true;
                                }
                            }else {
                                b = true;
                            }

                            if (a && b){
                                // 加入删除列表
                                needRemoveExceptionDataIds.add(attendanceExceptionDataPo.getId());
                            }
                        }
                        // 注意：还有开始时间和结束时间有其中一个为空的，这种正常不会出现，暂不予考虑。

                    }else {
                        // 不在同一天
                        LocalDateTime fixedStartDateTime = LocalDateTime.of(tempStartDate, fixedStartTime);
                        LocalDateTime fixedEndDateTime = LocalDateTime.of(tempStartDate, fixedEndTime);
                        LocalDateTime actualStartWorkDateTime = null;
                        LocalDateTime actualEndWorkDateTime = null;
                        if (Objects.nonNull(actualStartWorkTime)){
                            actualStartWorkDateTime = LocalDateTime.of(tempStartDate, actualStartWorkTime);
                        }
                        if (Objects.nonNull(actualEndWorkTime)){
                            actualEndWorkDateTime = LocalDateTime.of(tempStartDate, actualEndWorkTime);
                        }

                        // 实际开始时间和实际结束时间都为空，旷工
                        if (allNull){
                            // 时间是否合法
                            boolean s = restStartDateTime.isBefore(fixedStartDateTime) || restStartDateTime.equals(fixedStartDateTime);
                            if (s){
                                needRemoveExceptionDataIds.add(attendanceExceptionDataPo.getId());
                            }
                        }

                        // 实际开始时间和结束时间都不为空
                        if (allNonNull){
                            // 时间是否合法
                            boolean a = false, b = false;
                            // 迟到
                            if (actualStartWorkTime.isAfter(fixedStartTime)){
                                boolean s = (restStartDateTime.isBefore(fixedStartDateTime) || restStartDateTime.equals(fixedStartDateTime))
                                        && (restEndDateTime.isAfter(actualStartWorkDateTime) || restEndDateTime.equals(actualStartWorkDateTime));
                                if (s){
                                    a = true;
                                }
                            }else {
                                a = true;
                            }

                            // 早退
                            if (actualEndWorkTime.isBefore(fixedEndTime)){
                                boolean e = (restStartDateTime.isBefore(actualEndWorkDateTime) || restStartDateTime.isEqual(actualEndWorkDateTime))
                                        && (restEndDateTime.isAfter(fixedEndDateTime) || restEndDateTime.isEqual(fixedEndDateTime));
                                if (e){
                                    b = true;
                                }
                            }else {
                                b = true;
                            }

                            if (a && b){
                                // 加入删除列表
                                needRemoveExceptionDataIds.add(attendanceExceptionDataPo.getId());
                            }
                        }

                    }
                }
                tempStartDate = tempStartDate.plusDays(1);
            }
        }else if (data instanceof ImportVacation){
            // 休假
            ImportVacation importVacation = (ImportVacation) data;
            String name = importVacation.getName();
            String department = importVacation.getDepartment();

            LocalDate vacationActStartDate = LocalDate.parse(importVacation.getActStartTime(), dateFormatter);
            LocalDate vacationActEndDate = LocalDate.parse(importVacation.getActEndTime(), dateFormatter);
            LocalDate tempActStartDate = vacationActStartDate;
            while (tempActStartDate.isBefore(vacationActEndDate) || tempActStartDate.isEqual(vacationActEndDate)){
                String key = name + department + tempActStartDate.format(dateFormatter);
                if (dbExceptionInfoKeyAndValueMap.containsKey(key)) {
                    AttendanceExceptionDataPo attendanceExceptionDataPo = dbExceptionInfoKeyAndValueMap.get(key);
                    // 1：正常，2：迟到，3：早退，4：旷工
                    Integer type = attendanceExceptionDataPo.getType();
                    if (!Objects.equals(type, 1)) {
                        needRemoveExceptionDataIds.add(attendanceExceptionDataPo.getId());
                    }
                }

                tempActStartDate = tempActStartDate.plusDays(1);
            }
        }

    }

    /**
     * 根据导入的文件数据判断考勤记录是否是异常数据，若是，导入到数据库中
     * @param attendanceRecordDataList 异常考勤记录列表
     * @param cachedFileDataList 扫描到的文件数据列表
     */
    private void handleAttendanceExceptionData(List<ApiAttendanceRecordDataVo> attendanceRecordDataList, List cachedFileDataList){
        // 存放异常数据
        Set<AttendanceExceptionPo> attendanceExceptionPos = new HashSet<>();
        List<AttendanceExceptionDataPo> attendanceExceptionDataPos = new ArrayList<>();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M);
        LocalTime fixedTimeAm = LocalTime.parse(ConstantUtil.FIXED_START_TIME, timeFormatter);
        LocalTime fixedTimePm = LocalTime.parse(ConstantUtil.FIXED_END_TIME, timeFormatter);

        // 分析考勤记录
        AttendanceExceptionPo attendanceExceptionPo;
        for (ApiAttendanceRecordDataVo attendanceRecord : attendanceRecordDataList) {
            // 员工基本信息
            attendanceExceptionPo = new AttendanceExceptionPo();
            String exceptionId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ATTENDANCE_EXCEPTION_ID);
            attendanceExceptionPo.setId(exceptionId);
            attendanceExceptionPo.setExceptionId(exceptionId);
            attendanceExceptionPo.setDepartment(attendanceRecord.getDepartment());
            attendanceExceptionPo.setPosition(attendanceRecord.getPosition());
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
                if (notClockAmAndPm(attendanceRecord, cachedFileDataList, attendanceDate)){
                    continue;
                }

            } else if (Objects.isNull(actualStartWorkTime) || Objects.isNull(actualEndWorkTime)) {
                // 当天上班未打卡，下班打卡 或 当天上班打卡，下班未打卡
                if (clockAmOrPm(cachedFileDataList, dateTimeFormatter, fixedTimeAm, fixedTimePm, attendanceRecord, attendanceDate, actualStartWorkTime, actualEndWorkTime)){
                    continue;
                }

            } else if (actualStartWorkTime.isAfter(fixedTimeAm) || actualEndWorkTime.isBefore(fixedTimePm)) {
                // 上下班都打卡，且实际上班时间大于9:05 或 最后一个离开道闸的时间小于18点
                if (clockAmAndPm(cachedFileDataList, dateTimeFormatter, dateFormatter, fixedTimeAm, fixedTimePm, attendanceRecord, attendanceDate, actualStartWorkTime, actualEndWorkTime)){
                    continue;
                }

            }
            putExceptionDataToList(attendanceExceptionDataPos, attendanceRecord, attendanceMonth);
        }

        String tempAttendanceMonth = null;
        try {
            // 根据唯一索引，有则更新，没有则插入(前提是数据库中已经创建了唯一索引)
            if (CollectionUtils.isNotEmpty(attendanceExceptionPos)){
                AttendanceExceptionPo exceptionPo = attendanceExceptionPos.iterator().next();
                tempAttendanceMonth = exceptionPo.getAttendanceMonth();
                importAttendanceFileSMOImpl.insertAttendanceException(attendanceExceptionPos);
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
            exceptionDataPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ATTENDANCE_EXCEPTION_DATA_ID));

        }

        // 批量插入考勤异常数据
        if (CollectionUtils.isNotEmpty(attendanceExceptionDataPos)){
            attendanceExceptionMapper.insertAttendanceExceptionDataBatch(attendanceExceptionDataPos);
        }
    }

    /**
     * 该操作不在事务中运行
     * @param attendanceExceptionPos attendanceExceptionPos
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void insertAttendanceException(Set<AttendanceExceptionPo> attendanceExceptionPos) {
        attendanceExceptionMapper.upsertAttendanceExceptionBatch(attendanceExceptionPos);
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
    private boolean clockAmAndPm(List<Object> cachedFileDataList, DateTimeFormatter dateTimeFormatter, DateTimeFormatter dateFormatter, LocalTime fixedTimeAm,LocalTime fixedTimePm,
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
        methodParam.setCachedFileDataList(cachedFileDataList);

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
    private boolean clockAmOrPm(List<Object> cachedFileDataList, DateTimeFormatter dateTimeFormatter, LocalTime fixedTimeAm, LocalTime fixedTimePm,
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
        if (cachedFileDataList.get(0) instanceof ImportRest){
            List<AttendanceFileDto> restTimes = filterCachedFileData(attendanceDate, cachedFileDataList, attendanceRecord.getName(), attendanceRecord.getDepartment());

            if (CollectionUtils.isNotEmpty(restTimes)) {
                for (AttendanceFileDto restTime : restTimes) {
                    if (commonHandleClockAmOrPm(dateTimeFormatter, fixedTimeAm, fixedTimePm, attendanceDate, actualStartWorkTime, actualEndWorkTime, restTime.getStartTime(), restTime.getEndTime())){
                        return true;
                    }
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
     * @param attendanceRecord           考勤记录
     * @param attendanceDate             考勤日期类型
     */
    private boolean notClockAmAndPm(ApiAttendanceRecordDataVo attendanceRecord, List<Object> cachedFileDataList, LocalDate attendanceDate) {
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
        userInfoDto.setFixedStartWorkDateTime(attendanceDateStr + " " + ConstantUtil.FIXED_START_TIME);
        userInfoDto.setFixedEndWorkDateTime(attendanceDateStr + " " + ConstantUtil.FIXED_END_TIME);
        boolean isOutGoing = outGoingPersonnelInnerServiceSMOImpl.isOutGoing(userInfoDto);
        if (isOutGoing) {
            return true;
        }

        // 当天未打卡是否处于调休中
        if (cachedFileDataList.get(0) instanceof ImportRest){
            LocalDateTime fixedStartWorkDateTime = LocalDateTime.parse(userInfoDto.getFixedStartWorkDateTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
            LocalDateTime fixedEndWorkDateTime = LocalDateTime.parse(userInfoDto.getFixedEndWorkDateTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
            long count = cachedFileDataList.stream().filter(t -> {
                ImportRest rest = (ImportRest) t;
                LocalDateTime startTime = LocalDateTime.parse(rest.getStartTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_O));
                LocalDateTime endTime = LocalDateTime.parse(rest.getEndTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_O));

                return Objects.equals(rest.getName(), attendanceRecord.getName())
                        && Objects.equals(rest.getDepartment(), attendanceRecord.getDepartment())
                        && (startTime.isBefore(fixedStartWorkDateTime) || startTime.isEqual(fixedStartWorkDateTime))
                        && (endTime.isAfter(fixedEndWorkDateTime) || endTime.isEqual(fixedEndWorkDateTime));
            }).count();
            if (count > 0){
                return true;
            }
        }

        // 当天未打卡是否处于休假中
        if (cachedFileDataList.get(0) instanceof ImportVacation){
            long count = cachedFileDataList.stream().filter(t -> {
                ImportVacation vacation = (ImportVacation) t;
                LocalDate startTime = LocalDate.parse(vacation.getStartTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
                LocalDate endTime = LocalDate.parse(vacation.getEndTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));

                return Objects.equals(vacation.getName(), attendanceRecord.getName())
                        && Objects.equals(vacation.getDepartment(), attendanceRecord.getDepartment())
                        && (startTime.isBefore(attendanceDate) || startTime.isEqual(attendanceDate))
                        && (endTime.isAfter(attendanceDate) || endTime.isEqual(attendanceDate));
            }).count();
            return count > 0;
        }
        return false;
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
        List<Object> cachedFileDataList = methodParam.getCachedFileDataList();

        if (cachedFileDataList.get(0) instanceof ImportVacation) {
            List<AttendanceFileDto> vacationTimes = cachedFileDataList.stream().filter(t -> {
                ImportVacation vacation = (ImportVacation) t;
                LocalDate startTime = LocalDate.parse(vacation.getStartTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
                LocalDate endTime = LocalDate.parse(vacation.getEndTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
                return Objects.equals(vacation.getName(), userInfoDto.getName())
                        && Objects.equals(vacation.getDepartment(), userInfoDto.getDepartment())
                        // && Objects.equals(vacation.getPosition(), userInfoDto.getPosition())
                        && (startTime.isEqual(attendanceDate) || endTime.isEqual(attendanceDate));
            }).map(t -> {
                ImportVacation vacation = (ImportVacation) t;
                AttendanceFileDto attendanceFileDto = new AttendanceFileDto();
                attendanceFileDto.setStartTime(vacation.getStartTime());
                attendanceFileDto.setEndTime(vacation.getEndTime());
                return attendanceFileDto;
            }).collect(Collectors.toList());

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
        }

        // 该时间段是否申请调休
        if (cachedFileDataList.get(0) instanceof ImportRest) {
            LocalTime fixedTimeAm = methodParam.getFixedTimeAm();
            LocalTime fixedTimePm = methodParam.getFixedTimePm();
            LocalTime actualStartWorkTime = methodParam.getActualStartWorkTime();
            LocalTime actualEndWorkTime = methodParam.getActualEndWorkTime();

            List<AttendanceFileDto> restTimes = filterCachedFileData(attendanceDate, cachedFileDataList, userInfoDto.getName(), userInfoDto.getDepartment());

            if (CollectionUtils.isNotEmpty(restTimes)) {
                for (AttendanceFileDto restTime : restTimes) {

                    String restStartDateTimeStr = restTime.getStartTime();
                    String restEndDateTimeStr = restTime.getEndTime();
                    LocalDateTime restStartDateTime = LocalDateTime.parse(restStartDateTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_O));
                    LocalDateTime restEndDateTime = LocalDateTime.parse(restEndDateTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_O));

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
        }
        return false;
    }

    /**
     * 数据过滤
     * @param attendanceDate attendanceDate
     * @param cachedFileDataList cachedFileDataList
     * @param name 名称
     * @param department 部门
     * @return list
     */
    private List<AttendanceFileDto> filterCachedFileData(LocalDate attendanceDate, List<Object> cachedFileDataList, String name, String department) {
        return cachedFileDataList.stream().filter(t -> {
            ImportRest rest = (ImportRest) t;
            LocalDate startTime = LocalDate.parse(rest.getStartTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_O));
            LocalDate endTime = LocalDate.parse(rest.getEndTime(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_O));
            return Objects.equals(rest.getName(), name)
                    && Objects.equals(rest.getDepartment(), department)
                    && (startTime.isEqual(attendanceDate) || endTime.isEqual(attendanceDate));
        }).map(t -> {
            ImportRest rest = (ImportRest) t;
            AttendanceFileDto attendanceFileDto = new AttendanceFileDto();
            attendanceFileDto.setStartTime(rest.getStartTime());
            attendanceFileDto.setEndTime(rest.getEndTime());
            return attendanceFileDto;
        }).collect(Collectors.toList());
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
