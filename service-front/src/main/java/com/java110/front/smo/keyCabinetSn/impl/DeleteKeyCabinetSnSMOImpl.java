package com.java110.front.smo.keyCabinetSn.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.keyCabinetSn.IDeleteKeyCabinetSnSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @date 2022/2/19
 */
@Service("deleteKeyCabinetSnSMOImpl")
public class DeleteKeyCabinetSnSMOImpl extends AbstractComponentSMO implements IDeleteKeyCabinetSnSMO {

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
                ServiceConstant.SERVICE_API_URL + "/api/keyCabinetSn.deleteKeyCabinetSn",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> deleteKeyCabinetSn(IPageData pd) {
        return businessProcess(pd);
    }
}
