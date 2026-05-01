package com.java110.api.bmo.visitInvitation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @author hh
 * create date 2021/9/16
 */
public interface IVisitInvitationBMO extends IApiBaseBMO {

    /**
     * 保存访客邀约信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void addVisitInvitation(JSONObject reqJson, DataFlowContext context);

    /**
     * 修改访客邀约信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void updateVisitInvitation(JSONObject reqJson, DataFlowContext context);

    /**
     * 删除访客邀约信息
     * @param reqJson 请求json
     * @param context 上下文
     */
    void deleteVisitInvitation(JSONObject reqJson, DataFlowContext context);

}
