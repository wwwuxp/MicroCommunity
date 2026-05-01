package com.java110.front.smo.pubCarTrip.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.front.smo.pubCarTrip.IEditPubCarTripSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加行程管理表服务实现类
 * add by hh
 */
@Service("eidtPubCarTripSMOImpl")
public class EditPubCarTripSMOImpl extends AbstractComponentSMO implements IEditPubCarTripSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "tripId", "行程ID不能为空");
        Assert.hasKeyAndValue(paramIn, "license", "必填，请填写车牌");
        Assert.hasKeyAndValue(paramIn, "brand", "必填，请选择类型");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarTrip.updatePubCarTrip",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updatePubCarTrip(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
