package com.java110.api.listener.pubCarTrip;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.PubCarTripCarPassenger.IPubCarTripCarPassengerBMO;
import com.java110.api.bmo.pubCarApprovalProcess.IPubCarApprovalProcessBMO;
import com.java110.api.bmo.pubCarTrip.IPubCarTripBMO;
import com.java110.api.bmo.pubCarTripCarNum.IPubCarTripCarNumBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarApproverInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.pubCarApprover.PubCarApproverPo;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodePubCarTripConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;

/**
 * 保存行程信息侦听
 * add by hh
 */
@Java110Listener("savePubCarTripListener")
public class SavePubCarTripListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarTripBMO pubCarTripBMOImpl;
    @Autowired
    private IPubCarTripCarNumBMO pubCarTripCarNumBMOImpl;
    @Autowired
    private IPubCarTripCarPassengerBMO pubCarTripCarPassengerBMOImpl;
    @Autowired
    private IPubCarApprovalProcessBMO pubCarApprovalProcessBMOImpl;
    @Autowired
    private IPubCarApproverInnerServiceSMO pubCarApproverInnerServiceSMOImpl;
    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "userId", "申请人id不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区id不能为空");
        Assert.hasKeyAndValue(reqJson, "isUrgent", "加急不能为空");
        Assert.hasKeyAndValue(reqJson, "isDaily", "是否是日常用车不能为空");

        Assert.hasKey(reqJson, "trip", "请求中没有trip节点");
        JSONArray tripArray = reqJson.getJSONArray("trip");
        if (CollectionUtils.isEmpty(tripArray)) {
            throw new IllegalArgumentException("trip节点下没有任何数据");
        }

        commonTripValidate(tripArray);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        // 添加申请用车基本信息
        pubCarTripBMOImpl.addPubCarTrip(reqJson, context);

        // 添加申请车辆数信息
        pubCarTripCarNumBMOImpl.addPubCarTripCarNum(reqJson, context);

        // 添加乘车人信息
        pubCarTripCarPassengerBMOImpl.addPubCarTripCarPassenger(reqJson, context);

        // 添加审批流程数据信息
        String userId = reqJson.getString("userId");
        List<String> orgNameList = userInnerServiceSMOImpl.getUserOrgName(userId);
        if (CollectionUtils.isEmpty(orgNameList)){
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "未找到您的部门信息");
        }
        List<String> leaderIdList = Collections.emptyList();
        if (orgNameList.contains("局领导")){
            pubCarApprovalProcessBMOImpl.addPubCarApprovalProcess(reqJson, context, leaderIdList, true);
        }else {
            // 找到申请用车人的上级领导(局领导时不能使用)
            leaderIdList = pubCarApproverInnerServiceSMOImpl.getUserLeader(userId, PubCarApproverPo.TYPE_PUBCAR);
            if (CollectionUtils.isEmpty(leaderIdList)){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, "请先联系管理员配置部门领导");
            }
            pubCarApprovalProcessBMOImpl.addPubCarApprovalProcess(reqJson, context, leaderIdList, false);
        }

        // 不管是普通员工，部门领导，还是局领导申请用车，第一步都先发送给用车管理员审核
        // pubCarApprovalProcessBMOImpl.addPubCarApprovalProcess(reqJson, context, null, false);

        // 如果勾选加急，在此处可以发送短信给审核人通知快速审核

    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripConstant.ADD_PUBCARTRIP;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
