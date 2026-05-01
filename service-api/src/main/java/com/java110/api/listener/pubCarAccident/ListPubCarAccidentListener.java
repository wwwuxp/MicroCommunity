package com.java110.api.listener.pubCarAccident;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarAccident.PubCarAccidentDto;
import com.java110.intf.pubcar.IPubCarAccidentInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarAccidentConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarAccident.ApiPubCarAccidentDataVo;
import com.java110.vo.api.pubCarAccident.ApiPubCarAccidentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询用车事故列表 侦听
 * @author hh
 * @date 2021/10/11
 */
@Java110Listener("listPubCarAccidentListener")
public class ListPubCarAccidentListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarAccidentInnerServiceSMO pubCarAccidentInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        PubCarAccidentDto pubCarAccidentDto = BeanConvertUtil.covertBean(reqJson, PubCarAccidentDto.class);

        int count = pubCarAccidentInnerServiceSMOImpl.listPubCarAccidentsCount(pubCarAccidentDto);

        List<ApiPubCarAccidentDataVo> pubCarAccidentDataVos;

        if (count > 0) {
            pubCarAccidentDataVos = pubCarAccidentInnerServiceSMOImpl.listPubCarAccidents(pubCarAccidentDto);
        } else {
            pubCarAccidentDataVos = Collections.emptyList();
        }

        ApiPubCarAccidentVo apiPubCarAccidentVo = new ApiPubCarAccidentVo();

        apiPubCarAccidentVo.setTotal(count);
        apiPubCarAccidentVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarAccidentVo.setData(pubCarAccidentDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPubCarAccidentVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarAccidentConstant.LIST_PUBCAR_ACCIDENT;
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
