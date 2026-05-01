package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarViolation.PubCarViolationDto;
import com.java110.po.pubCarViolation.PubCarViolationPo;
import com.java110.vo.api.pubCarViolation.ApiPubCarViolationDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IPubCarViolationInnerServiceSMO
 * @Description 车辆违章信息表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarViolationApi")
public interface IPubCarViolationInnerServiceSMO {

    @RequestMapping(value = "/savePubCarViolation", method = RequestMethod.POST)
    int savePubCarViolation(@RequestBody PubCarViolationPo pubCarViolationPo);

    @RequestMapping(value = "/updatePubCarViolation", method = RequestMethod.POST)
    int updatePubCarViolation(@RequestBody  PubCarViolationPo pubCarViolationPo);

    @RequestMapping(value = "/deletePubCarViolation", method = RequestMethod.POST)
    int deletePubCarViolation(@RequestBody  PubCarViolationPo pubCarViolationPo);

    @RequestMapping(value = "/queryPubCarViolations", method = RequestMethod.POST)
    List<ApiPubCarViolationDataVo> queryPubCarViolations(@RequestBody PubCarViolationDto pubCarViolationDto);

    @RequestMapping(value = "/queryPubCarViolationsCount", method = RequestMethod.POST)
    int queryPubCarViolationsCount(@RequestBody PubCarViolationDto pubCarViolationDto);

}
