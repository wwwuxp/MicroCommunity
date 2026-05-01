package com.java110.api.listener.inspectionPoint;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.community.IInspectionInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeInspectionPointConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.vo.api.inspectionPoint.InspectionPointSimpleDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 巡检点二维码识别
 * @author hh
 * create date 2021/9/15
 */
@Java110Listener("identifyQrCodeListener")
public class IdentifyQrCodeListener extends AbstractServiceApiListener {

    @Autowired
    private IInspectionInnerServiceSMO inspectionInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "code", "识别码不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        List<InspectionPointSimpleDataVo> inspectionDtos = inspectionInnerServiceSMOImpl.queryInspectionsByCode(reqJson.getString("code"));

        if (CollectionUtils.isEmpty(inspectionDtos)){
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "该二维码已失效");
        }

        Assert.listOnlyOne(inspectionDtos, "数据错误，根据code查询到多条巡检点");

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(inspectionDtos.get(0)), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionPointConstant.IDENTIFY_QRCODE_NSPECTIONPOINT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

}
