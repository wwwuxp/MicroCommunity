package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarAccident.PubCarAccidentDto;
import com.java110.vo.api.pubCarAccident.ApiPubCarAccidentDataVo;

import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @date 2021/10/9
 */
public interface IPubCarAccidentServiceDao {

    /**
     * 保存用车事故业务表信息
     * @param businessPubCarAccident 数据
     */
    void saveBusinessPubCarAccidentInfo(JSONObject businessPubCarAccident);

    /**
     * 根据bId和operate获取用车事故业务表数据
     * @param info 数据
     * @return List
     */
    List<Map<String, Object>> getBusinessPubCarAccidentInfo(Map<String, Object> info);

    /**
     * 保存用车事故实例表信息
     * @param info info
     */
    void savePubCarAccidentInfo(Map<String, Object> info);

    /**
     * 查询用车事故列表数量
     * @param pubCarAccidentDto 参数
     * @return int
     */
    int listPubCarAccidentsCount(PubCarAccidentDto pubCarAccidentDto);

    /**
     * 查询用车事故列表
     * @param pubCarAccidentDto 参数
     * @return list
     */
    List<ApiPubCarAccidentDataVo> listPubCarAccidents(PubCarAccidentDto pubCarAccidentDto);

}
