package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarReimburse.PubCarReimburseDto;
import com.java110.vo.api.pubCarReimburse.ApiPubCarReimburseDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author hh
 * @date 2021/10/12
 */
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarReimburseApi")
public interface IPubCarReimburseInnerServiceSMO {

    /**
     * 查询用车报销列表数量
     */
    @RequestMapping(value = "/listPubCarAccidentsCount", method = RequestMethod.POST)
    int listPubCarReimbursesCount(@RequestBody PubCarReimburseDto pubCarReimburseDto);

    /**
     * 查询用车报销列表
     */
    @RequestMapping(value = "/listPubCarReimburses", method = RequestMethod.POST)
    List<ApiPubCarReimburseDataVo> listPubCarReimburses(@RequestBody PubCarReimburseDto pubCarReimburseDto);

    /**
     * 审批用车报销信息
     */
    @RequestMapping(value = "/auditPubCarReimburses", method = RequestMethod.POST)
    boolean auditPubCarReimburses(@RequestBody PubCarReimburseDto pubCarReimburseDto);

}
