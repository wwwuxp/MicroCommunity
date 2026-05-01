package com.hezong.pubcar.dao;

import com.java110.dto.keyCabinetLog.KeyCabinetLogDto;
import com.java110.vo.api.keyCabinetLog.ApiKeyCabinetLogDataVo;

import java.util.List;

/**
 * @author hh
 * @date 2022/2/10
 */
public interface IKeyCabinetLogServiceDao {

    /**
     * 获取钥匙取还日志信息
     * @param keyCabinetList 钥匙id集合
     * @return List
     */
    List<KeyCabinetLogDto> getKeyCabinetLogByKeyCabinetsBatch(List<String> keyCabinetList);

    /**
     * 根据姓名，部门和对应的钥匙来获取记录时间和类型
     * @param keyCabinetLogDto keyCabinetLogDto
     * @return keyCabinetLogDto
     */
    KeyCabinetLogDto getKeyCabinetLogByNameAndDepartmentAndKey(KeyCabinetLogDto keyCabinetLogDto);

    /**
     * 查询总数
     * @param keyCabinetLogDto keyCabinetLogDto
     * @return int
     */
    int queryKeyCabinetLogsCount(KeyCabinetLogDto keyCabinetLogDto);

    /**
     * 查询取还钥匙记录
     * @param keyCabinetLogDto keyCabinetLogDto
     * @return List
     */
    List<ApiKeyCabinetLogDataVo> queryKeyCabinetLogs(KeyCabinetLogDto keyCabinetLogDto);

    /**
     * 根据用户id和对应的钥匙来获取信息
     * @param keyCabinetLogDto keyCabinetLogDto
     * @return keyCabinetLogDto
     */
    KeyCabinetLogDto getKeyCabinetLogByApplicantIdAndKeyCabinet(KeyCabinetLogDto keyCabinetLogDto);

}
