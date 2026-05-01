package com.java110.api.listener.pubCarApprover;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarApprover.PubCarApproverDto;
import com.java110.intf.pubcar.IPubCarApproverInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarApproverConstant;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

/**
 * 删除公车审批人信息
 * @author hh
 * @date 2021/12/13
 */
@Java110Listener("deletePubCarApproverListener")
public class DeletePubCarApproverListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarApproverInnerServiceSMO pubCarApproverInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        Integer id = reqJson.getInteger("id");
        PubCarApproverDto pubCarApproverDto = new PubCarApproverDto();
        pubCarApproverDto.setId(id);
        int deleteFlag = pubCarApproverInnerServiceSMOImpl.deleteById(pubCarApproverDto);
        if (deleteFlag < 1){
            throw new RuntimeException("删除失败");
        }

        ResponseEntity<String> success = ResultVo.success();
        context.setResponseEntity(success);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarApproverConstant.DELETE_APPROVER;
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
