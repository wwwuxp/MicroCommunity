package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarGroup.PubCarGroupDto;
import com.java110.po.pubCarGroup.PubCarGroupPo;
import com.java110.vo.api.pubCarGroup.ApiPubCarGroupDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName IPubCarGroupInnerServiceSMO
 * @Description 分组信息表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarGroupApi")
public interface IPubCarGroupInnerServiceSMO {

    @RequestMapping(value = "/savePubCarGroup", method = RequestMethod.POST)
    int savePubCarGroup(@RequestBody PubCarGroupPo pubCarGroupPo);

    @RequestMapping(value = "/updatePubCarGroup", method = RequestMethod.POST)
    int updatePubCarGroup(@RequestBody  PubCarGroupPo pubCarGroupPo);

    @RequestMapping(value = "/deletePubCarGroup", method = RequestMethod.POST)
    int deletePubCarGroup(@RequestBody  PubCarGroupPo pubCarGroupPo);

    @RequestMapping(value = "/queryPubCarGroups", method = RequestMethod.POST)
    List<PubCarGroupDto> queryPubCarGroups(@RequestBody PubCarGroupDto pubCarGroupDto);

    @RequestMapping(value = "/queryPubCarGroupsCount", method = RequestMethod.POST)
    int queryPubCarGroupsCount(@RequestBody PubCarGroupDto pubCarGroupDto);

    @RequestMapping(value = "/queryPubCarGroupNames", method = RequestMethod.GET)
    List<ApiPubCarGroupDataVo> queryPubCarGroupNames(@RequestParam("communityId") String communityId);

    @RequestMapping(value = "/existPubCarGroup", method = RequestMethod.POST)
    boolean existPubCarGroup(@RequestBody PubCarGroupDto existGroupDto);

}
