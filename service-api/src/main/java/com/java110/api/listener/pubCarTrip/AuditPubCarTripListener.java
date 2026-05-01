package com.java110.api.listener.pubCarTrip;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarApprovalProcess.IPubCarApprovalProcessBMO;
import com.java110.api.bmo.pubCarTrip.IPubCarTripBMO;
import com.java110.api.bmo.pubCarTripCarNum.IPubCarTripCarNumBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarTripCarNum.PubCarTripCarNumDto;
import com.java110.intf.pubcar.IPubCarApprovalProcessInnerServiceSMO;
import com.java110.intf.pubcar.IPubCarTripCarNumInnerServiceSMO;
import com.java110.po.pubCarApprovalProcess.PubCarApprovalProcessPo;
import com.java110.utils.constant.ServiceCodePubCarTripConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 审核行程信息侦听
 * add by hh
 */
@Java110Listener("auditPubCarTripListener")
public class AuditPubCarTripListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarTripBMO pubCarTripBMOImpl;
    @Autowired
    private IPubCarApprovalProcessBMO pubCarApprovalProcessBMOImpl;
    @Autowired
    private IPubCarTripCarNumBMO pubCarTripCarNumBMOImpl;
    @Autowired
    private IPubCarTripCarNumInnerServiceSMO pubCarTripCarNumInnerServiceSMOImpl;
    @Autowired
    private IPubCarApprovalProcessInnerServiceSMO pubCarApprovalProcessInnerServiceSMOImpl;
    // @Autowired
    // private IUserInnerServiceSMO userInnerServiceSMOImpl;
    // @Autowired
    // private IPubCarApproverInnerServiceSMO pubCarApproverInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "tripId", "行程id不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区id不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "审核状态不能为空");
        Assert.hasKeyAndValue(reqJson, "reviewerRole", "审核人角色不能为空");
        if (Objects.equals(reqJson.getInteger("reviewerRole"), 2)){
            // Assert.hasKeyAndValue(reqJson, "isDepartLeaderApprove", "是否科室领导审批不能为空");
            Assert.hasKeyAndValue(reqJson, "isBureauLeaderApprove", "是否局领导审批不能为空");
        }

        // 审核不通过时请求中可以不用传递车辆信息（info节点）
        Integer state = reqJson.getInteger("state");
        Integer reviewerRole = reqJson.getInteger("reviewerRole");
        if (StatusConstant.STATUS_NOT_STARTED.equals(state) ||
                PubCarApprovalProcessPo.DEPARTMENT_LEADER == reviewerRole ||
                PubCarApprovalProcessPo.PUBCAR_BUREAU_LEADER == reviewerRole) {
            return;
        }

        Assert.hasKey(reqJson, "info", "当前为审核通过状态，但请求中没有包含info节点");
        JSONArray infoArray = reqJson.getJSONArray("info");
        if (CollectionUtils.isEmpty(infoArray)) {
            throw new IllegalArgumentException("当前为审核通过状态，但info节点中未包含任何信息");
        }

        // 获取行程状态和车辆编号
        List<PubCarTripCarNumDto> pubCarTripCarNumDtos = pubCarTripCarNumInnerServiceSMOImpl.getTripTypeAndNoByTripId(reqJson.getString("tripId"));

        for (int i = 0; i < infoArray.size(); i++) {
            JSONObject oneInfoJson = infoArray.getJSONObject(i);
            Assert.hasKeyAndValue(oneInfoJson, "carId", "存在未选择车辆");
            Assert.hasKeyAndValue(oneInfoJson, "no", "未传入车辆编号no");
            // 如果是自驾，不需要指定司机
            Integer no = oneInfoJson.getInteger("no");
            // 根据车辆编号找车辆行程类型
            List<Integer> tripTypes = pubCarTripCarNumDtos.stream()
                    .filter(t -> Objects.equals(no, t.getNo()))
                    .map(PubCarTripCarNumDto::getTripType)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tripTypes) && tripTypes.size() == 1){
                Integer tripType = tripTypes.get(0);
                if (Objects.equals(PubCarTripCarNumDto.SELF_DRIVING, tripType)){
                    oneInfoJson.put("driverId", pubCarTripCarNumDtos.get(0).getUserId());
                    continue;
                }
            }
            Assert.hasKeyAndValue(oneInfoJson, "driverId", "存在未选择司机");
        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        Integer reviewerRole = reqJson.getInteger("reviewerRole");
        Integer state = reqJson.getInteger("state");
        // 是部门领导审核
        switch (reviewerRole){
            case PubCarApprovalProcessPo.DEPARTMENT_LEADER:
                if (StatusConstant.STATUS_NOT_STARTED.equals(state)){
                    Integer tripStatus = reqJson.getInteger("tripStatus");
                    if (Objects.equals(tripStatus, StatusConstant.STATUS_FINISHED)){
                        throw new ListenerExecuteException(HttpStatus.BAD_REQUEST.value(), "该行程已经结束了哦，部门领导无法审批了");
                    }
                    approveRefuse(context, reqJson);
                }else {
                    approveAgree(context, reqJson, StatusConstant.AUDIT_RUNNING);

                    // 审批流程中新增数据，该由用车管理员进行审批
                    pubCarApprovalProcessBMOImpl.addPubCarApprovalProcess(reqJson, context, null, false);
                }
                break;
            case PubCarApprovalProcessPo.PUBCAR_ADMINISTRATOR:
                // 是用车管理员审核，首先判断是否是修改审批,根据行程id,看是否查询到数据来进行判断
                String tripId = reqJson.getString("tripId");
                Integer tripStatus = reqJson.getInteger("tripStatus");
                List<Integer> reviewerRoleList = pubCarApprovalProcessInnerServiceSMOImpl.getPubCarApprovalProcess(tripId);
                Set<Integer> reviewerRoleSet = new HashSet<>(reviewerRoleList);

                if (StatusConstant.STATUS_NOT_STARTED.equals(state)){
                    if (Objects.equals(tripStatus, StatusConstant.STATUS_RUNNING)){
                        throw new ListenerExecuteException(HttpStatus.BAD_REQUEST.value(), "该行程已经在进行中了哦，不能审批不通过");
                    }else if (Objects.equals(tripStatus, StatusConstant.STATUS_FINISHED)){
                        throw new ListenerExecuteException(HttpStatus.BAD_REQUEST.value(), "该行程已经结束了哦，不能审批不通过");
                    }
                    // 审核不通过，更新行程状态为不通过
                    if (CollectionUtils.isNotEmpty(reviewerRoleSet)){
                        // 修改审批,需要把局领导的流程删除
                        deleteProcess(context, reqJson, tripId, reviewerRoleSet);
                    }
                    approveRefuse(context, reqJson);
                }else {
                    // 审核通过，如果不需要局领导审批，更新行程状态为完成，如果需要局领导审批，更新行程状态为审核中
                    Integer isBureauLeaderApprove = reqJson.getInteger("isBureauLeaderApprove");
                    if (Objects.equals(isBureauLeaderApprove, 1)){
                        // 需要局领导审批
                        if (Objects.equals(tripStatus, StatusConstant.STATUS_RUNNING)){
                            throw new ListenerExecuteException(HttpStatus.BAD_REQUEST.value(), "该行程已经在进行中了哦，不能再由局领导审批了");
                        }else if (Objects.equals(tripStatus, StatusConstant.STATUS_FINISHED)){
                            throw new ListenerExecuteException(HttpStatus.BAD_REQUEST.value(), "该行程已经结束了哦，不能再由局领导审批了");
                        }
                        approveAgree(context, reqJson, StatusConstant.AUDIT_RUNNING);

                        // 审批流程中新增数据，该由局领导进行审批
                        if (CollectionUtils.isEmpty(reviewerRoleSet) || !reviewerRoleSet.contains(PubCarApprovalProcessPo.PUBCAR_BUREAU_LEADER)) {
                            pubCarApprovalProcessBMOImpl.addPubCarApprovalProcess(reqJson, context, null, true);
                        }
                    }else {
                        String communityId = reqJson.getString("communityId");
                        // 不需要局领导审批
                        JSONObject tripApprovalStatusJson = new JSONObject();
                        tripApprovalStatusJson.put("state", StatusConstant.STATUS_FINISHED);
                        tripApprovalStatusJson.put("remark", reqJson.getString("remark"));
                        tripApprovalStatusJson.put("tripId", tripId);
                        tripApprovalStatusJson.put("communityId", communityId);
                        tripApprovalStatusJson.put("isBureauLeaderApprove", reqJson.getInteger("isBureauLeaderApprove"));
                        pubCarTripBMOImpl.updatePubCarTrip(tripApprovalStatusJson, context);

                        // 如果是修改审批，之前是局领导审批，现在不需要局领导审批了
                        if (CollectionUtils.isNotEmpty(reviewerRoleSet) && reviewerRoleSet.contains(PubCarApprovalProcessPo.PUBCAR_BUREAU_LEADER)){
                            // 为true表明之前是由局领导审批了，现在要修改为不由局领导审批
                            JSONObject deleteApprovalProcessJson = new JSONObject();
                            deleteApprovalProcessJson.put("communityId", communityId);
                            deleteApprovalProcessJson.put("tripId", tripId);
                            deleteApprovalProcessJson.put("reviewerRole", PubCarApprovalProcessPo.PUBCAR_BUREAU_LEADER);
                            pubCarApprovalProcessBMOImpl.deletePubCarApprovalProcess(deleteApprovalProcessJson, context);
                        }

                        // 更新行程审批流程表基本信息
                        updateApprovalProcess(context, reqJson);
                    }
                    // 更新数据到pubcar_trip_car_num表
                    pubCarTripCarNumBMOImpl.updatePubCarTripCarNum(reqJson, context);
                }
                break;
            case PubCarApprovalProcessPo.PUBCAR_BUREAU_LEADER:
                // 是局领导审批
                if (StatusConstant.STATUS_NOT_STARTED.equals(state)){
                    // 审核不通过，更新行程状态为不通过
                    approveRefuse(context, reqJson);
                }else {
                    // 审核通过，更新行程状态为通过
                    approveAgree(context, reqJson, StatusConstant.STATUS_FINISHED);
                }
                break;
            default:
        }
    }

    /**
     * 删除流程
     * @param context 上下文
     * @param reqJson 请求json
     * @param tripId 行程id
     * @param reviewerRoleSet 审核角色列表
     */
    private void deleteProcess(DataFlowContext context, JSONObject reqJson, String tripId, Set<Integer> reviewerRoleSet) {
        for (Integer role : reviewerRoleSet) {
            if (!Objects.equals(PubCarApprovalProcessPo.PUBCAR_BUREAU_LEADER, role)){
                continue;
            }
            JSONObject deleteApprovalProcessJson = new JSONObject();
            deleteApprovalProcessJson.put("communityId", reqJson.getString("communityId"));
            deleteApprovalProcessJson.put("tripId", tripId);
            deleteApprovalProcessJson.put("reviewerRole", role);
            pubCarApprovalProcessBMOImpl.deletePubCarApprovalProcess(deleteApprovalProcessJson, context);
        }
    }

    /**
     * 审批通过
     * @param context 上下文
     * @param reqJson 请求json
     */
    private void approveAgree(DataFlowContext context, JSONObject reqJson, Integer state) {
        // 审核通过，更新行程状态为审核中
        JSONObject tripApprovalStatusJson = new JSONObject();
        tripApprovalStatusJson.put("state", state);
        tripApprovalStatusJson.put("remark", reqJson.getString("remark"));
        Integer isBureauLeaderApprove = reqJson.getInteger("isBureauLeaderApprove");
        if (Objects.nonNull(isBureauLeaderApprove)){
            tripApprovalStatusJson.put("isBureauLeaderApprove", isBureauLeaderApprove);
        }
        tripApprovalStatusJson.put("tripId", reqJson.getString("tripId"));
        tripApprovalStatusJson.put("communityId", reqJson.getString("communityId"));
        pubCarTripBMOImpl.updatePubCarTrip(tripApprovalStatusJson, context);
        // 更新行程审批流程表基本信息
        updateApprovalProcess(context, reqJson);
    }

    /**
     * 更新审批流程信息
     * @param context 上下文
     * @param reqJson 请求json
     */
    private void updateApprovalProcess(DataFlowContext context, JSONObject reqJson) {
        JSONObject approvalProcessStatusJson = new JSONObject();
        approvalProcessStatusJson.put("approvalStatus", StatusConstant.STATUS_FINISHED);
        approvalProcessStatusJson.put("tripId", reqJson.getString("tripId"));
        approvalProcessStatusJson.put("reviewerRole", reqJson.getString("reviewerRole"));
        approvalProcessStatusJson.put("communityId", reqJson.getString("communityId"));
        pubCarApprovalProcessBMOImpl.updatePubCarApprovalProcess(approvalProcessStatusJson, context);
    }

    /**
     * 审批不同意
     * @param context 上下文
     * @param reqJson 请求json
     */
    private void approveRefuse(DataFlowContext context, JSONObject reqJson) {
        // 审核不通过，更新行程状态为不通过
        pubCarTripBMOImpl.updatePubCarTrip(reqJson, context);
        // 更新行程审批流程表状态
        JSONObject approvalProcessStatusJson = new JSONObject();
        approvalProcessStatusJson.put("approvalStatus", StatusConstant.STATUS_NOT_STARTED);
        approvalProcessStatusJson.put("tripId", reqJson.getString("tripId"));
        approvalProcessStatusJson.put("reviewerRole", reqJson.getString("reviewerRole"));
        approvalProcessStatusJson.put("communityId", reqJson.getString("communityId"));
        pubCarApprovalProcessBMOImpl.updatePubCarApprovalProcess(approvalProcessStatusJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripConstant.AUDIT_PUBCARTRIP;
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
