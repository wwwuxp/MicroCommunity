package com.java110.api.listener.outGoing;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.dto.pubCarApprovalProcess.PubCarApprovalProcessDto;
import com.java110.dto.pubCarApprover.PubCarApproverDto;
import com.java110.intf.pubcar.IOutGoingInnerServiceSMO;
import com.java110.intf.pubcar.IPubCarApprovalProcessInnerServiceSMO;
import com.java110.intf.pubcar.IPubCarApproverInnerServiceSMO;
import com.java110.po.pubCarApprover.PubCarApproverPo;
import com.java110.utils.constant.ServiceCodeOutGoingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.outGoing.ApiOutGoingDataVo;
import com.java110.vo.api.outGoing.ApiOutGoingVo;
import com.java110.vo.api.outGoing.ApiUserOutGoingDataVo;
import com.java110.vo.api.outGoing.ApiUserOutGoingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 查询外出登记信息 侦听类
 */
@Java110Listener("listOutGoingsListener")
public class ListOutGoingsListener extends AbstractServiceApiListener {

    @Autowired
    private IOutGoingInnerServiceSMO outGoingInnerServiceSMOImpl;

    @Autowired
    private IPubCarApprovalProcessInnerServiceSMO pubCarApprovalProcessInnerServiceSMOImpl;

    @Autowired
    private IPubCarApproverInnerServiceSMO pubCarApproverInnerServiceSMOImpl;


    @Override
    public String getServiceCode() {
        return ServiceCodeOutGoingConstant.LIST_OUTGOINGS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        OutGoingDto outGoingDto = BeanConvertUtil.covertBean(reqJson, OutGoingDto.class);

        if (Objects.isNull(outGoingDto.getUserId())) {
            // userId为空，是查询外出审批列表
            if (Objects.isNull(outGoingDto.getState())) {
                outGoingDto.setState(2);
            }

            List<ApiOutGoingDataVo> outGoings;
            int count = 0;
            PubCarApproverDto pubCarApproverDto = pubCarApproverInnerServiceSMOImpl.getRoleTypeAndDepartmentIdByApproverId(context.getUserId(), PubCarApproverPo.TYPE_OUTGOING);
            if (Objects.nonNull(pubCarApproverDto)){
                Integer roleType = pubCarApproverDto.getRoleType();
                // 在外出审批人中有配置，是审批人
                PubCarApprovalProcessDto pubCarApprovalProcessDto = new PubCarApprovalProcessDto();
                pubCarApprovalProcessDto.setCurrentUserId(context.getUserId());
                pubCarApprovalProcessDto.setApprovalStatus(2);
                pubCarApprovalProcessDto.setReviewerRole(roleType);
                count = pubCarApprovalProcessInnerServiceSMOImpl.getOutGoingIdsCount(pubCarApprovalProcessDto);

                if (count > 0) {
                    List<String> outGoingIds = pubCarApprovalProcessInnerServiceSMOImpl.getOutGoingIds(pubCarApprovalProcessDto);
                    outGoingDto.setOutIds(outGoingIds);
                    outGoings = outGoingInnerServiceSMOImpl.queryOutGoings(outGoingDto);
                    for (ApiOutGoingDataVo outGoing : outGoings) {
                        outGoing.setReviewerRole(roleType);
                    }
                } else {
                    outGoings = Collections.emptyList();
                }
            }else {
                // 没有在外出审批人中配置，一律认为是普通员工，普通员工无权审批
                outGoings = Collections.emptyList();
            }

            ApiOutGoingVo apiOutGoingVo = new ApiOutGoingVo();

            apiOutGoingVo.setTotal(count);
            apiOutGoingVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
            apiOutGoingVo.setData(outGoings);

            ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiOutGoingVo), HttpStatus.OK);

            context.setResponseEntity(responseEntity);
        }else {
            // userId不为空，是外出记录列表
            if (Objects.isNull(outGoingDto.getState())) {
                outGoingDto.setState(2);
            }

            int count = outGoingInnerServiceSMOImpl.queryOutGoingsCount(outGoingDto);

            List<ApiUserOutGoingDataVo> userOutGoings;

            if (count > 0) {
                userOutGoings = outGoingInnerServiceSMOImpl.queryUserOutGoings(outGoingDto);
            } else {
                userOutGoings = Collections.emptyList();
            }

            ApiUserOutGoingVo apiUserOutGoingVo = new ApiUserOutGoingVo();

            apiUserOutGoingVo.setTotal(count);
            apiUserOutGoingVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
            apiUserOutGoingVo.setData(userOutGoings);

            ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiUserOutGoingVo), HttpStatus.OK);

            context.setResponseEntity(responseEntity);
        }

    }
}
