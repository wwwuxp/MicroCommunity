package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarTrip.PubCarTripDto;
import com.java110.dto.pubCarTrip.PubCarTripSimpleDto;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarDataScreen.ApiPubCarDataScreenOverviewAndAnalysisDataVo;
import com.java110.vo.api.pubCarDataScreen.CarTripDataVo;
import com.java110.vo.api.pubCarTrip.*;

import java.util.List;
import java.util.Map;

/**
 * 行程管理表组件内部之间使用，没有给外围系统提供服务能力
 * 行程管理表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IPubCarTripServiceDao {

    /**
     * 保存 行程管理表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void savePubCarTripInfo(Map info) throws DAOException;

    /**
     * 查询行程管理表信息（instance过程）
     * 根据bId 查询行程管理表信息
     * @param info bId 信息
     * @return 行程管理表信息
     * @throws DAOException DAO异常
     */
    List<ApiPubCarTripDataVo> getPubCarTripInfo(PubCarTripDto info) throws DAOException;

    /**
     * 修改行程管理表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePubCarTripInfo(Map info) throws DAOException;

    /**
     * 查询行程管理表总数
     *
     * @param info 行程管理表信息
     * @return 行程管理表数量
     */
    int queryPubCarTripsCount(PubCarTripDto info);

    void saveBusinessPubCarTripInfo(JSONObject businessPubCarTrip);

    List<Map> getBusinessPubCarTripInfo(Map info);

    int queryPubCarDriverTripsCount(PubCarTripSimpleDto pubCarTripSimpleDto);

    /**
     * 获取司机行程信息
     * @param pubCarTripSimpleDto 参数数据
     * @return list
     */
    List<ApiPubCarTripDataVo> queryPubCarDriverTrips(PubCarTripSimpleDto pubCarTripSimpleDto);

    int queryPubCarSimpleTripsCount(PubCarTripSimpleDto pubCarTripSimpleDto);

    List<ApiSimpleTripDataVo> queryPubCarSimpleTrips(PubCarTripSimpleDto pubCarTripSimpleDto);

    /**
     * 车辆统计中查询当前车辆行程信息
     * @param pubCarTripSimpleDto 参数
     * @return int
     */
    int queryCarTripStatisticsCount(PubCarTripSimpleDto pubCarTripSimpleDto);

    /**
     * 车辆统计中查询当前车辆行程信息
     * @param pubCarTripSimpleDto 参数
     * @return list
     */
    List<ApiCarTripStatisticsDataVo> queryCarTripStatistics(PubCarTripSimpleDto pubCarTripSimpleDto);

    ApiPubCarDataScreenOverviewAndAnalysisDataVo queryPubCarDataScreenOverview(Map<String, Object> map);

    Map<String, Object> queryPubCarDataScreenSingleDayOverview(Map<String, Object> map);

    List<CarTripDataVo> queryPubCarDataScreenCarUseData(Map<String, Object> map);

    List<Map<String, Object>> queryPubCarDataScreenTodayCarUseStatistics(Map<String, Object> map);

    int queryPubCarDataScreenCarUseDataCount(Map<String, Object> map);

    /**
     * 明天或未来7天内的用车数据统计部分
     * @param map 参数
     * @return list
     */
    List<Map<String, Object>> queryPubCarDataScreenTomorrowOrOneWeekCarUseStatistics(Map<String, Object> map);

    List<Map> getPubCarTripInstanceInfo(Map<String, String> info);

    int listChooseTripsCount(PubCarTripSimpleDto info);

    List<ApiChooseTripDataVo> listChooseTrips(PubCarTripSimpleDto pubCarTripSimpleDto);

    List<Map<String, String>> getDriverIdByCarIdAndViolationTimeAndCommunityId(Map<String, Object> map);

    List<PubCarTripDto> queryPubCarTripsByTripIdsBatch(List<String> tripIds);

    /**
     * 查询用车记录数量
     * @param pubCarTripDto 参数
     * @return int
     */
    int queryPubCarUseRecordsCount(PubCarTripDto pubCarTripDto);

    /**
     * 查询用车记录列表
     * @param pubCarTripDto 参数
     * @return list
     */
    List<ApiCarUseRecordDataVo> queryPubCarUseRecords(PubCarTripDto pubCarTripDto);

}
