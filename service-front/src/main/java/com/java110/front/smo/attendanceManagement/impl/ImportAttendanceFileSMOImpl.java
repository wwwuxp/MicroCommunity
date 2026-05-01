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
import com.java110.dto.attendance.UserInfoDto;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.entity.attendance.*;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.mapper.AttendanceExceptionMapper;
import com.java110.front.mapper.AttendanceFileDataMapper;
import com.java110.front.smo.attendanceManagement.IImportAttendanceFileSMO;
import com.java110.intf.community.IAttendanceExceptionInnerServiceSMO;
import com.java110.intf.community.IAttendanceRecordInnerServiceSMO;
import com.java110.intf.pubcar.IOutGoingPersonnelInnerServiceSMO;
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
            String fileTimeStr = reqJson.getString("fileTime").trim();
            // 处理时间，防止前端传递的数据是非正常时间格式
            if (fileTimeStr.contains("-")){
                String[] fileTimeStrSplit = fileTimeStr.split("-");
                String month = fileTimeStrSplit[1];
                if (month.length() == 1){
                    month = "0" + month;
                }
                fileTimeStr = fileTimeStrSplit[0] + "-" + month + "-" + fileTimeStrSplit[2];
            }

            AttendanceFilePo attendanceFilePo = new AttendanceFilePo();
            attendanceFilePo.setFileName(reqJson.getString("fileName"));
            attendanceFilePo.setFileTime(fileTimeStr);
            attendanceFilePo.setDataType(dataType);
            attendanceFilePo.setImportTime(new Date());
            attendanceFilePo.setUserId(result.getUserId());
            attendanceFileDataMapper.saveFileInfo(attendanceFilePo);

            Integer fileId = attendanceFilePo.getId();
            // 获取文件时间在的月份中所有异常打卡数据
            // AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
            LocalDate fileTime = LocalDate.parse(fileTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
            String attendanceMonth = fileTime.getYear() + "-" + fileTime.getMonthValue() + "-01";

            // attendanceRecordDto.setLessAttendanceDate(yearMonth + "-01");
            // attendanceRecordDto.setMoreAttendanceDate(yearMonth + "-31");
            // attendanceRecordDto.setFixedStartWorkTime(ConstantUtil.FIXED_START_TIME);
            // attendanceRecordDto.setFixedEndWorkTime(ConstantUtil.FIXED_END_TIME);
            // List<ApiAttendanceRecordDataVo> attendanceRecordDatas = attendanceRecordInnerServiceSMOImpl.queryAttendanceRecords(attendanceRecordDto);

            // 获取数据库中考勤月份下所有的考勤异常数据
            List<AttendanceExceptionDataPo> attendanceExceptionInfos = attendanceExceptionInnerServiceSMOImpl.getAttendanceExceptionInfo(attendanceMonth);
            Map<String, AttendanceExceptionDataPo> dbExceptionInfoKeyAndValueMap = attendanceExceptionInfos
                    .stream()
                    .collect(Collectors.toMap(t -> t.getName() + t.getDepartment() + t.getAttendanceDate(), t -> t, (k1, k2) -> k2));

            switch (dataType){
                case AttendanceFilePo.DATATYPE_VACATION:
                    handleVacation(easyExcelInputStream, fileId, dbExceptionInfoKeyAndValueMap);
                    break;
                case AttendanceFilePo.DATATYPE_COMP_VACATION:
                    handleCompVacation(easyExcelInputStream, fileId, dbExceptionInfoKeyAndValueMap);
                    break;
                case AttendanceFilePo.DATATYPE_REST:
                    handleRest(easyExcelInputStream, fileId, dbExceptionInfoKeyAndValueMap);
                    break;
                case AttendanceFilePo.DATATYPE_OVERTIME:
                    handleOverTime(easyExcelInputStream, fileId);
                    break;
                default:
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new SMOException("导入失败，可能是格式不符合要求或某列数据为空了");
        }

        return ResultVo.success();
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
