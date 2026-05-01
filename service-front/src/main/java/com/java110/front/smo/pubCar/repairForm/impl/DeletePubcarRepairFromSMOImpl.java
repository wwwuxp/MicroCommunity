package com.java110.front.smo.pubCar.repairForm.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCar.repairForm.DeletePubcarRepairFromSMO;
import com.java110.intf.pubcar.PubcarRepairFormSMO;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author lmf
 * @date 2022/7/26
 */
@Service("deletePubcarRepairFromSMOImpl")
public class DeletePubcarRepairFromSMOImpl extends AbstractComponentSMO implements DeletePubcarRepairFromSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    PubcarRepairFormSMO pubcarRepairFormSMO;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> delete(IPageData pd) {
        return super.businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubcarRepairFrom.delete",
                HttpMethod.POST);
        return responseEntity;
    }
}
