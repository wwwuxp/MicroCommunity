package com.java110.api.listener.keyCabinetSn;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.keyCabinetSn.KeyCabinetSnDto;
import com.java110.intf.pubcar.IKeyCabinetSnInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeKeyCabinetSnConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

/**
 * @author hh
 * @date 2022/2/18
 */
@Java110Listener("saveKeyCabinetSnListener")
public class SaveKeyCabinetSnListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IKeyCabinetSnInnerServiceSMO keyCabinetSnInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        KeyCabinetSnDto keyCabinetSnDto = BeanConvertUtil.covertBean(reqJson, KeyCabinetSnDto.class);
        int saveFlag = keyCabinetSnInnerServiceSMOImpl.saveKeyCabinetSn(keyCabinetSnDto);
        if (saveFlag < 1){
            throw new RuntimeException("保存失败");
        }

        ResponseEntity<String> success = ResultVo.success();
        context.setResponseEntity(success);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeKeyCabinetSnConstant.KEY_CABINET_SN_SAVE;
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
