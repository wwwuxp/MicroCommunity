package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendance.PassRecordDto;
import com.java110.vo.api.passRecord.ApiPassRecordDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author hh
 * @date 2022/1/26
 */
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/passRecordApi")
public interface IPassRecordInnerServiceSMO {


    /**
     * 查询总的通行记录数量
     * @param passRecordDto 参数
     * @return int
     */
    @RequestMapping(value = "/queryPassRecordsCount", method = RequestMethod.POST)
    int queryPassRecordsCount(@RequestBody PassRecordDto passRecordDto);

    /**
     * 查询通行记录列表数据
     * @param passRecordDto 参数
     * @return List
     */
    @RequestMapping(value = "/queryPassRecords", method = RequestMethod.POST)
    List<ApiPassRecordDataVo> queryPassRecords(@RequestBody PassRecordDto passRecordDto);

}
