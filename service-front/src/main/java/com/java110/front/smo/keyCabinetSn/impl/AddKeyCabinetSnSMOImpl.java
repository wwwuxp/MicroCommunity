package com.java110.front.smo.keyCabinetSn.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.keyCabinetSn.KeyCabinetSnDto;
import com.java110.front.smo.keyCabinetSn.IAddKeyCabinetSnSMO;
import com.java110.intf.pubcar.IKeyCabinetSnInnerServiceSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @date 2022/2/18
 */
@Service("addKeyCabinetSnSMOImpl")
public class AddKeyCabinetSnSMOImpl extends AbstractComponentSMO implements IAddKeyCabinetSnSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IKeyCabinetSnInnerServiceSMO keyCabinetSnInnerServiceSMOImpl;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "name", "名称不能为空");
        Assert.hasKeyAndValue(paramIn, "quantity", "柜子数量不能为空");
        Assert.hasKeyAndValue(paramIn, "deviceSn", "mac地址不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId不能为空");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        // 检验是否存在相同的数据
        KeyCabinetSnDto keyCabinetSnDto = BeanConvertUtil.covertBean(paramIn, KeyCabinetSnDto.class);
        boolean exist = keyCabinetSnInnerServiceSMOImpl.existSameDeviceSn(keyCabinetSnDto);
        if (exist){
            // 存在相同设备sn的数据
            throw new SMOException(HttpStatus.CONFLICT.value(), "已经存在相同设备sn的柜子");
        }

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/keyCabinetSn.saveKeyCabinetSn",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> addKeyCabinetSn(IPageData pd) {
        return businessProcess(pd);
    }
}
