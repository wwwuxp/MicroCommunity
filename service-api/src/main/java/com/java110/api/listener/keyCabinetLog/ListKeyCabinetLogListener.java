package com.java110.api.listener.keyCabinetLog;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.keyCabinetLog.KeyCabinetLogDto;
import com.java110.intf.pubcar.IkeyCabinetLogInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeKeyCabinetLogConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.keyCabinetLog.ApiKeyCabinetLogDataVo;
import com.java110.vo.api.keyCabinetLog.ApiKeyCabinetLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询取还钥匙记录数据
 * @author hh
 * @date 2022/2/14
 */
@Java110Listener("listKeyCabinetLogListener")
public class ListKeyCabinetLogListener extends AbstractServiceApiListener {

    @Autowired
    private IkeyCabinetLogInnerServiceSMO keyCabinetLogInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        KeyCabinetLogDto keyCabinetLogDto = BeanConvertUtil.covertBean(reqJson, KeyCabinetLogDto.class);

        int count = keyCabinetLogInnerServiceSMOImpl.queryKeyCabinetLogsCount(keyCabinetLogDto);

        List<ApiKeyCabinetLogDataVo> apiKeyCabinetLogDataVos;

        if (count > 0) {
            apiKeyCabinetLogDataVos = keyCabinetLogInnerServiceSMOImpl.queryKeyCabinetLogs(keyCabinetLogDto);
        } else {
            apiKeyCabinetLogDataVos = Collections.emptyList();
        }

        ApiKeyCabinetLogVo apiKeyCabinetLogVo = new ApiKeyCabinetLogVo();

        apiKeyCabinetLogVo.setTotal(count);
        apiKeyCabinetLogVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiKeyCabinetLogVo.setData(apiKeyCabinetLogDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiKeyCabinetLogVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeKeyCabinetLogConstant.LIST_KEY_CABINET_LOG;
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
