package com.java110.front.smo.pubCarViolation.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.front.smo.pubCarViolation.IEditPubCarViolationSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加车辆违章信息表服务实现类
 * add by hh
 */
@Service("eidtPubCarViolationSMOImpl")
public class EditPubCarViolationSMOImpl extends AbstractComponentSMO implements IEditPubCarViolationSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "violationId", "违章信息ID不能为空");
        Assert.hasKeyAndValue(paramIn, "license", "必填，请填写车牌");
        Assert.hasKeyAndValue(paramIn, "brand", "必填，请选择类型");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarViolation.updatePubCarViolation",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updatePubCarViolation(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
