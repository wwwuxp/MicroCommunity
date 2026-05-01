package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IKeyCabinetSnServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.keyCabinetSn.KeyCabinetSnDto;
import com.java110.intf.pubcar.IKeyCabinetSnInnerServiceSMO;
import com.java110.vo.api.keyCabinetSn.ApiKeyCabinetSnDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author hh
 * @date 2022/2/18
 */
@RestController
public class KeyCabinetSnInnerServiceSMOImpl extends BaseServiceSMO implements IKeyCabinetSnInnerServiceSMO {

    @Autowired
    private IKeyCabinetSnServiceDao keyCabinetSnServiceDaoImpl;

    @Override
    public int saveKeyCabinetSn(@RequestBody KeyCabinetSnDto keyCabinetSnDto) {
        return keyCabinetSnServiceDaoImpl.saveKeyCabinetSn(keyCabinetSnDto);
    }

    @Override
    public int queryKeyCabinetSnsCount(@RequestBody KeyCabinetSnDto keyCabinetSnDto) {
        return keyCabinetSnServiceDaoImpl.queryKeyCabinetSnsCount(keyCabinetSnDto);
    }

    @Override
    public List<ApiKeyCabinetSnDataVo> queryKeyCabinetSns(@RequestBody KeyCabinetSnDto keyCabinetSnDto) {
        int page = keyCabinetSnDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            keyCabinetSnDto.setPage((page - 1) * keyCabinetSnDto.getRow());
        }
        return keyCabinetSnServiceDaoImpl.queryKeyCabinetSns(keyCabinetSnDto);
    }

    @Override
    public boolean existSameDeviceSn(@RequestBody KeyCabinetSnDto keyCabinetSnDto) {
        Integer exist = keyCabinetSnServiceDaoImpl.existSameDeviceSn(keyCabinetSnDto);
        return Objects.nonNull(exist);
    }

    @Override
    public void updateKeyCabinetSn(@RequestBody KeyCabinetSnDto keyCabinetSnDto) {
        keyCabinetSnServiceDaoImpl.updateKeyCabinetSn(keyCabinetSnDto);
    }
}
