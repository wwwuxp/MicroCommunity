package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarDriver.PubCarDriverDto;
import com.java110.dto.pubCarDriver.PubCarExistDriverDto;
import com.java110.dto.pubCarDriver.PubCarSimpleDriverDto;
import com.java110.po.pubCarDriver.PubCarDriverPo;
import com.java110.vo.api.pubCarTripDriver.ApiPubCarDriverDataVo;
import com.java110.vo.api.pubCarTripDriver.ApiPubCarSimpleDriverDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName IPubCarUserInnerServiceSMO
 * @Description 用车人管理表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarDriverApi")
public interface IPubCarDriverInnerServiceSMO {

    @RequestMapping(value = "/savePubCarDriver", method = RequestMethod.POST)
    int savePubCarDriver(@RequestBody PubCarDriverPo pubCarDriverPo);

    @RequestMapping(value = "/updatePubCarDriver", method = RequestMethod.POST)
    int updatePubCarDriver(@RequestBody PubCarDriverPo pubCarDriverPo);

    @RequestMapping(value = "/deletePubCarDriver", method = RequestMethod.POST)
    int deletePubCarDriver(@RequestBody PubCarDriverPo pubCarDriverPo);

    @RequestMapping(value = "/queryPubCarDrivers", method = RequestMethod.POST)
    List<ApiPubCarDriverDataVo> queryPubCarDrivers(@RequestBody PubCarDriverDto pubCarDriverDto);

    @RequestMapping(value = "/queryPubCarDriversCount", method = RequestMethod.POST)
    int queryPubCarDriversCount(@RequestBody PubCarDriverDto pubCarDriverDto);

    @RequestMapping(value = "/queryPubCarDriversByValidateCondition", method = RequestMethod.GET)
    String queryPubCarDriversByValidateCondition(String jobNum);

    @RequestMapping(value = "/queryPubCarDriverNames", method = RequestMethod.GET)
    List<ApiPubCarSimpleDriverDataVo> queryPubCarDriverNames();

    @RequestMapping(value = "/existPubCarDriver", method = RequestMethod.POST)
    boolean existPubCarDriver(@RequestBody PubCarExistDriverDto existDriverDto);

}
