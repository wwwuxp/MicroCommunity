package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.po.outGoing.OutGoingPo;
import com.java110.vo.api.outGoing.ApiOutGoingDataVo;
import com.java110.vo.api.outGoing.ApiOutGoingStatisticsDataVo;
import com.java110.vo.api.outGoing.ApiUserOutGoingDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOutGoingInnerServiceSMO
 * @Description 外出信息表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/outGoingApi")
public interface IOutGoingInnerServiceSMO {

    @RequestMapping(value = "/saveOutGoing", method = RequestMethod.POST)
    int saveOutGoing(@RequestBody OutGoingPo outGoingPo);

    @RequestMapping(value = "/updateOutGoing", method = RequestMethod.POST)
    int updateOutGoing(@RequestBody  OutGoingPo outGoingPo);

    @RequestMapping(value = "/deleteOutGoing", method = RequestMethod.POST)
    int deleteOutGoing(@RequestBody  OutGoingPo outGoingPo);

    @RequestMapping(value = "/queryOutGoings", method = RequestMethod.POST)
    List<ApiOutGoingDataVo> queryOutGoings(@RequestBody OutGoingDto outGoingDto);

    @RequestMapping(value = "/queryOutGoingsCount", method = RequestMethod.POST)
    int queryOutGoingsCount(@RequestBody OutGoingDto outGoingDto);

    @RequestMapping(value = "/queryUserOutGoings", method = RequestMethod.POST)
    List<ApiUserOutGoingDataVo> queryUserOutGoings(@RequestBody OutGoingDto outGoingDto);

    /**
     * 查询外出统计列表
     * @param outGoingDto 请求参数
     * @return List
     */
    @RequestMapping(value = "/queryOutGoingStatistics", method = RequestMethod.POST)
    List<ApiOutGoingStatisticsDataVo> queryOutGoingStatistics(@RequestBody OutGoingDto outGoingDto);

    /**
     * 查询外出统计数量
     * @param outGoingDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/queryOutGoingStatisticsCount", method = RequestMethod.POST)
    int queryOutGoingStatisticsCount(@RequestBody OutGoingDto outGoingDto);

}
