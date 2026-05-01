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
import com.java110.vo.api.diningRecord.ApiDiningRecordDataVo;
import com.java110.vo.api.diningRecord.ApiDiningRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 就餐统计
 * @author hh
 * @date 2021/11/11
 */
@Java110Listener("countDiningListener")
public class CountDiningListener extends AbstractServiceApiListener {

    @Autowired
    private IDiningRecordInnerServiceSMO diningRecordInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        DiningRecordDto diningRecordDto = BeanConvertUtil.covertBean(reqJson, DiningRecordDto.class);

        int count = diningRecordInnerServiceSMOImpl.queryDiningRecordStatisticsCount(diningRecordDto);

        List<ApiDiningRecordDataVo> diningRecordDataVos;

        if (count > 0) {
            diningRecordDataVos = diningRecordInnerServiceSMOImpl.queryDiningRecordStatistics(diningRecordDto);
        } else {
            diningRecordDataVos = Collections.emptyList();
        }

        ApiDiningRecordVo apiDiningRecordVo = new ApiDiningRecordVo();

        apiDiningRecordVo.setTotal(count);
        apiDiningRecordVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiDiningRecordVo.setData(diningRecordDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiDiningRecordVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeDiningRecordConstant.COUNT_DINING_RECORD;
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
