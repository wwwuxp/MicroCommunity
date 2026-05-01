package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.outGoingUnitContact.OutGoingUnitContactDto;
import com.java110.po.outGoingUnitContact.OutGoingUnitContactPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOutGoingUnitContactInnerServiceSMO
 * @Description 外出单位联系人信息表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/outGoingUnitContactApi")
public interface IOutGoingUnitContactInnerServiceSMO {

    @RequestMapping(value = "/saveOutGoingUnitContact", method = RequestMethod.POST)
    int saveOutGoingUnitContact(@RequestBody OutGoingUnitContactPo outGoingUnitContactPo);

    @RequestMapping(value = "/updateOutGoingUnitContact", method = RequestMethod.POST)
    int updateOutGoingUnitContact(@RequestBody  OutGoingUnitContactPo outGoingUnitContactPo);

    @RequestMapping(value = "/deleteOutGoingUnitContact", method = RequestMethod.POST)
    int deleteOutGoingUnitContact(@RequestBody  OutGoingUnitContactPo outGoingUnitContactPo);

    @RequestMapping(value = "/queryOutGoingUnitContacts", method = RequestMethod.POST)
    List<OutGoingUnitContactDto> queryOutGoingUnitContacts(@RequestBody OutGoingUnitContactDto outGoingUnitContactDto);

    @RequestMapping(value = "/queryOutGoingUnitContactsCount", method = RequestMethod.POST)
    int queryOutGoingUnitContactsCount(@RequestBody OutGoingUnitContactDto outGoingUnitContactDto);

}
