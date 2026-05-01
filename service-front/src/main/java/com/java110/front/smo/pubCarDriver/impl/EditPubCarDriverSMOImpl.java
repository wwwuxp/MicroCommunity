package com.java110.front.smo.pubCarDriver.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.pubCarDriver.PubCarExistDriverDto;
import com.java110.intf.pubcar.IPubCarDriverInnerServiceSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.front.smo.pubCarDriver.IEditPubCarDriverSMO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加司机信息表服务实现类
 * add by hh
 */
@Service("eidtPubCarDriverSMOImpl")
public class EditPubCarDriverSMOImpl extends AbstractComponentSMO implements IEditPubCarDriverSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IPubCarDriverInnerServiceSMO pubCarDriverInnerServiceSMOImpl;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "driverId", "请传递 driverId 参数");
        validateDriver(paramIn);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        // 检验是否已经存在相同司机了
        PubCarExistDriverDto existDriverDto = BeanConvertUtil.covertBean(paramIn, PubCarExistDriverDto.class);
        if (pubCarDriverInnerServiceSMOImpl.existPubCarDriver(existDriverDto)){
            throw new SMOException(HttpStatus.CONFLICT.value(), "已经存在相同(电话或工号或员工卡号或身份证号)的司机了");
        }

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarDriver.updatePubCarDriver",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> updatePubCarDriver(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
