package com.hezong.pubcar.dao;

import com.java110.dto.keyCabinetSn.KeyCabinetSnDto;
import com.java110.vo.api.keyCabinetSn.ApiKeyCabinetSnDataVo;

import java.util.List;

/**
 * @author hh
 * @date 2022/2/18
 */
public interface IKeyCabinetSnServiceDao {

    /**
     * 保存钥匙柜设备信息
     * @param keyCabinetSnDto 请求参数
     * @return int
     */
    int saveKeyCabinetSn(KeyCabinetSnDto keyCabinetSnDto);

    /**
     * 查询钥匙柜总数
     * @param keyCabinetSnDto 请求参数
     * @return int
     */
    int queryKeyCabinetSnsCount(KeyCabinetSnDto keyCabinetSnDto);

    /**
     * 查询钥匙柜列表
     * @param keyCabinetSnDto 请求参数
     * @return List
     */
    List<ApiKeyCabinetSnDataVo> queryKeyCabinetSns(KeyCabinetSnDto keyCabinetSnDto);

    /**
     * 是否存在相同设备sn
     * @param keyCabinetSnDto 请求参数
     * @return Integer
     */
    Integer existSameDeviceSn(KeyCabinetSnDto keyCabinetSnDto);

    /**
     * 更新钥匙柜信息
     * @param keyCabinetSnDto 请求参数
     */
    void updateKeyCabinetSn(KeyCabinetSnDto keyCabinetSnDto);

}
