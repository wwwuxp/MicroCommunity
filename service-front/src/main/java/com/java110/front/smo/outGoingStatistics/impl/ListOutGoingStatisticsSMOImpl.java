package com.java110.front.smo.outGoingStatistics.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.outGoingStatistics.IListOutGoingStatisticsSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author hh
 * @date 2021/12/20
 */
@Service("listOutGoingStatisticsSMOImpl")
public class ListOutGoingStatisticsSMOImpl extends AbstractComponentSMO implements IListOutGoingStatisticsSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        String startDate = paramIn.getString("startDate").trim();
        String endDate = paramIn.getString("endDate").trim();
        paramIn.put("startDate", startDate + " 00:00:00");
        paramIn.put("endDate", endDate + " 23:59:59");
        validatePageInfo(pd);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/outGoing.listOutGoingStatistics" + mapToUrlParam(paramIn);

        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

    @Override
    public ResponseEntity<String> listOutGoingStatistics(IPageData pd) {
        return businessProcess(pd);
    }

}
