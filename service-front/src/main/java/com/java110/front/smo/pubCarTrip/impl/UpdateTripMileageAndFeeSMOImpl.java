package com.java110.front.smo.pubCarTrip.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IUpdateTripMileageAndFeeSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @date 2022/4/15
 */
@Service("updateTripMileageAndFeeSMOImpl")
public class UpdateTripMileageAndFeeSMOImpl extends AbstractComponentSMO implements IUpdateTripMileageAndFeeSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "tripId", "tripId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "driverId", "driverId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "startMileage", "开始里程不能为空");
        Assert.hasKeyAndValue(paramIn, "endMileage", "结束里程不能为空");
        Object startMileage = paramIn.get("startMileage");
        if (startMileage instanceof String) {
            String startMileageString = (String) startMileage;
            if (!startMileageString.matches(ConstantUtil.REGEX_DECIMAL)) {
                throw new IllegalArgumentException("开始里程数只能是数字类型");
            }
            paramIn.put("startMileage", Double.parseDouble(startMileageString));
        }
        Object endMileage = paramIn.get("endMileage");
        if (endMileage instanceof String) {
            String endMileageString = (String) endMileage;
            if (!endMileageString.matches(ConstantUtil.REGEX_DECIMAL)) {
                throw new IllegalArgumentException("开始里程数只能是数字类型");
            }
            paramIn.put("endMileage", Double.parseDouble(endMileageString));
        }
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarTrip.updateTripMileageAndFee",
                HttpMethod.POST);
    }


    @Override
    public ResponseEntity<String> updateTripMileageAndFee(IPageData pd) {
        return businessProcess(pd);
    }
}
