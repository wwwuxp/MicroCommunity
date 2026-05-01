package com.java110.front.smo.visitInvitation.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.visitInvitation.IEditVisitInvitationSMO;
import com.java110.po.visitInvitation.VisitInvitationPo;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * create date 2021/9/23
 */
@Service("editVisitInvitationSMOImpl")
public class EditVisitInvitationSMOImpl extends AbstractComponentSMO implements IEditVisitInvitationSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "invitationId", "invitationId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "visitType", "到访类型不能为空");
        if (VisitInvitationPo.CUSTOM.equals(paramIn.getInteger("visitType"))){
            Assert.hasKeyAndValue(paramIn, "visitTime", "到访时间不能为空");
        }
        Assert.hasKey(paramIn, "visitors", "visitors节点不能为空");
        JSONArray visitorArray = paramIn.getJSONArray("visitors");
        if (CollectionUtils.isEmpty(visitorArray)){
            throw new IllegalArgumentException("访客信息不能为空");
        }
        for (int i = 0; i < visitorArray.size(); i++) {
            JSONObject visitorObject = visitorArray.getJSONObject(i);
            Assert.hasKeyAndValue(visitorObject, "no", "到访人编号不能为空");
            Assert.hasKeyAndValue(visitorObject, "name", "到访人姓名不能为空");
            Assert.hasKeyAndValue(visitorObject, "idCard", "身份证号不能为空");
        }
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/visitInvitation.updateInvitation",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> editVisitInvitation(IPageData pd) {
        return businessProcess(pd);
    }

}
