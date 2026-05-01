package com.java110.front.smo.pubCarReimburse.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarReimburse.IAuditPubCarReimburseSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @date 2021/10/12
 */
@Service("auditPubCarReimburseSMOImpl")
public class AuditPubCarReimburseSMOImpl extends AbstractComponentSMO implements IAuditPubCarReimburseSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "approvalStatus", "审批状态参数不能为空");
        Assert.hasKey(paramIn, "reimburseIds", "reimburseIds 节点不能为空");
        JSONArray reimburseIds = paramIn.getJSONArray("reimburseIds");
        if (CollectionUtils.isEmpty(reimburseIds)){
            throw new IllegalArgumentException("请传入需要审批的报销id");
        }
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarReimburse.auditPubCarReimburse",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> auditPubCarReimburse(IPageData pd) {
        return businessProcess(pd);
    }
}
