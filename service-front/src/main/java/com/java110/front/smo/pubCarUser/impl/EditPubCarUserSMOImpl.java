package com.java110.front.smo.pubCarUser.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.pubCarUser.PubCarExistUserDto;
import com.java110.front.smo.pubCarUser.IEditPubCarUserSMO;
import com.java110.intf.pubcar.IPubCarUserInnerServiceSMO;
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

import java.util.List;

/**
 * 添加用车人管理表服务实现类
 * add by hh
 */
@Service("eidtPubCarUserSMOImpl")
public class EditPubCarUserSMOImpl extends AbstractComponentSMO implements IEditPubCarUserSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IPubCarUserInnerServiceSMO pubCarUserInnerServiceSMOImpl;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "carUserId", "用车人ID不能为空");
        validatePubCarUser(paramIn);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        // 查询是否已经存在相同用车人或自驾人
        PubCarExistUserDto existUserDto = BeanConvertUtil.covertBean(paramIn, PubCarExistUserDto.class);
        List<String> existPubCarUsers = pubCarUserInnerServiceSMOImpl.listExistPubCarUser(existUserDto);
        if (existPubCarUsers.size() == 1) {
            Integer isSelfDriving = paramIn.getInteger("isSelfDriving");
            String msg = "";
            if (isSelfDriving == 0) {
                msg = "已经存在相同用车人了";
            } else if (isSelfDriving == 1) {
                msg = "已经存在相同自驾人了";
            }
            throw new SMOException(HttpStatus.CONFLICT.value(), msg);
        }

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarUser.updatePubCarUser",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> updatePubCarUser(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
