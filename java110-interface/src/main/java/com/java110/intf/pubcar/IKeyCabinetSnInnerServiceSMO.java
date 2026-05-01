package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.keyCabinetSn.KeyCabinetSnDto;
import com.java110.vo.api.keyCabinetSn.ApiKeyCabinetSnDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author hh
 * @date 2022/2/18
 */
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/keyCabinetSnApi")
public interface IKeyCabinetSnInnerServiceSMO {

    /**
     * 保存钥匙柜设备信息
     * @param keyCabinetSnDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/saveKeyCabinetSn", method = RequestMethod.POST)
    int saveKeyCabinetSn(@RequestBody KeyCabinetSnDto keyCabinetSnDto);

    /**
     * 查询钥匙柜总数
     * @param keyCabinetSnDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/queryKeyCabinetSnsCount", method = RequestMethod.POST)
    int queryKeyCabinetSnsCount(@RequestBody KeyCabinetSnDto keyCabinetSnDto);

    /**
     * 查询钥匙柜列表
     * @param keyCabinetSnDto 请求参数
     * @return List
     */
    @RequestMapping(value = "/queryKeyCabinetSns", method = RequestMethod.POST)
    List<ApiKeyCabinetSnDataVo> queryKeyCabinetSns(@RequestBody KeyCabinetSnDto keyCabinetSnDto);

    /**
     * 是否存在相同设备sn
     * @param keyCabinetSnDto 请求参数
     * @return boolean
     */
    @RequestMapping(value = "/existSameDeviceSn", method = RequestMethod.POST)
    boolean existSameDeviceSn(@RequestBody KeyCabinetSnDto keyCabinetSnDto);

    /**
     * 更新钥匙柜信息
     * @param keyCabinetSnDto 请求参数
     */
    @RequestMapping(value = "/updateKeyCabinetSn", method = RequestMethod.POST)
    void updateKeyCabinetSn(@RequestBody KeyCabinetSnDto keyCabinetSnDto);

}
