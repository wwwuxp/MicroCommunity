package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCar.ExistPubCarDto;
import com.java110.dto.pubCar.PubCarDto;
import com.java110.po.pubCar.PubCarPo;
import com.java110.vo.api.pubCar.ApiCarStatisticsDataVo;
import com.java110.vo.api.pubCar.ApiSimplePubCarDataVo;
import com.java110.vo.api.pubCarArchives.ApiPubCarArchivesDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IPubCarInnerServiceSMO
 * @Description 车辆管理表接口类
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarApi")
public interface IPubCarInnerServiceSMO {

    @RequestMapping(value = "/savePubCar", method = RequestMethod.POST)
    int savePubCar(@RequestBody PubCarPo pubCarPo);

    @RequestMapping(value = "/updatePubCar", method = RequestMethod.POST)
    int updatePubCar(@RequestBody  PubCarPo pubCarPo);

    @RequestMapping(value = "/deletePubCar", method = RequestMethod.POST)
    int deletePubCar(@RequestBody  PubCarPo pubCarPo);

    @RequestMapping(value = "/queryPubCars", method = RequestMethod.POST)
    List<PubCarDto> queryPubCars(@RequestBody PubCarDto pubCarDto);

    @RequestMapping(value = "/queryPubCarsCount", method = RequestMethod.POST)
    int queryPubCarsCount(@RequestBody PubCarDto pubCarDto);

    @RequestMapping(value = "/queryInfoByCarId", method = RequestMethod.GET)
    List<Map<String, Object>> queryInfoByCarId(@RequestParam("carId") String carId);

    /**
     * 查询简单车辆信息
     * @return list
     */
    @RequestMapping(value = "/querySimplePubCars", method = RequestMethod.GET)
    List<ApiSimplePubCarDataVo> querySimplePubCars(@RequestParam("communityId") String communityId,
                                                   @RequestParam("startTime") String startTime,
                                                   @RequestParam("endTime") String endTime,
                                                   @RequestParam("carType") Integer carType);

    @RequestMapping(value = "/queryCarStatisticsCount", method = RequestMethod.POST)
    int queryCarStatisticsCount(@RequestBody PubCarDto pubCarDto);

    @RequestMapping(value = "/queryCarStatistics", method = RequestMethod.POST)
    List<ApiCarStatisticsDataVo> queryCarStatistics(@RequestBody PubCarDto pubCarDto);

    @RequestMapping(value = "/queryPubCarArchivesCount", method = RequestMethod.POST)
    int queryPubCarArchivesCount(@RequestBody PubCarDto pubCarDto);

    @RequestMapping(value = "/queryPubCarArchives", method = RequestMethod.POST)
    List<ApiPubCarArchivesDataVo> queryPubCarArchives(@RequestBody PubCarDto pubCarDto);

    /**
     * 车辆是否已经存在
     * @param existPubCarDto 参数
     * @return ExistPubCarDto
     */
    @RequestMapping(value = "/existPubCar", method = RequestMethod.POST)
    ExistPubCarDto existPubCar(@RequestBody ExistPubCarDto existPubCarDto);

    /**
     * 获取空车数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param carType 车辆类型
     * @param communityId 小区id
     * @return int
     */
    @RequestMapping(value = "/countEmptyPubCar", method = RequestMethod.GET)
    int countEmptyPubCar(@RequestParam("startTime") String startTime,
                         @RequestParam("endTime") String endTime,
                         @RequestParam("carType") Integer carType,
                         @RequestParam("communityId") String communityId);

}
