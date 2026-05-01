package com.java110.front.smo.pubCarUser.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.front.smo.pubCarUser.IDeletePubCarUserSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 删除用车人服务实现类
 * add by hh
 */
@Service("deletePubCarUserSMOImpl")
public class DeletePubCarUserSMOImpl extends AbstractComponentSMO implements IDeletePubCarUserSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "carUserId", "使用者ID不能为空");
        Assert.hasKeyAndValue(paramIn, "isSelfDriving", "是否是自驾参数不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "小区id不能为空");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarUser.deletePubCarUser",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deletePubCarUser(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
