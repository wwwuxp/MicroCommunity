package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarUser.DepartmentStatisticsDto;
import com.java110.dto.pubCarUser.PubCarExistUserDto;
import com.java110.dto.pubCarUser.PubCarUserDto;
import com.java110.po.pubCarUser.PubCarUserPo;
import com.java110.vo.api.pubCarUser.ApiDepartmentNamesDataVo;
import com.java110.vo.api.pubCarUser.ApiDepartmentStatisticsDataVo;
import com.java110.vo.api.pubCarUser.ApiPubCarUserDetailDataVo;
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
@RequestMapping("/pubCarUserApi")
public interface IPubCarUserInnerServiceSMO {

    @RequestMapping(value = "/savePubCarUser", method = RequestMethod.POST)
    int savePubCarUser(@RequestBody PubCarUserPo pubCarUserPo);

    @RequestMapping(value = "/updatePubCarUser", method = RequestMethod.POST)
    int updatePubCarUser(@RequestBody  PubCarUserPo pubCarUserPo);

    @RequestMapping(value = "/deletePubCarUser", method = RequestMethod.POST)
    int deletePubCarUser(@RequestBody  PubCarUserPo pubCarUserPo);

    /**
     * <p>查询用车人信息</p>
     *
     * @param pubCarUserDto 数据对象分享
     * @return PubCarUserDto 对象数据
     */
    @RequestMapping(value = "/queryPubCarUsers", method = RequestMethod.POST)
    List<PubCarUserDto> queryPubCarUsers(@RequestBody PubCarUserDto pubCarUserDto);

    /**
     * 查询<p>用车人</p>总记录数
     *
     * @param pubCarUserDto 数据对象分享
     * @return 用车人记录数
     */
    @RequestMapping(value = "/queryPubCarUsersCount", method = RequestMethod.POST)
    int queryPubCarUsersCount(@RequestBody PubCarUserDto pubCarUserDto);

    @RequestMapping(value = "/queryPubCarUserDetailsCount", method = RequestMethod.POST)
    int queryPubCarUserDetailsCount(@RequestBody PubCarUserDto pubCarUserDto);

    @RequestMapping(value = "/queryPubCarUserDetails", method = RequestMethod.POST)
    List<ApiPubCarUserDetailDataVo> queryPubCarUserDetails(@RequestBody PubCarUserDto pubCarUserDto);

    @RequestMapping(value = "/queryDepartmentStatisticsCount", method = RequestMethod.POST)
    int queryDepartmentStatisticsCount(@RequestBody DepartmentStatisticsDto departmentStatisticsDto);

    @RequestMapping(value = "/queryDepartmentStatistics", method = RequestMethod.POST)
    List<ApiDepartmentStatisticsDataVo> queryDepartmentStatistics(@RequestBody DepartmentStatisticsDto departmentStatisticsDto);

    @RequestMapping(value = "/queryDepartmentNames", method = RequestMethod.GET)
    List<ApiDepartmentNamesDataVo> queryDepartmentNames(@RequestParam("storeId") String storeId);

    @RequestMapping(value = "/listExistPubCarUser", method = RequestMethod.POST)
    List<String> listExistPubCarUser(@RequestBody PubCarExistUserDto existUserDto);
}
