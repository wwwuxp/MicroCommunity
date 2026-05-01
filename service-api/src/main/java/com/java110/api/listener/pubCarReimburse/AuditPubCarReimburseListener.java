package com.java110.api.listener.pubCarReimburse;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarReimburse.PubCarReimburseDto;
import com.java110.intf.pubcar.IPubCarReimburseInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarReimburseConstant;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;

/**
 * 审批用车报销侦听
 * @author hh
 * @date 2021/10/12
 */
@Java110Listener("auditPubCarReimburseListener")
public class AuditPubCarReimburseListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarReimburseInnerServiceSMO pubCarReimburseInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        PubCarReimburseDto pubCarReimburseDto = new PubCarReimburseDto();
        pubCarReimburseDto.setReimburseIds(reqJson.getJSONArray("reimburseIds").toJavaList(String.class));
        pubCarReimburseDto.setCommunityId(reqJson.getString("communityId"));
        pubCarReimburseDto.setApprovalStatus(reqJson.getInteger("approvalStatus"));
        boolean audit = pubCarReimburseInnerServiceSMOImpl.auditPubCarReimburses(pubCarReimburseDto);
        if (audit){
            context.setResponseEntity(ResultVo.success());
        }else {
            context.setResponseEntity(ResultVo.error("审批失败"));
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarReimburseConstant.AUDIT_PUBCAR_REIMBURSE;
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
