package com.java110.front.smo.visitInvitation.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.visitInvitation.IDeleteVisitInvitationSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 删除访客信息
 * @author hh
 * create date 2021/9/23
 */
@Service("deleteVisitInvitationSMOImpl")
public class DeleteVisitInvitationSMOImpl extends AbstractComponentSMO implements IDeleteVisitInvitationSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "invitationId", "访客邀约id 参数不能为空");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/visitInvitation.deleteInvitation",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> deleteVisitInvitation(IPageData pd) {
        return businessProcess(pd);
    }
}
