package com.java110.api.listener.diningRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.diningRecord.DiningRecordDto;
import com.java110.intf.community.IDiningRecordInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeDiningRecordConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.diningRecord.ApiDiningDetailDataVo;
import com.java110.vo.api.diningRecord.ApiDiningDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 获取员工就餐详情
 * @author hh
 * @date 2021/11/12
 */
@Java110Listener("getStaffDiningDetailListener")
public class GetStaffDiningDetailListener extends AbstractServiceApiListener {

    @Autowired
    private IDiningRecordInnerServiceSMO diningRecordInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        DiningRecordDto diningRecordDto = BeanConvertUtil.covertBean(reqJson, DiningRecordDto.class);

        List<ApiDiningDetailDataVo> diningDetailDataVos = diningRecordInnerServiceSMOImpl.queryDiningDetails(diningRecordDto);

        ApiDiningDetailVo apiDiningDetailVo = new ApiDiningDetailVo();

        // apiDiningDetailVo.setTotal(0);
        // apiDiningDetailVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiDiningDetailVo.setData(diningDetailDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiDiningDetailVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeDiningRecordConstant.GET_DINING_DETAIL;
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
