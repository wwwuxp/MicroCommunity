package com.java110.front.smo.pubCarArchives.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.pubCarArchives.IListPubCarArchivesSMO;
import com.java110.utils.constant.ServiceConstant;
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
 * @author hh
 * @datetime 2021/8/6 11:24
 */
@Service("listPubCarArchivesSMOImpl")
public class ListPubCarArchivesSMOImpl extends AbstractComponentSMO implements IListPubCarArchivesSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        super.validatePageInfo(pd);
    }

    @Override
    public ResponseEntity<String> listPubCarArchives(IPageData pd) {
        return businessProcess(pd);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);
        // 标记为车辆档案查询
        paramIn.put(ConstantUtil.SIMPLE_FLAG, ConstantUtil.SIMPLE_FLAG_CAR_ARCHIVES);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/pubCar.listPubCars" + mapToUrlParam(paramIn);


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

}
