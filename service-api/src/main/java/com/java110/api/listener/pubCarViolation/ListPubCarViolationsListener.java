package com.java110.api.listener.pubCarViolation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarViolation.PubCarViolationDto;
import com.java110.intf.pubcar.IPubCarViolationInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarViolationConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarViolation.ApiPubCarViolationDataVo;
import com.java110.vo.api.pubCarViolation.ApiPubCarViolationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 移动端查询用车违章 侦听
 * @author hh
 * @date 2021/10/13
 */
@Java110Listener("listPubCarViolationsListener")
public class ListPubCarViolationsListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarViolationInnerServiceSMO pubCarViolationInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "handleStatus", "handleStatus 参数不能为空");
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        PubCarViolationDto pubCarViolationDto = BeanConvertUtil.covertBean(reqJson, PubCarViolationDto.class);

        int count = pubCarViolationInnerServiceSMOImpl.queryPubCarViolationsCount(pubCarViolationDto);

        List<ApiPubCarViolationDataVo> pubCarViolations;

        if (count > 0) {
            pubCarViolations = pubCarViolationInnerServiceSMOImpl.queryPubCarViolations(pubCarViolationDto);
        } else {
            pubCarViolations = Collections.emptyList();
        }

        ApiPubCarViolationVo apiPubCarViolationVo = new ApiPubCarViolationVo();

        apiPubCarViolationVo.setTotal(count);
        apiPubCarViolationVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarViolationVo.setData(pubCarViolations);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPubCarViolationVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarViolationConstant.LIST_PUBCARVIOLATIONS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
