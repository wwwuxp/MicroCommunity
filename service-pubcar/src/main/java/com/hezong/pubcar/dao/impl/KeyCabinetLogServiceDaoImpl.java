package com.hezong.pubcar.dao.impl;

import com.hezong.pubcar.dao.IKeyCabinetLogServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.keyCabinetLog.KeyCabinetLogDto;
import com.java110.vo.api.keyCabinetLog.ApiKeyCabinetLogDataVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hh
 * @date 2022/2/10
 */
@Service("keyCabinetLogServiceDaoImpl")
public class KeyCabinetLogServiceDaoImpl extends BaseServiceDao implements IKeyCabinetLogServiceDao {

    @Override
    public List<KeyCabinetLogDto> getKeyCabinetLogByKeyCabinetsBatch(List<String> keyCabinetList) {
        return sqlSessionTemplate.selectList("keyCabinetLogServiceDaoImpl.getKeyCabinetLogByKeyCabinetsBatch", keyCabinetList);
    }

    @Override
    public KeyCabinetLogDto getKeyCabinetLogByNameAndDepartmentAndKey(KeyCabinetLogDto keyCabinetLogDto) {
        return sqlSessionTemplate.selectOne("keyCabinetLogServiceDaoImpl.getKeyCabinetLogByNameAndDepartmentAndKey", keyCabinetLogDto);
    }

    @Override
    public int queryKeyCabinetLogsCount(KeyCabinetLogDto keyCabinetLogDto) {
        return sqlSessionTemplate.selectOne("keyCabinetLogServiceDaoImpl.queryKeyCabinetLogsCount", keyCabinetLogDto);
    }

    @Override
    public List<ApiKeyCabinetLogDataVo> queryKeyCabinetLogs(KeyCabinetLogDto keyCabinetLogDto) {
        return sqlSessionTemplate.selectList("keyCabinetLogServiceDaoImpl.queryKeyCabinetLogs", keyCabinetLogDto);
    }

    @Override
    public KeyCabinetLogDto getKeyCabinetLogByApplicantIdAndKeyCabinet(KeyCabinetLogDto keyCabinetLogDto) {
        return sqlSessionTemplate.selectOne("keyCabinetLogServiceDaoImpl.getKeyCabinetLogByApplicantIdAndKeyCabinet", keyCabinetLogDto);
    }
}
