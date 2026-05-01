package com.java110.api.listener.pubCarApprover;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarApprover.PubCarApproverDto;
import com.java110.intf.pubcar.IPubCarApproverInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarApproverConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarApprover.ApiPubCarApproverDataVo;
import com.java110.vo.api.pubCarApprover.ApiPubCarApproverVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询公车审批人信息
 * @author hh
 * @date 2021/12/13
 */
@Java110Listener("listPubCarApproverListener")
public class ListPubCarApproverListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarApproverInnerServiceSMO pubCarApproverInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        PubCarApproverDto pubCarApproverDto = BeanConvertUtil.covertBean(reqJson, PubCarApproverDto.class);

        int count = pubCarApproverInnerServiceSMOImpl.queryPubCarApproversCount(pubCarApproverDto);

        List<ApiPubCarApproverDataVo> pubCarApproverDataVos;

        if (count > 0) {
            pubCarApproverDataVos = pubCarApproverInnerServiceSMOImpl.queryPubCarApprovers(pubCarApproverDto);
        } else {
            pubCarApproverDataVos = Collections.emptyList();
        }

        ApiPubCarApproverVo apiPubCarApproverVo = new ApiPubCarApproverVo();

        apiPubCarApproverVo.setTotal(count);
        apiPubCarApproverVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarApproverVo.setData(pubCarApproverDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPubCarApproverVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarApproverConstant.LIST_APPROVER;
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
