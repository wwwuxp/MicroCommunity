package com.java110.front.smo.pubCar.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCar.IConfirmPubCarSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ConstantUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * 确认已续保、确认已保养、确认已季审或已领标、确认已年审或已领标
 * @author hh
 * @datetime 2021/8/25 9:12
 */
@Service("confirmPubCarSMOImpl")
public class ConfirmPubCarSMOImpl extends AbstractComponentSMO implements IConfirmPubCarSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "carId", "公车ID不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");

        if (paramIn.containsKey("maintenanceFee")){
            String maintenanceFee = paramIn.getString("maintenanceFee");
            if (StringUtils.isBlank(maintenanceFee)){
                paramIn.put("maintenanceFee", "0.00");
            }else if (!maintenanceFee.matches(ConstantUtil.REGEX_DECIMAL)){
                throw new IllegalArgumentException("输入的保养费用格式不正确");
            }else {
                paramIn.put("maintenanceFee", new BigDecimal(maintenanceFee));
            }
        }
        checkKey(paramIn, "quarterReviewContent", "下次季审内容不能为空");
        checkKey(paramIn, "quarterReview", "下次季审时间不能为空");
        checkKey(paramIn, "yearReviewContent", "下次年审内容不能为空");
        checkKey(paramIn, "yearReview", "下次年审时间不能为空");
        checkKey(paramIn, "maintenanceExpirationDate", "下次保养时间不能为空");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCar.confirmPubCar",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> confirmPubCar(IPageData pd) {
        return super.businessProcess(pd);
    }

    private void checkKey(JSONObject paramIn, String key, String message){
        if (paramIn.containsKey(key)){
            Assert.hasKeyAndValue(paramIn, key, message);
        }
    }
}
