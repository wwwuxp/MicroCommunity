package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarReimburse.PubCarReimburseDto;
import com.java110.vo.api.pubCarReimburse.ApiPubCarReimburseDataVo;

import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @date 2021/10/12
 */
public interface IPubCarReimburseServiceDao {

    /**
     * 保存用车报销业务信息
     * @param businessPubCarReimburse 业务数据
     */
    void saveBusinessPubCarReimburseInfo(JSONObject businessPubCarReimburse);

    /**
     * 根据bId和operate获取用车报销业务表数据
     * @param info 参数
     * @return list
     */
    List<Map<String, Object>> getBusinessPubCarReimburseInfo(Map<String, Object> info);

    /**
     * 保存用车报销实例表数据
     * @param info 参数
     */
    void savePubCarReimburseInfo(Map<String, Object> info);

    /**
     * 查询用车报销列表数量
     * @param pubCarReimburseDto 参数
     * @return int
     */
    int listPubCarReimbursesCount(PubCarReimburseDto pubCarReimburseDto);

    /**
     * 查询用车报销列表
     * @param pubCarReimburseDto 参数
     * @return list
     */
    List<ApiPubCarReimburseDataVo> listPubCarReimburses(PubCarReimburseDto pubCarReimburseDto);

    /**
     * 审批用车报销列表
     * @param pubCarReimburseDto 参数
     * @return true or false
     */
    int auditPubCarReimburses(PubCarReimburseDto pubCarReimburseDto);

}
