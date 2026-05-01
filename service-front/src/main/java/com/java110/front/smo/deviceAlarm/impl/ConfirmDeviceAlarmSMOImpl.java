package com.java110.front.smo.deviceAlarm.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.deviceAlarm.IConfirmDeviceAlarmSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @date 2021/12/14
 */
@Service("confirmDeviceAlarmSMOImpl")
public class ConfirmDeviceAlarmSMOImpl extends AbstractComponentSMO implements IConfirmDeviceAlarmSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId不能为空");
        Assert.hasKeyAndValue(paramIn, "id", "id不能为空");
        Assert.hasKeyAndValue(paramIn, "handleState", "handleState不能为空");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/deviceAlarm.confirmDeviceAlarm",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> confirmDeviceAlarm(IPageData pd) {
        return businessProcess(pd);
    }
}
