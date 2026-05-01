package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarRepair.PubCarRepairDto;
import com.java110.po.pubCarRepair.PubCarRepairPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IPubCarRepairInnerServiceSMO
 * @Description 车辆维修信息表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarRepairApi")
public interface IPubCarRepairInnerServiceSMO {

    @RequestMapping(value = "/savePubCarRepair", method = RequestMethod.POST)
    int savePubCarRepair(@RequestBody PubCarRepairPo pubCarRepairPo);

    @RequestMapping(value = "/updatePubCarRepair", method = RequestMethod.POST)
    int updatePubCarRepair(@RequestBody  PubCarRepairPo pubCarRepairPo);

    @RequestMapping(value = "/deletePubCarRepair", method = RequestMethod.POST)
    int deletePubCarRepair(@RequestBody  PubCarRepairPo pubCarRepairPo);

    @RequestMapping(value = "/queryPubCarRepairs", method = RequestMethod.POST)
    List<PubCarRepairDto> queryPubCarRepairs(@RequestBody PubCarRepairDto pubCarRepairDto);

    @RequestMapping(value = "/queryPubCarRepairsCount", method = RequestMethod.POST)
    int queryPubCarRepairsCount(@RequestBody PubCarRepairDto pubCarRepairDto);

}
