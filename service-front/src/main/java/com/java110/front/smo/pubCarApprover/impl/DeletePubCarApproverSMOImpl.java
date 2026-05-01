package com.java110.front.smo.pubCarApprover.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarApprover.IDeletePubCarApproverSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @date 2021/12/13
 */
@Service("deletePubCarApproverSMOImpl")
public class DeletePubCarApproverSMOImpl extends AbstractComponentSMO implements IDeletePubCarApproverSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "id", "id不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId不能为空");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarApprover.deletePubCarApprover",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> deletePubCarApprover(IPageData pd) {
        return businessProcess(pd);
    }

}
