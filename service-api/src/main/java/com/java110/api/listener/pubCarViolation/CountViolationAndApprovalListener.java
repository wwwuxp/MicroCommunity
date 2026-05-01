package com.java110.api.listener.pubCarViolation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCar.repaorForm.PubcarRepairFormWhereDto;
import com.java110.dto.pubCarApprovalProcess.PubCarApprovalProcessDto;
import com.java110.dto.pubCarViolation.PubCarViolationDto;
import com.java110.intf.pubcar.IPubCarApprovalProcessInnerServiceSMO;
import com.java110.intf.pubcar.IPubCarViolationInnerServiceSMO;
import com.java110.intf.pubcar.PubcarRepairFormSMO;
import com.java110.utils.constant.ServiceCodePubCarViolationConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 移动端首页显示用车审批和违章处理的红色小数字
 * @author hh
 * @date 2021/11/24
 */
@Java110Listener("countViolationAndApprovalListener")
public class CountViolationAndApprovalListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarApprovalProcessInnerServiceSMO pubCarApprovalProcessInnerServiceSMOImpl;

    @Autowired
    private IPubCarViolationInnerServiceSMO pubCarViolationInnerServiceSMOImpl;

    @Autowired
    private PubcarRepairFormSMO pubcarRepairFormSMO;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        String communityId = reqJson.getString("communityId");
        PubCarApprovalProcessDto pubCarApprovalProcessDto = new PubCarApprovalProcessDto();
        pubCarApprovalProcessDto.setCurrentUserId(context.getUserId());
        pubCarApprovalProcessDto.setApprovalStatus(2);
        pubCarApprovalProcessDto.setCommunityId(communityId);
        int approvalCount = pubCarApprovalProcessInnerServiceSMOImpl.getNotApprovalCount(pubCarApprovalProcessDto);

        PubCarViolationDto pubCarViolationDto = new PubCarViolationDto();
        pubCarViolationDto.setHandleStatus(1);
        pubCarViolationDto.setCommunityId(communityId);
        int violationsCount = pubCarViolationInnerServiceSMOImpl.queryPubCarViolationsCount(pubCarViolationDto);
        //修车审批数量查询  2022-08-01
        PubcarRepairFormWhereDto pubcarRepairForm = new PubcarRepairFormWhereDto();
        pubcarRepairForm.setAuditStatus("1");
        pubcarRepairForm.setCommunityId(communityId);
        pubcarRepairForm.setUserId(context.getUserId());
        Integer count = pubcarRepairFormSMO.quertyPageCount(pubcarRepairForm);

        JSONObject responseJson = new JSONObject();
        responseJson.put("violationsCount", violationsCount);
        responseJson.put("approvalCount", approvalCount);
        responseJson.put("repairFormCount", count);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseJson.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarViolationConstant.INDEX_COUNT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
