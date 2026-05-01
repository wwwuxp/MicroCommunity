package com.java110.front.smo.pubCar.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.pubCar.ExistPubCarDto;
import com.java110.front.smo.pubCar.IAddPubCarSMO;
import com.java110.intf.pubcar.IPubCarInnerServiceSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * 添加车辆服务实现类
 * add by hh
 */
@Service("addPubCarSMOImpl")
public class AddPubCarSMOImpl extends AbstractComponentSMO implements IAddPubCarSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IPubCarInnerServiceSMO pubCarInnerServiceSMOImpl;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        validatePubCar(paramIn);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        // 检查车牌号在数据库中是否存在
        ExistPubCarDto requestExistPubCarDto = BeanConvertUtil.covertBean(paramIn, ExistPubCarDto.class);
        ExistPubCarDto responseExistPubCarDto = pubCarInnerServiceSMOImpl.existPubCar(requestExistPubCarDto);
        if (Objects.nonNull(responseExistPubCarDto)) {
            String requestLicense = requestExistPubCarDto.getLicense();
            String responseLicense = responseExistPubCarDto.getLicense();
            if (requestLicense.equals(responseLicense)){
                throw new SMOException(HttpStatus.CONFLICT.value(), "已经存在相同车牌号车辆了");
            }
            String requestKeyCabinet = requestExistPubCarDto.getKeyCabinet();
            String responseKeyCabinet = responseExistPubCarDto.getKeyCabinet();
            if (requestKeyCabinet.equals(responseKeyCabinet)){
                throw new SMOException(HttpStatus.CONFLICT.value(), responseLicense + "已经在" + requestKeyCabinet + "号钥匙柜了");
            }
        }

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCar.savePubCar",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> savePubCar(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
