package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.po.pubCarTripMileage.PubCarTripMileagePo;
import com.java110.vo.api.pubCarTripMileage.ApiTripMileageAndFeeDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName IPubCarTripMileageInnerServiceSMO
 * @Description 行驶里程信息表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarTripMileageApi")
public interface IPubCarTripMileageInnerServiceSMO {

    @RequestMapping(value = "/savePubCarTripMileage", method = RequestMethod.POST)
    int savePubCarTripMileage(@RequestBody PubCarTripMileagePo pubCarTripMileagePo);

    @RequestMapping(value = "/updatePubCarTripMileage", method = RequestMethod.POST)
    int updatePubCarTripMileage(@RequestBody  PubCarTripMileagePo pubCarTripMileagePo);

    @RequestMapping(value = "/deletePubCarTripMileage", method = RequestMethod.POST)
    int deletePubCarTripMileage(@RequestBody  PubCarTripMileagePo pubCarTripMileagePo);

    @RequestMapping(value = "/queryPubCarTripMileages", method = RequestMethod.POST)
    List<PubCarTripMileageDto> queryPubCarTripMileages(@RequestBody PubCarTripMileageDto pubCarTripMileageDto);

    @RequestMapping(value = "/queryPubCarTripMileagesCount", method = RequestMethod.POST)
    int queryPubCarTripMileagesCount(@RequestBody PubCarTripMileageDto pubCarTripMileageDto);

    @RequestMapping(value = "/queryAllotCarTrip", method = RequestMethod.GET)
    List<PubCarTripMileageDto> queryAllotCarTrip(@RequestParam("tripId") String tripId,
                                                 @RequestParam("communityId") String communityId);

    @RequestMapping(value = "/getDriverTripIsInProgress", method = RequestMethod.POST)
    boolean getDriverTripIsInProgress(@RequestBody PubCarTripMileageDto pubCarTripMileageDto);

    /**
     * 查询上一部车的结束里程
     * @param carId 车辆id
     * @param communityId 小区id
     * @return Double
     */
    @RequestMapping(value = "/queryEndMileageOfPreviousCar", method = RequestMethod.GET)
    Double queryEndMileageOfPreviousCar(@RequestParam("carId") String carId,
                                        @RequestParam("communityId") String communityId);

    /**
     * 获取行程里程登记信息
     * @param pubCarTripMileageDto pubCarTripMileageDto
     * @return ApiTripMileageAndFeeDataVo
     */
    @RequestMapping(value = "/getTripMileageAndFee", method = RequestMethod.POST)
    ApiTripMileageAndFeeDataVo getTripMileageAndFee(@RequestBody PubCarTripMileageDto pubCarTripMileageDto);

    /**
     * 更新行程里程和费用信息
     * @param pubCarTripMileageDto pubCarTripMileageDto
     * @return int
     */
    @RequestMapping(value = "/updateTripMileageAndFee", method = RequestMethod.POST)
    int updateTripMileageAndFee(@RequestBody PubCarTripMileageDto pubCarTripMileageDto);

}
