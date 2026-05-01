package com.java110.front.smo.keyCabinetLog.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.keyCabinetLog.IListKeyCabinetLogSMO;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @date 2022/2/14
 */
@Service("listKeyCabinetLogSMOImpl")
public class ListKeyCabinetLogSMOImpl extends AbstractComponentSMO implements IListKeyCabinetLogSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        validatePageInfo(pd);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/keyCabinetLog.listKeyCabinetLogs" + mapToUrlParam(paramIn);

        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

    @Override
    public ResponseEntity<String> listKeyCabinetLog(IPageData pd) {
        return businessProcess(pd);
    }
}
