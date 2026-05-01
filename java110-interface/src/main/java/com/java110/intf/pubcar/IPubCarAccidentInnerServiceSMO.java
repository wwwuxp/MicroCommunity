package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarAccident.PubCarAccidentDto;
import com.java110.vo.api.pubCarAccident.ApiPubCarAccidentDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author hh
 * @date 2021/10/11
 */
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarAccidentApi")
public interface IPubCarAccidentInnerServiceSMO {

    /**
     * 查询用车事故列表数量
     */
    @RequestMapping(value = "/listPubCarAccidentsCount", method = RequestMethod.POST)
    int listPubCarAccidentsCount(@RequestBody PubCarAccidentDto pubCarAccidentDto);

    /**
     * 查询用车事故列表
     */
    @RequestMapping(value = "/listPubCarAccidents", method = RequestMethod.POST)
    List<ApiPubCarAccidentDataVo> listPubCarAccidents(@RequestBody PubCarAccidentDto pubCarAccidentDto);

}
