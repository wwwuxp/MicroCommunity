package com.java110.front.smo.pubCarStatistics.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.pubCarStatistics.IListDepartmentStatisticsSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 查询部门用车统计
 */
@Service("listPubCarDepartmentStatisticsSMOImpl")
public class ListDepartmentStatisticsSMOImpl extends AbstractComponentSMO implements IListDepartmentStatisticsSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listDepartmentStatistics(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        super.validatePageInfo(pd);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);
        // 标记为部门用车统计
        paramIn.put(ConstantUtil.SIMPLE_FLAG, ConstantUtil.SIMPLE_FLAG_STATICS);
        paramIn.put("storeId",result.getStoreId());
        paramIn.put("storeTypeCd",result.getStoreTypeCd());
        paramIn.put("communityId",result.getCommunityId());
        paramIn.put("userId",result.getUserId());

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/pubCarUser.listPubCarUsers" + mapToUrlParam(paramIn);


        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

}
