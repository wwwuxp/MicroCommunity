package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarApprovalProcess.PubCarApprovalProcessDto;

import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @date 2021/12/6
 */
public interface IPubCarApprovalProcessServiceDao {

    /**
     * 保存审批流程业务信息
     * @param businessPubCarApprovalProcess 业务数据
     */
    void saveBusinessPubCarApprovalProcessInfo(JSONObject businessPubCarApprovalProcess);

    /**
     * 获取用车审批流程业务信息
     * @param info 信息
     * @return List
     */
    List<Map> getBusinessPubCarApprovalProcessInfo(Map info);

    /**
     * 保存用车审批流程实例表信息
     * @param info 参数
     */
    void savePubCarApprovalProcessInfo(Map info);

    /**
     * 获取用车审批流程实例信息
     * @param info 参数
     * @return list
     */
    List<Map> getPubCarApprovalProcessInstanceInfo(Map<String, String> info);

    /**
     * 更新用车审批流程表信息
     * @param paramIn 参数
     */
    void updatePubCarApprovalProcessInfo(Map<String, String> paramIn);

    /**
     * 获取行程id
     * @param pubCarApprovalProcessDto 请求参数
     * @return List
     */
    List<PubCarApprovalProcessDto> getTrips(PubCarApprovalProcessDto pubCarApprovalProcessDto);

    /**
     * 获取行程id数量
     * @param pubCarApprovalProcessDto 请求参数
     * @return int
     */
    int getTripIdsCount(PubCarApprovalProcessDto pubCarApprovalProcessDto);

    /**
     * 获取未审核的数量
     * @param pubCarApprovalProcessDto 请求参数
     * @return int
     */
    int getNotApprovalCount(PubCarApprovalProcessDto pubCarApprovalProcessDto);

    /**
     * 局领导是否能显示这条数据
     * @param map 请求参数
     * @return Integer
     */
    Integer hasBureauLeaderApprove(Map<String, Object> map);

    /**
     * 获取当前用户需要审核的外出Id的数量
     * @param pubCarApprovalProcessDto 请求参数
     * @return int
     */
    int getOutGoingIdsCount(PubCarApprovalProcessDto pubCarApprovalProcessDto);

    /**
     * 获取当前用户需要审核的外出Id
     * @param pubCarApprovalProcessDto 请求参数
     * @return List
     */
    List<String> getOutGoingIds(PubCarApprovalProcessDto pubCarApprovalProcessDto);

    /**
     * 根据行程id列表查找对应的审核进程
     * @param tripIdList 行程id列表
     * @return list
     */
    List<PubCarApprovalProcessDto> getTripApprovalProcess(List<String> tripIdList);

    /**
     * 根据行程id获取行程审批流程信息
     * @param map 行程id
     * @return List
     */
    List<Integer> getPubCarApprovalProcess(Map<String, String> map);

}
