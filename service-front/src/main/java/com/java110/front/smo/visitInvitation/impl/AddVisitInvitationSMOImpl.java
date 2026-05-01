package com.java110.front.smo.visitInvitation.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.visitInvitation.IAddVisitInvitationSMO;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * create date 2021/9/23
 */
@Service("addVisitInvitationSMOImpl")
public class AddVisitInvitationSMOImpl extends AbstractComponentSMO implements IAddVisitInvitationSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) { }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/visitInvitation.saveInvitation",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> addVisitInvitation(IPageData pd) {
        return businessProcess(pd);
    }
}
