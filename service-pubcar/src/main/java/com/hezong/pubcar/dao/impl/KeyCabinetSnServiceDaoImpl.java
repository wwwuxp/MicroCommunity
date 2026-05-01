package com.hezong.pubcar.dao.impl;

import com.hezong.pubcar.dao.IKeyCabinetSnServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.keyCabinetSn.KeyCabinetSnDto;
import com.java110.vo.api.keyCabinetSn.ApiKeyCabinetSnDataVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hh
 * @date 2022/2/18
 */
@Service("keyCabinetSnServiceDaoImpl")
public class KeyCabinetSnServiceDaoImpl extends BaseServiceDao implements IKeyCabinetSnServiceDao {

    @Override
    public int saveKeyCabinetSn(KeyCabinetSnDto keyCabinetSnDto) {
        return sqlSessionTemplate.insert("keyCabinetSnServiceDaoImpl.saveKeyCabinetSn", keyCabinetSnDto);
    }

    @Override
    public int queryKeyCabinetSnsCount(KeyCabinetSnDto keyCabinetSnDto) {
        return sqlSessionTemplate.selectOne("keyCabinetSnServiceDaoImpl.queryKeyCabinetSnsCount", keyCabinetSnDto);
    }

    @Override
    public List<ApiKeyCabinetSnDataVo> queryKeyCabinetSns(KeyCabinetSnDto keyCabinetSnDto) {
        return sqlSessionTemplate.selectList("keyCabinetSnServiceDaoImpl.queryKeyCabinetSns", keyCabinetSnDto);
    }

    @Override
    public Integer existSameDeviceSn(KeyCabinetSnDto keyCabinetSnDto) {
        return sqlSessionTemplate.selectOne("keyCabinetSnServiceDaoImpl.existSameDeviceSn", keyCabinetSnDto);
    }

    @Override
    public void updateKeyCabinetSn(KeyCabinetSnDto keyCabinetSnDto) {
        sqlSessionTemplate.update("keyCabinetSnServiceDaoImpl.updateKeyCabinetSn", keyCabinetSnDto);
    }
}
