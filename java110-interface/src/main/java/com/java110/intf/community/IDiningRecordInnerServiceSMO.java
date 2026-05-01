package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.diningRecord.DiningRecordDto;
import com.java110.vo.api.diningRecord.ApiCountTotalDiningRecordVo;
import com.java110.vo.api.diningRecord.ApiDiningDetailDataVo;
import com.java110.vo.api.diningRecord.ApiDiningRecordDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author hh
 * @date 2021/11/11
 */
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/diningRecordApi")
public interface IDiningRecordInnerServiceSMO {

    /**
     * 查询就餐记录统计总数量
     * @param diningRecordDto 请求参数
     * @return int
     */
    @RequestMapping(value = "/queryDiningRecordStatisticsCount", method = RequestMethod.POST)
    int queryDiningRecordStatisticsCount(@RequestBody DiningRecordDto diningRecordDto);

    /**
     * 查询就餐记录统计
     * @param diningRecordDto 请求参数
     * @return list
     */
    @RequestMapping(value = "/queryDiningRecordStatistics", method = RequestMethod.POST)
    List<ApiDiningRecordDataVo> queryDiningRecordStatistics(@RequestBody DiningRecordDto diningRecordDto);

    /**
     * 获取员工就餐详情
     * @param diningRecordDto 请求参数
     * @return list
     */
    @RequestMapping(value = "/queryDiningDetails", method = RequestMethod.POST)
    List<ApiDiningDetailDataVo> queryDiningDetails(@RequestBody DiningRecordDto diningRecordDto);

    /**
     * 查询总的就餐统计数据
     * @param diningRecordDto 请求参数
     * @return ApiCountTotalDiningRecordVo
     */
    @RequestMapping(value = "/queryTotalDiningRecordStatisticsCount", method = RequestMethod.POST)
    ApiCountTotalDiningRecordVo queryTotalDiningRecordStatisticsCount(@RequestBody DiningRecordDto diningRecordDto);

}
