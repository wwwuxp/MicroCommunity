package com.java110.front.smo.keyCabinetSn.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.keyCabinetSn.IListKeyCabinetSnSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @date 2022/2/19
 */
@Service("listKeyCabinetSnSMOImpl")
public class ListKeyCabinetSnSMOImpl extends AbstractComponentSMO implements IListKeyCabinetSnSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId参数不能为空");
        if (StringUtils.isBlank(paramIn.getString("choose"))){
            validatePageInfo(pd);
        }
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/keyCabinetSn.listKeyCabinetSns" + mapToUrlParam(paramIn);

        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

    @Override
    public ResponseEntity<String> listKeyCabinetSn(IPageData pd) {
        return businessProcess(pd);
    }
}
