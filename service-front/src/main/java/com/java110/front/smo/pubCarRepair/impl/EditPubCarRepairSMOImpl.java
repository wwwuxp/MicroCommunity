package com.java110.front.smo.pubCarRepair.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.front.smo.pubCarRepair.IEditPubCarRepairSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加车辆维修信息表服务实现类
 * add by hh
 */
@Service("eidtPubCarRepairSMOImpl")
public class EditPubCarRepairSMOImpl extends AbstractComponentSMO implements IEditPubCarRepairSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "repairId", "维修信息ID不能为空");
Assert.hasKeyAndValue(paramIn, "license", "必填，请填写车牌");
Assert.hasKeyAndValue(paramIn, "brand", "必填，请选择类型");


        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST__PUBCARREPAIR);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarRepair.updatePubCarRepair",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updatePubCarRepair(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
