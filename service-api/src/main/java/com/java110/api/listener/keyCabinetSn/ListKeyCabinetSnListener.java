package com.java110.api.listener.keyCabinetSn;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.keyCabinetSn.KeyCabinetSnDto;
import com.java110.intf.pubcar.IKeyCabinetSnInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeKeyCabinetSnConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.keyCabinetSn.ApiKeyCabinetSnDataVo;
import com.java110.vo.api.keyCabinetSn.ApiKeyCabinetSnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * @author hh
 * @date 2022/2/19
 */
@Java110Listener("listKeyCabinetSnListener")
public class ListKeyCabinetSnListener extends AbstractServiceApiListener {

    @Autowired
    private IKeyCabinetSnInnerServiceSMO keyCabinetSnInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        KeyCabinetSnDto keyCabinetSnDto = BeanConvertUtil.covertBean(reqJson, KeyCabinetSnDto.class);

        int count = keyCabinetSnInnerServiceSMOImpl.queryKeyCabinetSnsCount(keyCabinetSnDto);

        List<ApiKeyCabinetSnDataVo> apiKeyCabinetSnDataVos;

        if (count > 0) {
            apiKeyCabinetSnDataVos = keyCabinetSnInnerServiceSMOImpl.queryKeyCabinetSns(keyCabinetSnDto);
        } else {
            apiKeyCabinetSnDataVos = Collections.emptyList();
        }

        ApiKeyCabinetSnVo apiKeyCabinetSnVo = new ApiKeyCabinetSnVo();

        apiKeyCabinetSnVo.setTotal(count);
        apiKeyCabinetSnVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiKeyCabinetSnVo.setData(apiKeyCabinetSnDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiKeyCabinetSnVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeKeyCabinetSnConstant.KEY_CABINET_SN_LIST;
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
