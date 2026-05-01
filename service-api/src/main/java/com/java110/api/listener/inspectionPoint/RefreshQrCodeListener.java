package com.java110.api.listener.inspectionPoint;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspection.IInspectionBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.community.IInspectionInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeInspectionPointConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;

/**
 * 刷新巡检点二维码
 * @author hh
 * create date 2021/9/16
 */
@Java110Listener("refreshQrCodeListener")
public class RefreshQrCodeListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IInspectionInnerServiceSMO inspectionInnerServiceSMOImpl;

    @Autowired
    private IInspectionBMO inspectionBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区id参数不能为空");
        Assert.hasKeyAndValue(reqJson, "inspectionId", "巡检点id不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        String code;
        // 判断识别码是否唯一
        do {
            // 生成8位字母数字随机识别码
            code = RandomStringUtils.randomAlphanumeric(8);
        } while (!inspectionInnerServiceSMOImpl.isCodeUnique(code));

        reqJson.put("code", code);
        inspectionBMOImpl.updateInspectionPoint(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionPointConstant.REFRESH_QRCODE_NSPECTIONPOINT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
