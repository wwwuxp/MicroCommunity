package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarTripCarPassenger.PubCarTripCarPassengerDto;
import com.java110.po.pubCarTripCarPassenger.PubCarTripCarPassengerPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IPubCarTripCarPassengerInnerServiceSMO
 * @Description 乘车人信息管理表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarTripCarPassengerApi")
public interface IPubCarTripCarPassengerInnerServiceSMO {

    @RequestMapping(value = "/savePubCarTripCarPassenger", method = RequestMethod.POST)
    int savePubCarTripCarPassenger(@RequestBody PubCarTripCarPassengerPo pubCarTripCarPassengerPo);

    @RequestMapping(value = "/updatePubCarTripCarPassenger", method = RequestMethod.POST)
    int updatePubCarTripCarPassenger(@RequestBody  PubCarTripCarPassengerPo pubCarTripCarPassengerPo);

    @RequestMapping(value = "/deletePubCarTripCarPassenger", method = RequestMethod.POST)
    int deletePubCarTripCarPassenger(@RequestBody  PubCarTripCarPassengerPo pubCarTripCarPassengerPo);

    @RequestMapping(value = "/queryPubCarTripCarPassengers", method = RequestMethod.POST)
    List<PubCarTripCarPassengerDto> queryPubCarTripCarPassengers(@RequestBody PubCarTripCarPassengerDto pubCarTripCarPassengerDto);

    @RequestMapping(value = "/queryPubCarTripCarPassengersCount", method = RequestMethod.POST)
    int queryPubCarTripCarPassengersCount(@RequestBody PubCarTripCarPassengerDto pubCarTripCarPassengerDto);

}
