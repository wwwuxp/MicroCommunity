package com.java110.front.smo.pubCarTrip.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IAuditPubCarTripSMO;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 审核行程服务实现类
 * add by hh
 */
@Service("auditPubCarTripSMOImpl")
public class AuditPubCarTripSMOImpl extends AbstractComponentSMO implements IAuditPubCarTripSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        //validateAuditTrip(paramIn);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarTrip.auditPubCarTrip",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> auditPubCarTrip(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
