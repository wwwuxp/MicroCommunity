package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.dto.outGoingPersonnel.OutGoingPersonnelDto;
import com.java110.po.outGoingPersonnel.OutGoingPersonnelPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOutGoingPersonnelInnerServiceSMO
 * @Description 外出人员信息表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/outGoingPersonnelApi")
public interface IOutGoingPersonnelInnerServiceSMO {

    @RequestMapping(value = "/saveOutGoingPersonnel", method = RequestMethod.POST)
    int saveOutGoingPersonnel(@RequestBody  OutGoingPersonnelPo outGoingPersonnelPo);

    @RequestMapping(value = "/updateOutGoingPersonnel", method = RequestMethod.POST)
    int updateOutGoingPersonnel(@RequestBody OutGoingPersonnelPo outGoingPersonnelPo);

    @RequestMapping(value = "/deleteOutGoingPersonnel", method = RequestMethod.POST)
    int deleteOutGoingPersonnel(@RequestBody  OutGoingPersonnelPo outGoingPersonnelPo);

    @RequestMapping(value = "/queryOutGoingPersonnels", method = RequestMethod.POST)
    List<OutGoingPersonnelDto> queryOutGoingPersonnels(@RequestBody OutGoingPersonnelDto outGoingPersonnelDto);

    @RequestMapping(value = "/queryOutGoingPersonnelsCount", method = RequestMethod.POST)
    int queryOutGoingPersonnelsCount(@RequestBody OutGoingPersonnelDto outGoingPersonnelDto);

    /**
     * 根据姓名，部门，职位来查询所有的外出时间段
     */
    @RequestMapping(value = "/listOutGoingTimesByUserInfo", method = RequestMethod.POST)
    List<OutGoingDto> listOutGoingTimesByUserInfo(@RequestBody UserInfoDto userInfoDto);

    @RequestMapping(value = "/isOutGoing", method = RequestMethod.POST)
    boolean isOutGoing(@RequestBody UserInfoDto userInfoDto);
}
