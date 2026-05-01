package com.java110.intf.pubcar;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.keyCabinetLog.KeyCabinetLogDto;
import com.java110.vo.api.keyCabinetLog.ApiKeyCabinetLogDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author hh
 * @date 2022/2/10
 */
@FeignClient(name = "pubcar-service", configuration = {FeignConfiguration.class})
@RequestMapping("/keyCabinetLogApi")
public interface IkeyCabinetLogInnerServiceSMO {

    /**
     * 获取钥匙取还日志信息
     * @param keyCabinetList 钥匙id集合
     * @return List
     */
    @RequestMapping(value = "/getKeyCabinetLogByKeyCabinetsBatch", method = RequestMethod.GET)
    List<KeyCabinetLogDto> getKeyCabinetLogByKeyCabinetsBatch(@RequestParam("keyCabinetList") List<String> keyCabinetList);

    /**
     * 根据姓名，部门和对应的钥匙来获取记录时间和类型
     * @param keyCabinetLogDto keyCabinetLogDto
     * @return KeyCabinetLogDto
     */
    @RequestMapping(value = "/getKeyCabinetLogByNameAndDepartmentAndKey", method = RequestMethod.POST)
    KeyCabinetLogDto getKeyCabinetLogByNameAndDepartmentAndKey(@RequestBody KeyCabinetLogDto keyCabinetLogDto);

    /**
     * 查询总数
     * @param keyCabinetLogDto keyCabinetLogDto
     * @return int
     */
    @RequestMapping(value = "/queryKeyCabinetLogsCount", method = RequestMethod.POST)
    int queryKeyCabinetLogsCount(@RequestBody KeyCabinetLogDto keyCabinetLogDto);

    /**
     * 分页查询取还钥匙日志
     * @param keyCabinetLogDto keyCabinetLogDto
     * @return list
     */
    @RequestMapping(value = "/queryKeyCabinetLogs", method = RequestMethod.POST)
    List<ApiKeyCabinetLogDataVo> queryKeyCabinetLogs(@RequestBody KeyCabinetLogDto keyCabinetLogDto);

}
