package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.*;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCar.PubCarDto;
import com.java110.dto.pubCarApprovalProcess.PubCarApprovalProcessDto;
import com.java110.dto.pubCarApprover.PubCarApproverDto;
import com.java110.dto.pubCarTrip.PubCarTripDto;
import com.java110.dto.pubCarTrip.PubCarTripSimpleDto;
import com.java110.dto.pubCarUser.DepartmentStatisticsDto;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.po.pubCarApprovalProcess.PubCarApprovalProcessPo;
import com.java110.po.pubCarTrip.PubCarTripPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ConstantUtil;
import com.java110.vo.api.pubCar.ApiCarStatisticsDataVo;
import com.java110.vo.api.pubCarDataScreen.ApiPubCarDataScreenOverviewAndAnalysisDataVo;
import com.java110.vo.api.pubCarDataScreen.CarTripDataVo;
import com.java110.vo.api.pubCarDataScreen.DataScreenAnalysisData;
import com.java110.vo.api.pubCarTrip.*;
import com.java110.vo.api.pubCarUser.ApiDepartmentStatisticsDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 行程管理表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarTripInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarTripInnerServiceSMO {

    @Autowired
    private IPubCarTripServiceDao pubCarTripServiceDaoImpl;
    @Autowired
    private IPubCarUserServiceDao pubCarUserServiceDaoImpl;
    @Autowired
    private IPubCarDriverServiceDao pubCarDriverServiceDaoImpl;
    @Autowired
    private IPubCarServiceDao pubCarServiceDaoImpl;
    @Autowired
    private IPubCarApprovalProcessServiceDao pubCarApprovalProcessServiceDaoImpl;
    @Autowired
    private IPubCarApproverServiceDao pubCarApproverServiceDaoImpl;
    @Autowired
    private Executor asyncServiceExecutor;

    @Override
    public int savePubCarTrip(@RequestBody PubCarTripPo pubCarTripPo) {
        int saveFlag = 1;
        pubCarTripServiceDaoImpl.savePubCarTripInfo(BeanConvertUtil.beanCovertMap(pubCarTripPo));
        return saveFlag;
    }

    @Override
    public int updatePubCarTrip(@RequestBody PubCarTripPo pubCarTripPo) {
        int saveFlag = 1;
        pubCarTripServiceDaoImpl.updatePubCarTripInfo(BeanConvertUtil.beanCovertMap(pubCarTripPo));
        return saveFlag;
    }

    @Override
    public int deletePubCarTrip(@RequestBody PubCarTripPo pubCarTripPo) {
        int saveFlag = 1;
        pubCarTripPo.setStatusCd("1");
        pubCarTripServiceDaoImpl.updatePubCarTripInfo(BeanConvertUtil.beanCovertMap(pubCarTripPo));
        return saveFlag;
    }

    @Override
    public List<ApiPubCarTripDataVo> queryPubCarTrips(@RequestBody PubCarTripDto pubCarTripDto) {
        //校验是否传了 分页信息
        int page = pubCarTripDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarTripDto.setPage((page - 1) * pubCarTripDto.getRow());
        }

        List<ApiPubCarTripDataVo> pubCarTripInfo = pubCarTripServiceDaoImpl.getPubCarTripInfo(pubCarTripDto);
        // modified in 2022/04/16
        // if (Objects.isNull(pubCarTripDto.getUserId())) {
        // userId为空，查询从超级管理员或审批人来，普通用户，userId不为空
        List<String> tripIdList = pubCarTripInfo.stream().map(ApiPubCarTripDataVo::getTripId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tripIdList)){
            return Collections.emptyList();
        }
        List<PubCarApprovalProcessDto> pubCarApprovalProcessDtoList = pubCarApprovalProcessServiceDaoImpl.getTripApprovalProcess(tripIdList);
        Map<String, List<PubCarApprovalProcessDto>> tripIdAndValueMap = pubCarApprovalProcessDtoList.stream().collect(Collectors.groupingBy(PubCarApprovalProcessDto::getTripId));

        // 获取用车管理员信息
        List<PubCarApproverDto> pubCarAdministratorApproverDtoList = pubCarApproverServiceDaoImpl.getPubCarAdministrators();
        for (ApiPubCarTripDataVo apiPubCarTripDataVo : pubCarTripInfo) {
            List<ProcessDataVo> processDataVos = getProcesses(tripIdAndValueMap, pubCarAdministratorApproverDtoList, apiPubCarTripDataVo.getTripId());
            if (processDataVos == null) {
                continue;
            }

            // 获取最后一条数据
            ProcessDataVo lastProcessDataVo = processDataVos.get(processDataVos.size() - 1);
            Integer approvalStatus = lastProcessDataVo.getApprovalStatus();
            // 审核状态，3代表通过
            if (Objects.equals(approvalStatus, 3)){
                apiPubCarTripDataVo.setPass(1);
            }else {
                apiPubCarTripDataVo.setPass(0);
            }

            apiPubCarTripDataVo.setProcessDataVos(processDataVos);
        }
        // }

        // 数据库中返回的没有排好序，这里排完序返回
        if (StringUtils.isBlank(pubCarTripDto.getSort())){
            return pubCarTripInfo.stream()
                    .sorted(Comparator.comparing(ApiPubCarTripDataVo::getApplicationTime)
                            .reversed())
                    .collect(Collectors.toList());
        }

        return pubCarTripInfo.stream()
                .sorted(Comparator.comparing(ApiPubCarTripDataVo::getStartTime)
                        .reversed()
                        .thenComparing(ApiPubCarTripDataVo::getApplicationTime)
                        .reversed())
                .collect(Collectors.toList());
    }

    private List<ProcessDataVo> getProcesses(Map<String, List<PubCarApprovalProcessDto>> tripIdAndValueMap, List<PubCarApproverDto> pubCarAdministratorApproverDtoList, String tripId) {
        // 这里的数据可能会有多个同级审核人能够看到该行程的审核
        List<PubCarApprovalProcessDto> pubCarApprovalProcessDtos = tripIdAndValueMap.get(tripId);
        if (CollectionUtils.isEmpty(pubCarApprovalProcessDtos)) {
            return null;
        }
        // 对出现同级多个审批人进行合并
        Map<Integer, List<PubCarApprovalProcessDto>> sequenceAndValueMap = pubCarApprovalProcessDtos.stream().collect(Collectors.groupingBy(PubCarApprovalProcessDto::getSequence));

        // 是否包含用车管理员审批,若不，则把用车管理员加进去
        if (!sequenceAndValueMap.containsKey(2)) {
            List<PubCarApprovalProcessDto> pubCarAdministratorApprovalProcessList = new ArrayList<>();
            for (PubCarApproverDto pubCarAdministratorApproverDto : pubCarAdministratorApproverDtoList) {
                PubCarApprovalProcessDto pubCarAdministratorApprovalProcessDto = new PubCarApprovalProcessDto();
                pubCarAdministratorApprovalProcessDto.setTripId(tripId);
                pubCarAdministratorApprovalProcessDto.setName(pubCarAdministratorApproverDto.getName());
                pubCarAdministratorApprovalProcessDto.setReviewerRole(PubCarApprovalProcessPo.PUBCAR_ADMINISTRATOR);
                pubCarAdministratorApprovalProcessDto.setApprovalStatus(2);
                pubCarAdministratorApprovalProcessDto.setSequence(2);
                pubCarAdministratorApprovalProcessList.add(pubCarAdministratorApprovalProcessDto);
            }
            sequenceAndValueMap.put(PubCarApprovalProcessPo.PUBCAR_ADMINISTRATOR, pubCarAdministratorApprovalProcessList);
        }

        List<PubCarApprovalProcessDto> hasHandledPubCarApprovalProcessDtos = new ArrayList<>();
        Set<Map.Entry<Integer, List<PubCarApprovalProcessDto>>> entries = sequenceAndValueMap.entrySet();
        for (Map.Entry<Integer, List<PubCarApprovalProcessDto>> entry : entries) {

            List<PubCarApprovalProcessDto> value = entry.getValue();
            if (CollectionUtils.isNotEmpty(value)) {
                String approvalName = value.stream().map(PubCarApprovalProcessDto::getName).collect(Collectors.joining(","));
                // 取一条数据
                PubCarApprovalProcessDto oneApprovalProcess = value.get(0);
                PubCarApprovalProcessDto pubCarApprovalProcessDto = new PubCarApprovalProcessDto();
                pubCarApprovalProcessDto.setTripId(oneApprovalProcess.getTripId());
                pubCarApprovalProcessDto.setName(approvalName);
                pubCarApprovalProcessDto.setReviewerRole(oneApprovalProcess.getReviewerRole());
                pubCarApprovalProcessDto.setApprovalStatus(oneApprovalProcess.getApprovalStatus());
                pubCarApprovalProcessDto.setSequence(oneApprovalProcess.getSequence());
                // 若不是未审核，则设置审核时间，审核时间来自数据库的update_time字段,2代表未审核
                if (!Objects.equals(oneApprovalProcess.getApprovalStatus(), 2)) {
                    pubCarApprovalProcessDto.setApprovalTime(oneApprovalProcess.getApprovalTime());
                }
                hasHandledPubCarApprovalProcessDtos.add(pubCarApprovalProcessDto);
            }

        }

        List<ProcessDataVo> processDataVos = new ArrayList<>();
        for (PubCarApprovalProcessDto pubCarApprovalProcessDto : hasHandledPubCarApprovalProcessDtos) {
            ProcessDataVo processDataVo = new ProcessDataVo();
            processDataVo.setSequence(pubCarApprovalProcessDto.getSequence());
            processDataVo.setReviewerRole(pubCarApprovalProcessDto.getReviewerRole());
            processDataVo.setName(pubCarApprovalProcessDto.getName());
            processDataVo.setApprovalTime(pubCarApprovalProcessDto.getApprovalTime());
            processDataVo.setApprovalStatus(pubCarApprovalProcessDto.getApprovalStatus());
            processDataVos.add(processDataVo);
        }

        // 排序
        processDataVos.sort(Comparator.comparing(ProcessDataVo::getSequence));
        return processDataVos;
    }


    @Override
    public int queryPubCarTripsCount(@RequestBody PubCarTripDto pubCarTripDto) {
        return pubCarTripServiceDaoImpl.queryPubCarTripsCount(pubCarTripDto);
    }

    @Override
    public int queryPubCarDriverTripsCount(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto) {
        return pubCarTripServiceDaoImpl.queryPubCarDriverTripsCount(pubCarTripSimpleDto);
    }

    @Override
    public List<ApiPubCarTripDataVo> queryPubCarDriverTrips(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto) {
        int page = pubCarTripSimpleDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarTripSimpleDto.setPage((page - 1) * pubCarTripSimpleDto.getRow());
        }
        return pubCarTripServiceDaoImpl.queryPubCarDriverTrips(pubCarTripSimpleDto);
    }

    @Override
    public List<ApiSimpleTripDataVo> queryPubCarSimpleTrips(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto) {
        int page = pubCarTripSimpleDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarTripSimpleDto.setPage((page - 1) * pubCarTripSimpleDto.getRow());
        }

        return pubCarTripServiceDaoImpl.queryPubCarSimpleTrips(pubCarTripSimpleDto);
    }

    @Override
    public int queryPubCarSimpleTripsCount(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto) {
        return pubCarTripServiceDaoImpl.queryPubCarSimpleTripsCount(pubCarTripSimpleDto);
    }

    @Override
    public int queryCarTripStatisticsCount(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto) {
        return pubCarTripServiceDaoImpl.queryCarTripStatisticsCount(pubCarTripSimpleDto);
    }

    @Override
    public List<ApiCarTripStatisticsDataVo> queryCarTripStatistics(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto) {
        int page = pubCarTripSimpleDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarTripSimpleDto.setPage((page - 1) * pubCarTripSimpleDto.getRow());
        }
        return pubCarTripServiceDaoImpl.queryCarTripStatistics(pubCarTripSimpleDto);
    }

    @Override
    public ApiPubCarDataScreenOverviewAndAnalysisDataVo queryPubCarDataScreenOverviewAndAnalysisData(String startTimeStr, String endTimeStr, String storeId, String communityId) {
        Map<String, Object> map = new HashMap<>();
        map.put("communityId", communityId);
        map.put("storeId", storeId);

        Date startTime = null;
        Date endTime = null;
        try {
            if (StringUtils.isNotBlank(startTimeStr)) {
                startTime = DateUtils.parseDate(startTimeStr, "yyyy-MM-dd");
            }
            if (StringUtils.isNotBlank(endTimeStr)) {
                endTime = DateUtils.parseDate(endTimeStr, "yyyy-MM-dd");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        Date finalStartTime = startTime;
        Date finalEndTime = endTime;

        // 数据总览中行程数、行程里程、行驶时长、平均行程里程、平均行程时长数据
        ApiPubCarDataScreenOverviewAndAnalysisDataVo apiPubCarDataScreenOverviewAndAnalysisDataVo = pubCarTripServiceDaoImpl.queryPubCarDataScreenOverview(map);

        // 数据总览中 单日用车峰值、日均行程数 数据
        Map<String, Object> singleDay = pubCarTripServiceDaoImpl.queryPubCarDataScreenSingleDayOverview(map);
        apiPubCarDataScreenOverviewAndAnalysisDataVo.setPeakSingleDayCar(Integer.parseInt(singleDay.get("peakSingleDayCar").toString()));
        apiPubCarDataScreenOverviewAndAnalysisDataVo.setAverageTripsPerDay(Integer.parseInt(singleDay.get("averageTripsPerDay").toString()));

        // 当天用车数据统计
        CompletableFuture<Void> todayCarUseStatisticalDataFuture = CompletableFuture.runAsync(() ->
                setTodayCarUseStatisticalData(map,
                        apiPubCarDataScreenOverviewAndAnalysisDataVo), asyncServiceExecutor);

        // 明天用车数据统计
        CompletableFuture<Void> tomorrowCarUseStatisticalDataFuture = CompletableFuture.runAsync(() ->
                setTomorrowOrOneWeekCarUseStatisticalData(map,
                        apiPubCarDataScreenOverviewAndAnalysisDataVo,
                        ConstantUtil.TOMORROW), asyncServiceExecutor);

        // 未来7天用车数据统计
        CompletableFuture<Void> oneWeekCarUseStatisticalDataFuture = CompletableFuture.runAsync(() ->
                setTomorrowOrOneWeekCarUseStatisticalData(map,
                        apiPubCarDataScreenOverviewAndAnalysisDataVo,
                        ConstantUtil.ONEWEEK), asyncServiceExecutor);

        // 部门用车分析数据
        CompletableFuture<Void> departmentalTripAnalysisFuture = CompletableFuture.runAsync(() ->
                setDepartmentalTripAnalysis(finalStartTime,
                        finalEndTime,
                        storeId,
                        communityId,
                        apiPubCarDataScreenOverviewAndAnalysisDataVo), asyncServiceExecutor);

        // 司机驾驶分析数据
        CompletableFuture<Void> driverTripAnalysisFuture = CompletableFuture.runAsync(() ->
                setDriverTripAnalysis(map,
                        apiPubCarDataScreenOverviewAndAnalysisDataVo), asyncServiceExecutor);

        // 车辆使用分析数据
        CompletableFuture<Void> carTripAnalysisFuture = CompletableFuture.runAsync(() ->
                setCarTripAnalysis(finalStartTime,
                        finalEndTime,
                        communityId,
                        apiPubCarDataScreenOverviewAndAnalysisDataVo), asyncServiceExecutor);

        // 等待所有任务执行完成
        CompletableFuture.allOf(todayCarUseStatisticalDataFuture,
                tomorrowCarUseStatisticalDataFuture,
                oneWeekCarUseStatisticalDataFuture,
                departmentalTripAnalysisFuture,
                driverTripAnalysisFuture,
                carTripAnalysisFuture).join();

        return apiPubCarDataScreenOverviewAndAnalysisDataVo;
    }

    /**
     * 明天或未来7天内的用车数据统计部分
     *
     * @param map map
     * @param apiPubCarDataScreenOverviewAndAnalysisDataVo 数据vo
     * @param flag flag标记
     */
    private void setTomorrowOrOneWeekCarUseStatisticalData(Map<String, Object> map, ApiPubCarDataScreenOverviewAndAnalysisDataVo apiPubCarDataScreenOverviewAndAnalysisDataVo, Integer flag) {
        map.put("flag", flag);
        List<Map<String, Object>> tomorrowOrOneWeekCarUseStatisticsMaps = pubCarTripServiceDaoImpl.queryPubCarDataScreenTomorrowOrOneWeekCarUseStatistics(map);

        double totalTripMileages = 0;
        int selfDrivingOrders = 0;
        int assignOrders = 0;
        int size = 0;
        if (CollectionUtils.isNotEmpty(tomorrowOrOneWeekCarUseStatisticsMaps)) {

            for (Map<String, Object> tomorrowOrOneWeekCarUseStatisticsMap : tomorrowOrOneWeekCarUseStatisticsMaps) {
                if (Objects.isNull(tomorrowOrOneWeekCarUseStatisticsMap.get("tripId"))){
                    continue;
                }

                Integer tripType = (Integer) tomorrowOrOneWeekCarUseStatisticsMap.get("tripType");
                Double tripMileage = (Double) tomorrowOrOneWeekCarUseStatisticsMap.get("tripMileage");

                if (Objects.equals(tripType, 1)) {
                    assignOrders++;
                } else if (Objects.equals(tripType, 2)) {
                    selfDrivingOrders++;
                }
                totalTripMileages += tripMileage;
                size++;
            }

        }

        if (ConstantUtil.TOMORROW.equals(flag)) {
            apiPubCarDataScreenOverviewAndAnalysisDataVo.setTomorrowTotalOrders(size);
            apiPubCarDataScreenOverviewAndAnalysisDataVo.setTomorrowAssignOrders(assignOrders);
            apiPubCarDataScreenOverviewAndAnalysisDataVo.setTomorrowSelfDrivingOrders(selfDrivingOrders);
            apiPubCarDataScreenOverviewAndAnalysisDataVo.setTomorrowTripMileage(totalTripMileages);
        } else if (ConstantUtil.ONEWEEK.equals(flag)) {
            apiPubCarDataScreenOverviewAndAnalysisDataVo.setOneWeekTotalOrders(size);
            apiPubCarDataScreenOverviewAndAnalysisDataVo.setOneWeekTripMileage(totalTripMileages);
            apiPubCarDataScreenOverviewAndAnalysisDataVo.setOneWeekAssignOrders(assignOrders);
            apiPubCarDataScreenOverviewAndAnalysisDataVo.setOneWeekSelfDrivingOrders(selfDrivingOrders);
        }

    }

    /**
     * 当天用车数据中的统计部分
     *
     * @param map map
     * @param apiPubCarDataScreenOverviewAndAnalysisDataVo 数据vo
     */
    private void setTodayCarUseStatisticalData(Map<String, Object> map, ApiPubCarDataScreenOverviewAndAnalysisDataVo apiPubCarDataScreenOverviewAndAnalysisDataVo) {
        // 查询当天的总订单数、进行订单、完成订单、无效订单数据
        List<Map<String, Object>> todayCarUseStatisticsMaps = pubCarTripServiceDaoImpl.queryPubCarDataScreenTodayCarUseStatistics(map);

        int inProgressOrder = 0;
        int completeOrder = 0;
        int invalidOrder = 0;
        if (CollectionUtils.isNotEmpty(todayCarUseStatisticsMaps)) {
            for (Map<String, Object> todayCarUseStatisticsMap : todayCarUseStatisticsMaps) {
                Integer tripStatus = (Integer) todayCarUseStatisticsMap.get("tripStatus");
                Integer approvalStatus = (Integer) todayCarUseStatisticsMap.get("approvalStatus");
                if (Objects.equals(tripStatus, 2)) {
                    inProgressOrder++;
                } else if (Objects.equals(tripStatus, 3)) {
                    completeOrder++;
                }
                if (Objects.equals(approvalStatus, 1)) {
                    invalidOrder++;
                }
            }
        }
        apiPubCarDataScreenOverviewAndAnalysisDataVo.setTodayTotalOrders(todayCarUseStatisticsMaps.size());
        apiPubCarDataScreenOverviewAndAnalysisDataVo.setInProgressOrder(inProgressOrder);
        apiPubCarDataScreenOverviewAndAnalysisDataVo.setCompleteOrder(completeOrder);
        apiPubCarDataScreenOverviewAndAnalysisDataVo.setInvalidOrder(invalidOrder);
    }

    @Override
    public List<CarTripDataVo> queryPubCarDataScreenCarUseData(Integer page, Integer row, Integer symbol, String communityId) {
        Map<String, Object> map = new HashMap<>();
        map.put("communityId", communityId);
        map.put("symbol", symbol);

        if (page != PageDto.DEFAULT_PAGE) {
            map.put("page", (page - 1) * row);
            map.put("row", row);
        }

        Set<String> userIdSet = new HashSet<>();
        List<CarTripDataVo> carTripDataVos = pubCarTripServiceDaoImpl.queryPubCarDataScreenCarUseData(map);
        for (CarTripDataVo carTripDataVo : carTripDataVos) {
            String driver = carTripDataVo.getDriver();
            if (Objects.isNull(driver)){
                userIdSet.add(carTripDataVo.getUserId());
            }
        }
        if (CollectionUtils.isNotEmpty(userIdSet)){
            Map<String, String> idAndNameMap = new HashMap<>();
            List<Map<String, String>> userIdAndUserNameMapList = pubCarUserServiceDaoImpl.getUserNameByUserIds(userIdSet);
            for (Map<String, String> idNameMap : userIdAndUserNameMapList) {
                String userId = idNameMap.get("userId");
                String name = idNameMap.get("name");

                idAndNameMap.put(userId, name);
            }
            for (CarTripDataVo carTripDataVo : carTripDataVos) {
                String driver = carTripDataVo.getDriver();
                if (Objects.isNull(driver)){
                    String userId = carTripDataVo.getUserId();
                    carTripDataVo.setDriver(idAndNameMap.get(userId));
                }
            }
        }

        return carTripDataVos;
    }

    @Override
    public int queryPubCarDataScreenCarUseDataCount(Integer symbol, String communityId) {
        Map<String, Object> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("communityId", communityId);

        return pubCarTripServiceDaoImpl.queryPubCarDataScreenCarUseDataCount(map);
    }

    @Override
    public int listChooseTripsCount(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto) {
        return pubCarTripServiceDaoImpl.listChooseTripsCount(pubCarTripSimpleDto);
    }

    @Override
    public List<ApiChooseTripDataVo> listChooseTrips(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto) {
        int page = pubCarTripSimpleDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarTripSimpleDto.setPage((page - 1) * pubCarTripSimpleDto.getRow());
        }
        return pubCarTripServiceDaoImpl.listChooseTrips(pubCarTripSimpleDto);
    }

    @Override
    public List<String> getDriverIdByCarIdAndViolationTimeAndCommunityId(String carId, String violationTime, String communityId) {
        Map<String, Object> map = new HashMap<>();
        map.put("carId", carId);
        map.put("violationTime", violationTime);
        map.put("communityId", communityId);

        List<Map<String, String>> driverIdAndUserIdResults = pubCarTripServiceDaoImpl.getDriverIdByCarIdAndViolationTimeAndCommunityId(map);
        return driverIdAndUserIdResults.stream().map(t -> {
            String driverId = t.get("driverId");
            if ("-1".equals(driverId)) {
                return t.get("userId");
            }
            return driverId;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PubCarTripDto> queryPubCarTripsByTripIdsBatch(@RequestParam("tripIds") List<String> tripIds) {
        if (CollectionUtils.isEmpty(tripIds)){
            return Collections.emptyList();
        }
        return pubCarTripServiceDaoImpl.queryPubCarTripsByTripIdsBatch(tripIds);
    }

    @Override
    public int queryPubCarUseRecordsCount(@RequestBody PubCarTripDto pubCarTripDto) {
        return pubCarTripServiceDaoImpl.queryPubCarUseRecordsCount(pubCarTripDto);
    }

    @Override
    public List<ApiCarUseRecordDataVo> queryPubCarUseRecords(@RequestBody PubCarTripDto pubCarTripDto) {
        int page = pubCarTripDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarTripDto.setPage((page - 1) * pubCarTripDto.getRow());
        }
        // 获取当前用户的用车记录列表数据
        List<ApiCarUseRecordDataVo> apiCarUseRecordDataVos = pubCarTripServiceDaoImpl.queryPubCarUseRecords(pubCarTripDto);
        List<String> tripIdList = apiCarUseRecordDataVos.stream().map(ApiCarUseRecordDataVo::getTripId).collect(Collectors.toList());
        // 获取行程的审批流程
        if (CollectionUtils.isEmpty(tripIdList)){
            return Collections.emptyList();
        }
        List<PubCarApprovalProcessDto> pubCarApprovalProcessDtoList = pubCarApprovalProcessServiceDaoImpl.getTripApprovalProcess(tripIdList);
        Map<String, List<PubCarApprovalProcessDto>> tripIdAndValueMap = pubCarApprovalProcessDtoList.stream().collect(Collectors.groupingBy(PubCarApprovalProcessDto::getTripId));

        // 获取用车管理员信息
        List<PubCarApproverDto> pubCarAdministratorApproverDtoList = pubCarApproverServiceDaoImpl.getPubCarAdministrators();

        for (ApiCarUseRecordDataVo apiCarUseRecordDataVo : apiCarUseRecordDataVos) {
            List<ProcessDataVo> processDataVos = getProcesses(tripIdAndValueMap, pubCarAdministratorApproverDtoList, apiCarUseRecordDataVo.getTripId());
            if (processDataVos == null) {
                continue;
            }

            // 获取最后一条数据
            ProcessDataVo lastProcessDataVo = processDataVos.get(processDataVos.size() - 1);
            Integer approvalStatus = lastProcessDataVo.getApprovalStatus();
            // 审核状态，3代表通过
            if (Objects.equals(approvalStatus, 3)){
                apiCarUseRecordDataVo.setPass(1);
            }else {
                apiCarUseRecordDataVo.setPass(0);
            }

            apiCarUseRecordDataVo.setProcess(processDataVos);
        }

        return apiCarUseRecordDataVos;
    }

    /**
     * 车辆使用分析数据
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param communityId 小区id
     * @param apiPubCarDataScreenOverviewAndAnalysisDataVo 数据vo
     */
    private void setCarTripAnalysis(Date startTime, Date endTime, String communityId, ApiPubCarDataScreenOverviewAndAnalysisDataVo apiPubCarDataScreenOverviewAndAnalysisDataVo) {
        PubCarDto pubCarDto = new PubCarDto();
        pubCarDto.setCommunityId(communityId);
        pubCarDto.setStartTime(startTime);
        pubCarDto.setEndTime(endTime);
        List<ApiCarStatisticsDataVo> carStatisticsDataVos = pubCarServiceDaoImpl.queryCarStatistics(pubCarDto);

        // 拷贝数据
        List<DataScreenAnalysisData> carTripAnalysisData = new ArrayList<>();
        for (ApiCarStatisticsDataVo carStatisticsDataVo : carStatisticsDataVos) {
            DataScreenAnalysisData carAnalysisData = new DataScreenAnalysisData();
            if (StringUtils.isBlank(carStatisticsDataVo.getModel())) {
                carAnalysisData.setName(carStatisticsDataVo.getBrand());
            } else {
                carAnalysisData.setName(carStatisticsDataVo.getBrand() + "-" + carStatisticsDataVo.getModel());
            }
            carAnalysisData.setCount(carStatisticsDataVo.getTripNum());
            carAnalysisData.setMileage(carStatisticsDataVo.getOrderMileage());
            carTripAnalysisData.add(carAnalysisData);
        }

        apiPubCarDataScreenOverviewAndAnalysisDataVo.setCarTripAnalysis(carTripAnalysisData);
    }

    /**
     * 司机驾驶分析数据
     *
     * @param map map
     * @param apiPubCarDataScreenOverviewAndAnalysisDataVo 数据vo
     */
    private void setDriverTripAnalysis(Map<String, Object> map, ApiPubCarDataScreenOverviewAndAnalysisDataVo apiPubCarDataScreenOverviewAndAnalysisDataVo) {
        map.put("simpleFlag", ConstantUtil.SIMPLE_FLAG_STATICS);
        List<Map> pubCarDriverInfo = pubCarDriverServiceDaoImpl.getPubCarDriverInfo(map);

        // 拷贝数据
        List<DataScreenAnalysisData> driverTripAnalysisData = new ArrayList<>();
        for (Map mapInfo : pubCarDriverInfo) {
            DataScreenAnalysisData driverAnalysisData = new DataScreenAnalysisData();

            driverAnalysisData.setName((String) mapInfo.get("name"));
            driverAnalysisData.setCount(Integer.parseInt(mapInfo.get("tripNum").toString()));
            driverAnalysisData.setMileage(Double.parseDouble(mapInfo.get("mileage").toString()));
            driverTripAnalysisData.add(driverAnalysisData);
        }

        apiPubCarDataScreenOverviewAndAnalysisDataVo.setDriverTripAnalysis(driverTripAnalysisData);
    }

    /**
     * 部门用车分析数据
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param communityId 小区id
     * @param apiPubCarDataScreenOverviewAndAnalysisDataVo 数据vo
     */
    private void setDepartmentalTripAnalysis(Date startTime, Date endTime, String storeId, String communityId, ApiPubCarDataScreenOverviewAndAnalysisDataVo apiPubCarDataScreenOverviewAndAnalysisDataVo) {
        DepartmentStatisticsDto departmentStatisticsDto = new DepartmentStatisticsDto();
        departmentStatisticsDto.setCommunityId(communityId);
        departmentStatisticsDto.setStoreId(storeId);
        departmentStatisticsDto.setStartTime(startTime);
        departmentStatisticsDto.setEndTime(endTime);
        List<ApiDepartmentStatisticsDataVo> departmentStatisticsDatas = pubCarUserServiceDaoImpl.queryDepartmentStatistics(departmentStatisticsDto);

        // 拷贝数据
        List<DataScreenAnalysisData> departmentTripAnalysisData = new ArrayList<>();
        for (ApiDepartmentStatisticsDataVo departmentStatistics : departmentStatisticsDatas) {

            DataScreenAnalysisData departmentAnalysisData = new DataScreenAnalysisData();
            departmentAnalysisData.setName(departmentStatistics.getDepartment());
            departmentAnalysisData.setCount(departmentStatistics.getTripNum());
            departmentAnalysisData.setMileage(departmentStatistics.getMileage());
            departmentTripAnalysisData.add(departmentAnalysisData);
        }

        apiPubCarDataScreenOverviewAndAnalysisDataVo.setDepartmentalTripAnalysis(departmentTripAnalysisData);
    }

}
