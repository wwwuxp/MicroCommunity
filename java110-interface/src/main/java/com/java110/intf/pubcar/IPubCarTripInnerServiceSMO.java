package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarTrip.PubCarTripDto;
import com.java110.dto.pubCarTrip.PubCarTripSimpleDto;
import com.java110.po.pubCarTrip.PubCarTripPo;
import com.java110.vo.api.pubCarDataScreen.ApiPubCarDataScreenOverviewAndAnalysisDataVo;
import com.java110.vo.api.pubCarDataScreen.CarTripDataVo;
import com.java110.vo.api.pubCarTrip.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName IPubCarTripInnerServiceSMO
 * @Description 行程管理表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarTripApi")
public interface IPubCarTripInnerServiceSMO {

    @RequestMapping(value = "/savePubCarTrip", method = RequestMethod.POST)
    int savePubCarTrip(@RequestBody PubCarTripPo pubCarTripPo);

    @RequestMapping(value = "/updatePubCarTrip", method = RequestMethod.POST)
    int updatePubCarTrip(@RequestBody  PubCarTripPo pubCarTripPo);

    @RequestMapping(value = "/deletePubCarTrip", method = RequestMethod.POST)
    int deletePubCarTrip(@RequestBody  PubCarTripPo pubCarTripPo);

    @RequestMapping(value = "/queryPubCarTrips", method = RequestMethod.POST)
    List<ApiPubCarTripDataVo> queryPubCarTrips(@RequestBody PubCarTripDto pubCarTripDto);

    @RequestMapping(value = "/queryPubCarTripsCount", method = RequestMethod.POST)
    int queryPubCarTripsCount(@RequestBody PubCarTripDto pubCarTripDto);

    @RequestMapping(value = "/queryPubCarDriverTripsCount", method = RequestMethod.POST)
    int queryPubCarDriverTripsCount(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto);

    /**
     * 获取司机行程信息
     * @param pubCarTripSimpleDto 数据
     * @return list
     */
    @RequestMapping(value = "/queryPubCarDriverTrips", method = RequestMethod.POST)
    List<ApiPubCarTripDataVo> queryPubCarDriverTrips(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto);

    @RequestMapping(value = "/queryPubCarSimpleTripsCount", method = RequestMethod.POST)
    int queryPubCarSimpleTripsCount(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto);

    @RequestMapping(value = "/queryPubCarSimpleTrips", method = RequestMethod.POST)
    List<ApiSimpleTripDataVo> queryPubCarSimpleTrips(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto);

    /**
     * 车辆统计中查询当前车辆行程信息数量
     * @param pubCarTripSimpleDto 参数
     * @return int
     */
    @RequestMapping(value = "/queryCarTripStatisticsCount", method = RequestMethod.POST)
    int queryCarTripStatisticsCount(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto);

    /**
     * 车辆统计中查询当前车辆行程信息
     * @param pubCarTripSimpleDto 参数
     * @return list
     */
    @RequestMapping(value = "/queryCarTripStatistics", method = RequestMethod.POST)
    List<ApiCarTripStatisticsDataVo> queryCarTripStatistics(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto);

    /**
     * 获取数据总览、用车数据中的统计部分、部门用车分析、司机驾驶分析、车辆使用分析数据
     * @param startTimeStr 开始时间
     * @param endTimeStr 结束时间
     * @param storeId storeId
     * @param communityId 小区id
     * @return ApiPubCarDataScreenOverviewAndAnalysisDataVo
     */
    @RequestMapping(value = "/queryPubCarDataScreenOverview", method = RequestMethod.GET)
    ApiPubCarDataScreenOverviewAndAnalysisDataVo queryPubCarDataScreenOverviewAndAnalysisData(@RequestParam("startTimeStr") String startTimeStr,
                                                                                              @RequestParam("endTimeStr") String endTimeStr,
                                                                                              @RequestParam(value = "storeId", required = false) String storeId,
                                                                                              @RequestParam(value = "communityId", required = false) String communityId);

    @RequestMapping(value = "/queryPubCarDataScreenCarUseData", method = RequestMethod.GET)
    List<CarTripDataVo> queryPubCarDataScreenCarUseData(@RequestParam("page") Integer page,
                                                        @RequestParam("row") Integer row,
                                                        @RequestParam("symbol") Integer symbol,
                                                        @RequestParam(value = "communityId", required = false) String communityId);

    @RequestMapping(value = "/queryPubCarDataScreenCarUseDataCount", method = RequestMethod.GET)
    int queryPubCarDataScreenCarUseDataCount(@RequestParam("symbol") Integer symbol,
                                             @RequestParam(value = "communityId", required = false) String communityId);


    /**
     * 查询选择行程列表数量
     * @param pubCarTripSimpleDto 参数
     * @return int
     */
    @RequestMapping(value = "/listChooseTripsCount", method = RequestMethod.POST)
    int listChooseTripsCount(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto);

    /**
     * 查询选择行程列表
     * @param pubCarTripSimpleDto 参数
     * @return list
     */
    @RequestMapping(value = "/listChooseTrips", method = RequestMethod.POST)
    List<ApiChooseTripDataVo> listChooseTrips(@RequestBody PubCarTripSimpleDto pubCarTripSimpleDto);

    /**
     * 根据车辆id和违章时间获取司机id
     * @param carId 车辆id
     * @param violationTime 违章时间
     * @param communityId 小区id
     * @return 司机id集合
     */
    @RequestMapping(value = "/getDriverIdByCarIdAndViolationTimeAndCommunityId", method = RequestMethod.GET)
    List<String> getDriverIdByCarIdAndViolationTimeAndCommunityId(@RequestParam("carId") String carId,
                                                                  @RequestParam("violationTime") String violationTime,
                                                                  @RequestParam("communityId") String communityId);

    /**
     * 根据行程id集合批量获取行程信息
     * @param tripIds 行程id集合
     * @return list
     */
    @RequestMapping(value = "/queryPubCarTripsByTripIdsBatch", method = RequestMethod.GET)
    List<PubCarTripDto> queryPubCarTripsByTripIdsBatch(@RequestParam("tripIds") List<String> tripIds);

    /**
     * 查询用车记录数量
     * @param pubCarTripDto 参数
     * @return int
     */
    @RequestMapping(value = "/queryPubCarUseRecordsCount", method = RequestMethod.POST)
    int queryPubCarUseRecordsCount(@RequestBody PubCarTripDto pubCarTripDto);

    /**
     * 查询用车记录列表
     * @param pubCarTripDto 参数
     * @return list
     */
    @RequestMapping(value = "/queryPubCarUseRecords", method = RequestMethod.POST)
    List<ApiCarUseRecordDataVo> queryPubCarUseRecords(@RequestBody PubCarTripDto pubCarTripDto);

}
