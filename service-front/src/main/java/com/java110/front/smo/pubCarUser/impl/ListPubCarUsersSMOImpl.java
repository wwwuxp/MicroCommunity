package com.java110.front.smo.pubCarUser.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.front.smo.pubCarUser.IListPubCarUsersSMO;
import com.java110.utils.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 查询用车人服务类
 */
@Service("listPubCarUsersSMOImpl")
public class ListPubCarUsersSMOImpl extends AbstractComponentSMO implements IListPubCarUsersSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listPubCarUsers(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    public ResponseEntity<String> listPubCarUserDetails(IPageData pd) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        Assert.hasKeyAndValue(paramIn, "isSelfDriving", "是否是自驾参数不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "小区id不能为空");

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);
        paramIn.put(ConstantUtil.SIMPLE_FLAG, ConstantUtil.SIMPLE_FLAG_YES);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/pubCarUser.listPubCarUsers" + mapToUrlParam(paramIn);

        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

    @Override
    public ResponseEntity<String> listDepartmentNames(IPageData pd) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");

        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);
        paramIn.put(ConstantUtil.SIMPLE_FLAG, ConstantUtil.SIMPLE_FLAG_DEPARTMENT_NAME);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/pubCarUser.listPubCarUsers" + mapToUrlParam(paramIn);


        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "isSelfDriving", "isSelfDriving参数不能为空");
        super.validatePageInfo(pd);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/pubCarUser.listPubCarUsers" + mapToUrlParam(paramIn);


        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
