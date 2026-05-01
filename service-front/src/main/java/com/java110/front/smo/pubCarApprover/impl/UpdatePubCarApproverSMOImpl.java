package com.java110.front.smo.pubCarApprover.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.pubCarApprover.PubCarApproverDto;
import com.java110.front.smo.pubCarApprover.IUpdatePubCarApproverSMO;
import com.java110.intf.pubcar.IPubCarApproverInnerServiceSMO;
import com.java110.po.pubCarApprover.PubCarApproverPo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hh
 * @date 2022/3/29
 */
@Service("updatePubCarApproverSMOImpl")
public class UpdatePubCarApproverSMOImpl extends AbstractComponentSMO implements IUpdatePubCarApproverSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IPubCarApproverInnerServiceSMO pubCarApproverInnerServiceSMOImpl;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId不能为空");
        Assert.hasKeyAndValue(paramIn, "id", "id不能为空");
        Assert.hasKey(paramIn, "approverId", "请选择审批人");
        Assert.hasKeyAndValue(paramIn, "roleType", "审批人类型不能为空");
        Assert.hasKeyAndValue(paramIn, "tableType", "表类型不能为空");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        // 检验是否存在相同的数据
        PubCarApproverDto existApproverDto = BeanConvertUtil.covertBean(paramIn, PubCarApproverDto.class);
        List<String> approverIds = new ArrayList<>();
        approverIds.add(paramIn.getString("approverId"));
        existApproverDto.setApproverIds(approverIds);
        if (pubCarApproverInnerServiceSMOImpl.existPubCarApprover(existApproverDto)){
            Integer tableType = existApproverDto.getTableType();
            if (Objects.equals(tableType, PubCarApproverPo.TYPE_PUBCAR)){
                throw new SMOException(HttpStatus.CONFLICT.value(), "已经存在相同用车审批人了");
            }else if (Objects.equals(tableType, PubCarApproverPo.TYPE_OUTGOING)){
                throw new SMOException(HttpStatus.CONFLICT.value(), "已经存在相同外出审批人了");
            }
        }

        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubCarApprover.updatePubCarApprover",
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> updatePubCarApprover(IPageData pd) {
        return businessProcess(pd);
    }

}
