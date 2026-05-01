package com.java110.api.bmo.pubCarApprovalProcess.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCarApprovalProcess.IPubCarApprovalProcessBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.pubCarApprover.PubCarApproverDto;
import com.java110.intf.pubcar.IPubCarApproverInnerServiceSMO;
import com.java110.po.pubCarApprovalProcess.PubCarApprovalProcessPo;
import com.java110.po.pubCarApprover.PubCarApproverPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author hh
 * @date 2021/12/6
 */
@Service("pubCarApprovalProcessBMOImpl")
public class PubCarApprovalProcessBMOImpl extends ApiBaseBMO implements IPubCarApprovalProcessBMO {

    @Autowired
    private IPubCarApproverInnerServiceSMO pubCarApproverInnerServiceSMOImpl;

    @Override
    public void addPubCarApprovalProcess(JSONObject reqJson, DataFlowContext context, String tripId){
        List<String> administrators = pubCarApproverInnerServiceSMOImpl.getPubCarApprovers(PubCarApproverPo.VEHICLE_REPAIR, PubCarApproverPo.TYPE_PUBCAR);
        if (CollectionUtils.isEmpty(administrators)){
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, "请先联系管理员配置用车管理人");
        }
        JSONArray businessPubCarApprovalProcessArray = new JSONArray();
        putBusinessPubCarApprovalProcessObject(reqJson, administrators, businessPubCarApprovalProcessArray, tripId, PubCarApprovalProcessPo.TYPE_PUBCAR);
        handleBusinessJsonArrayData(context,businessPubCarApprovalProcessArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_APPROVAL_PROCESS, PubCarApprovalProcessPo.class);
    }

    @Override
    public void addPubCarApprovalProcess(JSONObject reqJson, DataFlowContext context, List<String> leaderIdList, boolean isBureau) {
        JSONArray businessPubCarApprovalProcessArray = new JSONArray();
        // leaderIdList不为空表明数据从申请用车进来
        if (Objects.nonNull(leaderIdList)) {
            // 自己就是领导，直接发送给用车管理人进行审批
            List<String> administrators = null;
            if (leaderIdList.contains(reqJson.getString("userId")) || isBureau) {
                administrators = pubCarApproverInnerServiceSMOImpl.getPubCarApprovers(PubCarApproverPo.PUBCAR_ADMINISTRATOR, PubCarApproverPo.TYPE_PUBCAR);
                if (CollectionUtils.isEmpty(administrators)){
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, "请先联系管理员配置用车管理人");
                }
            }

            JSONArray tripArray = reqJson.getJSONArray("trip");
            for (int i = 0; i < tripArray.size(); i++) {
                JSONObject oneBusinessTrip = tripArray.getJSONObject(i);
                String tripId = oneBusinessTrip.getString("tripId");

                // 公车管理人列表不为空，说明用车申请人是领导
                if (CollectionUtils.isNotEmpty(administrators)) {
                    putBusinessPubCarApprovalProcessObject(reqJson, administrators, businessPubCarApprovalProcessArray, tripId, PubCarApprovalProcessPo.PUBCAR_ADMINISTRATOR);
                } else {
                    putBusinessPubCarApprovalProcessObject(reqJson, leaderIdList, businessPubCarApprovalProcessArray, tripId, PubCarApprovalProcessPo.DEPARTMENT_LEADER);
                }
            }
        }else {
            // leaderIdList为空表明数据是从用车审批进来
            if (isBureau){
                // 由局领导进来审批
                List<String> bureauLeaders = pubCarApproverInnerServiceSMOImpl.getPubCarApprovers(PubCarApproverPo.PUBCAR_BUREAU_LEADER, PubCarApproverPo.TYPE_PUBCAR);
                if (CollectionUtils.isEmpty(bureauLeaders)){
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, "请先联系管理员配置局领导信息");
                }
                putBusinessPubCarApprovalProcessObject(reqJson, bureauLeaders, businessPubCarApprovalProcessArray, reqJson.getString("tripId"), PubCarApprovalProcessPo.PUBCAR_BUREAU_LEADER);
            }else {
                // 由用车管理员进来审批
                List<String> administrators = pubCarApproverInnerServiceSMOImpl.getPubCarApprovers(PubCarApproverPo.PUBCAR_ADMINISTRATOR, PubCarApproverPo.TYPE_PUBCAR);
                if (CollectionUtils.isEmpty(administrators)){
                    throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, "请先联系管理员配置用车管理人");
                }
                putBusinessPubCarApprovalProcessObject(reqJson, administrators, businessPubCarApprovalProcessArray, reqJson.getString("tripId"), PubCarApprovalProcessPo.PUBCAR_ADMINISTRATOR);
            }
        }

        handleBusinessJsonArrayData(context, businessPubCarApprovalProcessArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_APPROVAL_PROCESS, PubCarApprovalProcessPo.class);
    }

    @Override
    public void updatePubCarApprovalProcess(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessJsonObject = new JSONObject();
        businessJsonObject.put("approvalStatus", reqJson.getInteger("approvalStatus"));
        // businessJsonObject.put("userId", context.getUserId());
        businessJsonObject.put("tripId", reqJson.getString("tripId"));
        businessJsonObject.put("reviewerRole", reqJson.getInteger("reviewerRole"));
        businessJsonObject.put("communityId", reqJson.getString("communityId"));
        handleBusinessJsonObjectData(context, businessJsonObject, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_APPROVAL_PROCESS, PubCarApprovalProcessPo.class);
    }

    @Override
    public void deletePubCarApprovalProcess(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessJsonObject = new JSONObject();
        businessJsonObject.put("tripId", reqJson.getString("tripId"));
        businessJsonObject.put("reviewerRole", reqJson.getInteger("reviewerRole"));
        businessJsonObject.put("communityId", reqJson.getString("communityId"));
        handleBusinessJsonObjectData(context, businessJsonObject, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_APPROVAL_PROCESS, PubCarApprovalProcessPo.class);
    }

    @Override
    public void addOutGoingApprovalProcess(JSONObject reqJson, DataFlowContext context) {
        JSONArray businessPubCarApprovalProcessArray = new JSONArray();
        String userId = context.getUserId();
        PubCarApproverDto pubCarApproverDto = pubCarApproverInnerServiceSMOImpl.getRoleTypeAndDepartmentIdByApproverId(userId, PubCarApproverPo.TYPE_OUTGOING);

        // 默认外出审批人设置已经全部配置好
        if (Objects.isNull(pubCarApproverDto)){
            // 是普通员工
            List<String> departmentLeaderIdList = pubCarApproverInnerServiceSMOImpl.getUserLeader(userId, PubCarApproverPo.TYPE_OUTGOING);
            if (CollectionUtils.isEmpty(departmentLeaderIdList)){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, "请先联系管理员配置各部门领导");
            }
            putBusinessOutGoingApprovalProcessObject(reqJson, businessPubCarApprovalProcessArray, departmentLeaderIdList, PubCarApprovalProcessPo.DEPARTMENT_LEADER);

        }else {
            Integer roleType = pubCarApproverDto.getRoleType();
            String departmentId = pubCarApproverDto.getDepartmentId();
            // "部门领导" | "副局长" | "局长"
            if (PubCarApprovalProcessPo.DEPARTMENT_LEADER == roleType){
                // 是部门领导申请,departmentId只会有一个,判断是哪个副局长管理这个部门
                List<String> bureauLeaders = pubCarApproverInnerServiceSMOImpl.whichBureauLeaderManage(departmentId);
                putBusinessOutGoingApprovalProcessObject(reqJson, businessPubCarApprovalProcessArray, bureauLeaders, PubCarApprovalProcessPo.OUTGOING_BUREAU_LEADER);

            }else if (PubCarApprovalProcessPo.OUTGOING_BUREAU_LEADER == roleType){
                // 是局领导申请,找到局负责人
                List<String> bureauHeaders = pubCarApproverInnerServiceSMOImpl.getPubCarApprovers(PubCarApproverPo.OUTGOING_BUREAU_HEADER, PubCarApproverPo.TYPE_OUTGOING);
                putBusinessOutGoingApprovalProcessObject(reqJson, businessPubCarApprovalProcessArray, bureauHeaders, PubCarApprovalProcessPo.OUTGOING_BUREAU_HEADER);
            }
        }
        handleBusinessJsonArrayData(context, businessPubCarApprovalProcessArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_APPROVAL_PROCESS, PubCarApprovalProcessPo.class);
    }

    /**
     * 外出类型添加至Business中
     * @param reqJson 请求json
     * @param businessPubCarApprovalProcessArray 业务数组
     * @param reviewerIdList 审核人id列表
     * @param reviewerRole 审核角色类型
     */
    private void putBusinessOutGoingApprovalProcessObject(JSONObject reqJson, JSONArray businessPubCarApprovalProcessArray, List<String> reviewerIdList, Integer reviewerRole) {
        for (String reviewerId : reviewerIdList) {
            JSONObject businessPubCarApprovalProcessObject = new JSONObject();
            businessPubCarApprovalProcessObject.put("approvalProcessId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_APPROVAL_PROCESS_ID));
            businessPubCarApprovalProcessObject.put("tableType", PubCarApprovalProcessPo.TYPE_OUTGOING);
            businessPubCarApprovalProcessObject.put("tripId", reqJson.getString("outId"));
            businessPubCarApprovalProcessObject.put("userId", reviewerId);
            businessPubCarApprovalProcessObject.put("reviewerRole", reviewerRole);
            businessPubCarApprovalProcessObject.put("sequence", 1);
            businessPubCarApprovalProcessObject.put("approvalStatus", 2);

            businessPubCarApprovalProcessObject.put("communityId", reqJson.getString("communityId"));
            businessPubCarApprovalProcessArray.add(businessPubCarApprovalProcessObject);
        }
    }

    /**
     * 公车类型添加至Business中
     * @param reqJson 请求json
     * @param reviewerIdList 审核人id列表
     * @param businessPubCarApprovalProcessArray 业务数组
     * @param tripId 行程id
     */
    private void putBusinessPubCarApprovalProcessObject(JSONObject reqJson, List<String> reviewerIdList, JSONArray businessPubCarApprovalProcessArray,
                                                        String tripId, int flag) {
        for (String reviewerId : reviewerIdList) {
            JSONObject businessPubCarApprovalProcessObject = new JSONObject();
            businessPubCarApprovalProcessObject.put("approvalProcessId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_APPROVAL_PROCESS_ID));
            businessPubCarApprovalProcessObject.put("tableType", PubCarApprovalProcessPo.TYPE_PUBCAR);
            businessPubCarApprovalProcessObject.put("tripId", tripId);
            businessPubCarApprovalProcessObject.put("userId", reviewerId);
            if (flag == PubCarApprovalProcessPo.PUBCAR_BUREAU_LEADER){
                businessPubCarApprovalProcessObject.put("reviewerRole", PubCarApprovalProcessPo.PUBCAR_BUREAU_LEADER);
                businessPubCarApprovalProcessObject.put("sequence", 3);
            }else if (flag == PubCarApprovalProcessPo.PUBCAR_ADMINISTRATOR){
                businessPubCarApprovalProcessObject.put("reviewerRole", PubCarApprovalProcessPo.PUBCAR_ADMINISTRATOR);
                businessPubCarApprovalProcessObject.put("sequence", 2);
            }else if (flag == PubCarApprovalProcessPo.DEPARTMENT_LEADER){
                businessPubCarApprovalProcessObject.put("reviewerRole", PubCarApprovalProcessPo.DEPARTMENT_LEADER);
                businessPubCarApprovalProcessObject.put("sequence", 1);
            }else if (flag == PubCarApprovalProcessPo.TYPE_PUBCAR){
                //这里是修车申请，只要领导审批就好了
                businessPubCarApprovalProcessObject.put("reviewerRole", PubCarApprovalProcessPo.VEHICLE_REPAIR);
                businessPubCarApprovalProcessObject.put("sequence", 1);
            }
            businessPubCarApprovalProcessObject.put("approvalStatus", 2);

            businessPubCarApprovalProcessObject.put("communityId", reqJson.getString("communityId"));
            businessPubCarApprovalProcessArray.add(businessPubCarApprovalProcessObject);
        }
    }

}
