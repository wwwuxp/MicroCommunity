package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarTripCarNum.PubCarTripCarNumDto;
import com.java110.po.pubCarTripCarNum.PubCarTripCarNumPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName IPubCarTripCarNumInnerServiceSMO
 * @Description 申请车辆数量管理表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarTripCarNumApi")
public interface IPubCarTripCarNumInnerServiceSMO {

    @RequestMapping(value = "/savePubCarTripCarNum", method = RequestMethod.POST)
    int savePubCarTripCarNum(@RequestBody PubCarTripCarNumPo pubCarTripCarNumPo);

    @RequestMapping(value = "/updatePubCarTripCarNum", method = RequestMethod.POST)
    int updatePubCarTripCarNum(@RequestBody  PubCarTripCarNumPo pubCarTripCarNumPo);

    @RequestMapping(value = "/deletePubCarTripCarNum", method = RequestMethod.POST)
    int deletePubCarTripCarNum(@RequestBody  PubCarTripCarNumPo pubCarTripCarNumPo);

    @RequestMapping(value = "/queryPubCarTripCarNums", method = RequestMethod.POST)
    List<PubCarTripCarNumDto> queryPubCarTripCarNums(@RequestBody PubCarTripCarNumDto pubCarTripCarNumDto);

    @RequestMapping(value = "/queryPubCarTripCarNumsCount", method = RequestMethod.POST)
    int queryPubCarTripCarNumsCount(@RequestBody PubCarTripCarNumDto pubCarTripCarNumDto);

    @RequestMapping(value = "/getTripTypeAndNoByTripId", method = RequestMethod.GET)
    List<PubCarTripCarNumDto> getTripTypeAndNoByTripId(@RequestParam("tripId") String tripId);

}
