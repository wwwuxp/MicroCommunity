package com.java110.api.listener.pubCarRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarRepair.PubCarRepairDto;
import com.java110.intf.pubcar.IPubCarRepairInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarRepairConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarRepair.ApiPubCarRepairDataVo;
import com.java110.vo.api.pubCarRepair.ApiPubCarRepairVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listPubCarRepairsListener")
public class ListPubCarRepairsListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarRepairInnerServiceSMO pubCarRepairInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarRepairConstant.LIST_PUBCARREPAIRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        PubCarRepairDto pubCarRepairDto = BeanConvertUtil.covertBean(reqJson, PubCarRepairDto.class);

        int count = pubCarRepairInnerServiceSMOImpl.queryPubCarRepairsCount(pubCarRepairDto);

        List<ApiPubCarRepairDataVo> pubCarRepairs;

        if (count > 0) {
            pubCarRepairs = BeanConvertUtil.covertBeanList(pubCarRepairInnerServiceSMOImpl.queryPubCarRepairs(pubCarRepairDto), ApiPubCarRepairDataVo.class);
        } else {
            pubCarRepairs = new ArrayList<>();
        }

        ApiPubCarRepairVo apiPubCarRepairVo = new ApiPubCarRepairVo();

        apiPubCarRepairVo.setTotal(count);
        apiPubCarRepairVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarRepairVo.setData(pubCarRepairs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPubCarRepairVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
