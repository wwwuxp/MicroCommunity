package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IKeyCabinetLogServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.keyCabinetLog.KeyCabinetLogDto;
import com.java110.intf.pubcar.IkeyCabinetLogInnerServiceSMO;
import com.java110.vo.api.keyCabinetLog.ApiKeyCabinetLogDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author hh
 * @date 2022/2/10
 */
@RestController
public class KeyCabinetLogInnerServiceSMOImpl extends BaseServiceSMO implements IkeyCabinetLogInnerServiceSMO {

    @Autowired
    private IKeyCabinetLogServiceDao keyCabinetLogServiceDaoImpl;

    @Override
    public List<KeyCabinetLogDto> getKeyCabinetLogByKeyCabinetsBatch(@RequestParam("keyCabinetList") List<String> keyCabinetList) {
        if (CollectionUtils.isEmpty(keyCabinetList)){
            return Collections.emptyList();
        }
        return keyCabinetLogServiceDaoImpl.getKeyCabinetLogByKeyCabinetsBatch(keyCabinetList);
    }

    @Override
    public KeyCabinetLogDto getKeyCabinetLogByNameAndDepartmentAndKey(@RequestBody KeyCabinetLogDto keyCabinetLogDto) {
        return keyCabinetLogServiceDaoImpl.getKeyCabinetLogByNameAndDepartmentAndKey(keyCabinetLogDto);
    }

    @Override
    public int queryKeyCabinetLogsCount(@RequestBody KeyCabinetLogDto keyCabinetLogDto) {
        return keyCabinetLogServiceDaoImpl.queryKeyCabinetLogsCount(keyCabinetLogDto);
    }

    @Override
    public List<ApiKeyCabinetLogDataVo> queryKeyCabinetLogs(@RequestBody KeyCabinetLogDto keyCabinetLogDto) {
        int page = keyCabinetLogDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            keyCabinetLogDto.setPage((page - 1) * keyCabinetLogDto.getRow());
        }
        return keyCabinetLogServiceDaoImpl.queryKeyCabinetLogs(keyCabinetLogDto);
    }
}
