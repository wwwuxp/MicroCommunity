package com.java110.front.smo.pubCarDataScreen.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarDataScreen.IGetDataScreenOverviewAndAnalysisSMO;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @datetime 2021/8/16 14:03
 */
@Service("getDataScreenOverviewAndAnalysisSMOImpl")
public class GetDataScreenOverviewAndAnalysisSMOImpl extends AbstractComponentSMO implements IGetDataScreenOverviewAndAnalysisSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        // Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        /*ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);*/

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/dataScreen.getOverviewAndAnalysis" + mapToUrlParam(paramIn);


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> getOverviewAndAnalysis(IPageData pd) {
        return businessProcess(pd);
    }
}
