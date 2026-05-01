package com.java110.front.smo.pubCarDriver.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.pubCarDriver.PubCarExistDriverDto;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.pubCarDriver.IAddPubCarDriverSMO;
import com.java110.intf.pubcar.IPubCarDriverInnerServiceSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 添加司机服务实现类
 * add by hh
 */
@Service("addPubCarDriverSMOImpl")
public class AddPubCarDriverSMOImpl extends AbstractComponentSMO implements IAddPubCarDriverSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IPubCarDriverInnerServiceSMO pubCarDriverInnerServiceSMOImpl;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        validateDriver(paramIn);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        // 检验是否已经存在相同司机了
        PubCarExistDriverDto existDriverDto = BeanConvertUtil.covertBean(paramIn, PubCarExistDriverDto.class);
        if (pubCarDriverInnerServiceSMOImpl.existPubCarDriver(existDriverDto)){
            throw new SMOException(HttpStatus.CONFLICT.value(), "已经存在相同(电话或工号或员工卡号或身份证号)的司机了");
        }

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarDriver.savePubCarDriver",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> savePubCarDriver(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
