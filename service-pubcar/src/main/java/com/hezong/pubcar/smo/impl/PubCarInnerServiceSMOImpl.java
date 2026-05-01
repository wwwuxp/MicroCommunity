package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IKeyCabinetLogServiceDao;
import com.hezong.pubcar.dao.IPubCarServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.keyCabinetLog.KeyCabinetLogDto;
import com.java110.dto.pubCar.ExistPubCarDto;
import com.java110.dto.pubCar.PubCarDto;
import com.java110.intf.pubcar.IPubCarInnerServiceSMO;
import com.java110.po.pubCar.PubCarPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCar.ApiCarStatisticsDataVo;
import com.java110.vo.api.pubCar.ApiSimplePubCarDataVo;
import com.java110.vo.api.pubCarArchives.ApiPubCarArchivesDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 车辆管理表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarInnerServiceSMO {

    @Autowired
    private IPubCarServiceDao pubCarServiceDaoImpl;

    @Autowired
    private IKeyCabinetLogServiceDao keyCabinetLogServiceDaoImpl;

    @Override
    public int savePubCar(@RequestBody PubCarPo pubCarPo) {
        int saveFlag = 1;
        pubCarServiceDaoImpl.savePubCarInfo(BeanConvertUtil.beanCovertMap(pubCarPo));
        return saveFlag;
    }

    @Override
    public int updatePubCar(@RequestBody  PubCarPo pubCarPo) {
        int saveFlag = 1;
         pubCarServiceDaoImpl.updatePubCarInfo(BeanConvertUtil.beanCovertMap(pubCarPo));
        return saveFlag;
    }

    @Override
    public int deletePubCar(@RequestBody  PubCarPo pubCarPo) {
        int saveFlag = 1;
        pubCarPo.setStatusCd("1");
        pubCarServiceDaoImpl.updatePubCarInfo(BeanConvertUtil.beanCovertMap(pubCarPo));
        return saveFlag;
    }

    @Override
    public List<PubCarDto> queryPubCars(@RequestBody  PubCarDto pubCarDto) {
        //校验是否传了 分页信息
        int page = pubCarDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarDto.setPage((page - 1) * pubCarDto.getRow());
        }
        return BeanConvertUtil.covertBeanList(pubCarServiceDaoImpl.getPubCarInfo(BeanConvertUtil.beanCovertMap(pubCarDto)), PubCarDto.class);
    }

    @Override
    public int queryPubCarsCount(@RequestBody PubCarDto pubCarDto) {
        return pubCarServiceDaoImpl.queryPubCarsCount(BeanConvertUtil.beanCovertMap(pubCarDto));
    }

    @Override
    public List<Map<String, Object>> queryInfoByCarId(@RequestParam("carId") String carId) {
        return pubCarServiceDaoImpl.queryInfoByCarId(carId);
    }

    @Override
    public List<ApiSimplePubCarDataVo> querySimplePubCars(String communityId, String startTime, String endTime, Integer carType) {
        Map<String, Object> map = new HashMap<>();
        map.put("communityId", communityId);

        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startApplyTime = LocalDateTime.parse(startTime, dateTimeFormatter);

            map.put("startTime", startTime);
            map.put("endTime", endTime);
            map.put("carType", carType);
            // 获取时间段内在忙的车集合
            List<PubCarDto> notEmptyCars  = pubCarServiceDaoImpl.listNotEmptyCars(map);
            Set<String> notEmptyCarIds = notEmptyCars.stream().map(PubCarDto::getCarId).collect(Collectors.toSet());
            for (PubCarDto notEmptyCar : notEmptyCars) {
                Date startDateTime = notEmptyCar.getStartTime();
                Date endDateTime = notEmptyCar.getEndTime();
                if (startDateTime == null || endDateTime == null) {
                    continue;
                }

                // 转换为LocalDateTime
                LocalDateTime startLocalDateTime = startDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime endLocalDateTime = endDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                if (startLocalDateTime.isBefore(startApplyTime) && endLocalDateTime.isAfter(startApplyTime)){
                    Integer keyStatus = notEmptyCar.getKeyStatus();
                    if (Objects.equals(keyStatus, 0) || Objects.equals(keyStatus, 1)){
                        // 钥匙状态为0:可领取，可能是上一个人还没有领取，也可能是已经用完车归还了，继续往下判断
                        KeyCabinetLogDto keyCabinetLogDto = new KeyCabinetLogDto();
                        keyCabinetLogDto.setKeyNo(notEmptyCar.getKeyCabinet());
                        keyCabinetLogDto.setUserId(notEmptyCar.getApplicantId());
                        KeyCabinetLogDto oneKeyCabinetLogDto = keyCabinetLogServiceDaoImpl.getKeyCabinetLogByApplicantIdAndKeyCabinet(keyCabinetLogDto);
                        if (Objects.nonNull(oneKeyCabinetLogDto) && Objects.equals(oneKeyCabinetLogDto.getType(), 2)){
                            // 日志显示已经还车了，但这个还车可能是同一个人上上一次申请也是批准的这辆车，上上一次已经还过了，但上一次还没有去领车，这里不处理那么准确了，但这个地方存在那么个问题
                            notEmptyCarIds.remove(notEmptyCar.getCarId());
                        }
                    }
                }
            }

            // 获取空车id
            Map<String, Object> param = new HashMap<>();
            param.put("notEmptyCarIds", notEmptyCarIds);
            param.put("carType", carType);
            List<ApiSimplePubCarDataVo> emptyCarInfos = pubCarServiceDaoImpl.listEmptyCarInfos(param);
            if (CollectionUtils.isNotEmpty(emptyCarInfos)) {
                List<String> emptyCarIds = emptyCarInfos.stream().map(ApiSimplePubCarDataVo::getCarId).collect(Collectors.toList());
                List<ApiSimplePubCarDataVo> carReturnAndReserveInfos = pubCarServiceDaoImpl.getCarReturnAndReserveInfo(emptyCarIds);
                Map<String, ApiSimplePubCarDataVo> carIdAndValueMap = carReturnAndReserveInfos.stream().collect(Collectors.toMap(ApiSimplePubCarDataVo::getCarId, t -> t, (k1, k2) -> k1));

                for (ApiSimplePubCarDataVo emptyCar : emptyCarInfos) {
                    emptyCar.setIsBusy(0);
                    // 处理归还
                    handleReturn(dateTimeFormatter, emptyCar, carIdAndValueMap);
                    // 处理预约
                    handleReserve(dateTimeFormatter, emptyCar, carIdAndValueMap);
                }
            }

            map.put("carIds", notEmptyCarIds);
            if (CollectionUtils.isNotEmpty(notEmptyCarIds)){
                List<ApiSimplePubCarDataVo> busyCars = pubCarServiceDaoImpl.querySimplePubCars(map);
                for (ApiSimplePubCarDataVo busyCar : busyCars) {
                    busyCar.setIsBusy(1);
                }
                emptyCarInfos.addAll(busyCars);
            }

            // 排序
            emptyCarInfos.sort(Comparator.comparing(ApiSimplePubCarDataVo::getKeyCabinet));
            return emptyCarInfos;
        }
        return pubCarServiceDaoImpl.querySimplePubCars(map);
    }

    @Override
    public int queryCarStatisticsCount(@RequestBody PubCarDto pubCarDto) {
        return pubCarServiceDaoImpl.queryCarStatisticsCount(pubCarDto);
    }

    @Override
    public List<ApiCarStatisticsDataVo> queryCarStatistics(@RequestBody PubCarDto pubCarDto) {
        int page = pubCarDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarDto.setPage((page - 1) * pubCarDto.getRow());
        }
        return pubCarServiceDaoImpl.queryCarStatistics(pubCarDto);
    }

    @Override
    public int queryPubCarArchivesCount(@RequestBody PubCarDto pubCarDto) {
        return pubCarServiceDaoImpl.queryPubCarArchivesCount(pubCarDto);
    }

    @Override
    public List<ApiPubCarArchivesDataVo> queryPubCarArchives(@RequestBody PubCarDto pubCarDto) {
        int page = pubCarDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarDto.setPage((page - 1) * pubCarDto.getRow());
        }
        return pubCarServiceDaoImpl.queryPubCarArchives(pubCarDto);
    }

    @Override
    public ExistPubCarDto existPubCar(@RequestBody ExistPubCarDto existPubCarDto) {
        return pubCarServiceDaoImpl.existPubCar(existPubCarDto);
    }

    @Override
    public int countEmptyPubCar(String startTime, String endTime, Integer carType, String communityId) {
        // 该时间段内空闲车的数量
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("communityId", communityId);
        // 获取时间段内在忙的车id集合
        List<String> carIds  = pubCarServiceDaoImpl.listNotEmptyCars(map).stream().map(PubCarDto::getCarId).collect(Collectors.toList());

        Map<String, Object> param = new HashMap<>();
        param.put("notEmptyCarIds", carIds);
        param.put("carType", carType);
        // 查询空车数量
        return pubCarServiceDaoImpl.listEmptyCarInfos(param).size();
    }

    /**
     * 处理归还
     * @param dateTimeFormatter 日期格式化
     * @param emptyCar 空车数据
     * @param carIdAndValueMap 车辆id和值map
     */
    private void handleReturn(DateTimeFormatter dateTimeFormatter, ApiSimplePubCarDataVo emptyCar, Map<String, ApiSimplePubCarDataVo> carIdAndValueMap) {
        String carId = emptyCar.getCarId();
        String returnTimeStr = carIdAndValueMap.get(carId).getReturnTime();
        if (StringUtils.isBlank(returnTimeStr)){
            emptyCar.setReturnTime("");
            return;
        }
        LocalDateTime returnTime = LocalDateTime.parse(returnTimeStr, dateTimeFormatter);
        LocalDateTime now = LocalDateTime.now();

        long minute = ChronoUnit.MINUTES.between(returnTime, now);
        if (minute < 60){
            emptyCar.setReturnTime(minute + "分钟前被使用");
        }else if (minute < 1440){
            int hour = (int) (minute / 60);
            emptyCar.setReturnTime(hour + "小时前被使用");
        }else if (minute < 43200){
            int day = (int) (minute / 1440);
            emptyCar.setReturnTime(day + "天前被使用");
        }else if (minute < 518400){
            int month = (int) (minute / 43200);
            emptyCar.setReturnTime(month + "月前被使用");
        }else {
            int year = (int) (minute / 518400);
            emptyCar.setReturnTime(year + "年前被使用");
        }
    }

    /**
     * 处理预约
     * @param dateTimeFormatter 日期格式化
     * @param emptyCar 空车数据
     * @param carIdAndValueMap 车辆id和值map
     */
    private void handleReserve(DateTimeFormatter dateTimeFormatter, ApiSimplePubCarDataVo emptyCar, Map<String, ApiSimplePubCarDataVo> carIdAndValueMap) {
        String carId = emptyCar.getCarId();
        String reserveTimeStr = carIdAndValueMap.get(carId).getReserveTime();
        if (StringUtils.isBlank(reserveTimeStr)){
            emptyCar.setReserveTime("未被预约");
            return;
        }

        LocalDateTime reserveTime = LocalDateTime.parse(reserveTimeStr, dateTimeFormatter);
        LocalDateTime now = LocalDateTime.now();
        // 预约时间必须在现在之后
        // if (reserveTime.isBefore(now)){
        //     emptyCar.setReserveTime("使用中");
        //     return;
        // }

        long minuteNum = ChronoUnit.MINUTES.between(now, reserveTime);
        // 是否为正数
        boolean isPositiveNumber = minuteNum >= 0;
        // 取绝对值
        long minute = Math.abs(minuteNum);
        if (minute < 60){
            emptyCar.setReserveTime("预约" + minute + (isPositiveNumber ? "分钟后使用" : "分钟前使用"));
        }else if (minute < 1440){
            int hour = (int) (minute / 60);
            emptyCar.setReserveTime("预约" + hour + (isPositiveNumber ? "小时后使用" : "小时前使用"));
        }else if (minute < 43200){
            int day = (int) (minute / 1440);
            emptyCar.setReserveTime("预约" + day + (isPositiveNumber ? "天后使用" : "天前使用"));
        }else if (minute < 518400){
            int month = (int) (minute / 43200);
            emptyCar.setReserveTime("预约" + month + (isPositiveNumber ? "月后使用" : "月前使用"));
        }else {
            // 预约时间点距离现在太长，标记一年后
            emptyCar.setReserveTime((isPositiveNumber ? "预约一年后使用" : "预约一年前使用"));
        }
    }
}
