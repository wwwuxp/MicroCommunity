package com.java110.api.listener.pubCarReimburse;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarReimburse.PubCarReimburseDto;
import com.java110.intf.pubcar.IPubCarReimburseInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarReimburseConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarReimburse.ApiPubCarReimburseDataVo;
import com.java110.vo.api.pubCarReimburse.ApiPubCarReimburseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询用车报销列表 侦听
 * @author hh
 * @date 2021/10/12
 */
@Java110Listener("listPubCarReimburseListener")
public class ListPubCarReimburseListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarReimburseInnerServiceSMO pubCarReimburseInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        PubCarReimburseDto pubCarReimburseDto = BeanConvertUtil.covertBean(reqJson, PubCarReimburseDto.class);

        int count = pubCarReimburseInnerServiceSMOImpl.listPubCarReimbursesCount(pubCarReimburseDto);

        List<ApiPubCarReimburseDataVo> pubCarReimburseDataVos;

        if (count > 0) {
            pubCarReimburseDataVos = pubCarReimburseInnerServiceSMOImpl.listPubCarReimburses(pubCarReimburseDto);
        } else {
            pubCarReimburseDataVos = Collections.emptyList();
        }

        ApiPubCarReimburseVo apiPubCarReimburseVo = new ApiPubCarReimburseVo();

        apiPubCarReimburseVo.setTotal(count);
        apiPubCarReimburseVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarReimburseVo.setData(pubCarReimburseDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPubCarReimburseVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarReimburseConstant.LIST_PUBCAR_REIMBURSE;
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
