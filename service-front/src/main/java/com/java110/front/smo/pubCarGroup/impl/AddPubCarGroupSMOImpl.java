package com.java110.front.smo.pubCarGroup.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.pubCarGroup.PubCarGroupDto;
import com.java110.intf.pubcar.IPubCarGroupInnerServiceSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.front.smo.pubCarGroup.IAddPubCarGroupSMO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加分组服务实现类
 * add by hh
 */
@Service("addPubCarGroupSMOImpl")
public class AddPubCarGroupSMOImpl extends AbstractComponentSMO implements IAddPubCarGroupSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IPubCarGroupInnerServiceSMO pubCarGroupInnerServiceSMOImpl;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        validateGroup(paramIn);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        // 检验是否存在相同的分组类型和分组名称
        PubCarGroupDto existGroupDto = BeanConvertUtil.covertBean(paramIn, PubCarGroupDto.class);
        if (pubCarGroupInnerServiceSMOImpl.existPubCarGroup(existGroupDto)){
            throw new SMOException(HttpStatus.CONFLICT.value(), "已经存在相同类型和名称的分组了");
        }

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarGroup.savePubCarGroup",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> savePubCarGroup(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
