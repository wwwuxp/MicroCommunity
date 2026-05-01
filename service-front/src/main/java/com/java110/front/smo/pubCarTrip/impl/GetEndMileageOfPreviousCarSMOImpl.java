package com.java110.front.smo.pubCarTrip.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.pubCarTrip.IGetEndMileageOfPreviousCarSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @date 2022/4/6
 */
@Service("getEndMileageOfPreviousCarSMOImpl")
public class GetEndMileageOfPreviousCarSMOImpl extends AbstractComponentSMO implements IGetEndMileageOfPreviousCarSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "carId", "carId 参数不能为空");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("storeId", result.getStoreId());
        paramIn.put("storeTypeCd", result.getStoreTypeCd());

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/pubCarTrip.getEndMileageOfPrevious" + mapToUrlParam(paramIn);

        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

    @Override
    public ResponseEntity<String> getEndMileageOfPreviousCar(IPageData pd) {
        return null;
    }

}
