package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.pubCarAlarm.PubCarAlarmDto;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.vo.api.pubCarAlarm.ApiPubCarAlarmDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IPubCarAlarmInnerServiceSMO
 * @Description 异常报警信息表接口类
 * add by hh
 **/
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/pubCarAlarmApi")
public interface IPubCarAlarmInnerServiceSMO {

    @RequestMapping(value = "/savePubCarAlarm", method = RequestMethod.POST)
    int savePubCarAlarm(@RequestBody PubCarAlarmPo pubCarAlarmPo);

    @RequestMapping(value = "/updatePubCarAlarm", method = RequestMethod.POST)
    int updatePubCarAlarm(@RequestBody  PubCarAlarmPo pubCarAlarmPo);

    @RequestMapping(value = "/deletePubCarAlarm", method = RequestMethod.POST)
    int deletePubCarAlarm(@RequestBody  PubCarAlarmPo pubCarAlarmPo);

    /**
     * 查询异常报警列表
     * @param pubCarAlarmDto 参数
     * @return list
     */
    @RequestMapping(value = "/queryPubCarAlarms", method = RequestMethod.POST)
    List<ApiPubCarAlarmDataVo> queryPubCarAlarms(@RequestBody PubCarAlarmDto pubCarAlarmDto);

    /**
     * 查询异常报警列表数量
     * @param pubCarAlarmDto 参数
     * @return int
     */
    @RequestMapping(value = "/queryPubCarAlarmsCount", method = RequestMethod.POST)
    int queryPubCarAlarmsCount(@RequestBody PubCarAlarmDto pubCarAlarmDto);

    /**
     * 批量保存用车报警信息
     * @param pubCarAlarmPos 数据
     */
    @RequestMapping(value = "/savePubCarAlarmBatch", method = RequestMethod.POST)
    void savePubCarAlarmBatch(@RequestBody List<PubCarAlarmPo> pubCarAlarmPos);

}
