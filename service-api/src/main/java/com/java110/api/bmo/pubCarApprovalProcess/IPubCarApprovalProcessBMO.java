package com.java110.api.bmo.pubCarApprovalProcess;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

import java.util.List;

/**
 * @author hh
 * @date 2021/12/6
 */
public interface IPubCarApprovalProcessBMO extends IApiBaseBMO {

    /**
     * 添加公车审核进程
     * @param reqJson 请求json
     * @param context 上下文
     * @param leaderIdList 领导id列表
     * @param isBureau 是否是局领导
     */
    void addPubCarApprovalProcess(JSONObject reqJson, DataFlowContext context, List<String> leaderIdList, boolean isBureau);

    /**
     * 添加修车审核进程
     * @param reqJson 请求json
     * @param context 全局上下文对象
     * @param tripId 主键id
     */
    void addPubCarApprovalProcess(JSONObject reqJson, DataFlowContext context, String tripId);

    /**
     * 更新公车审核进程
     * @param reqJson 请求json
     * @param context 上下文
     */
    void updatePubCarApprovalProcess(JSONObject reqJson, DataFlowContext context);

    /**
     * 删除公车审核进程
     * @param deleteApprovalProcessJson 请求json
     * @param context 上下文
     */
    void deletePubCarApprovalProcess(JSONObject deleteApprovalProcessJson, DataFlowContext context);

    /**
     * 添加外出审批流程信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void addOutGoingApprovalProcess(JSONObject reqJson, DataFlowContext context);

}
