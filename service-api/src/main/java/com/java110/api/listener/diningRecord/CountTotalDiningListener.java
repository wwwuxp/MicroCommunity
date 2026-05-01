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
import com.java110.vo.api.diningRecord.ApiCountTotalDiningRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 统计总的就餐记录数据
 * @author hh
 * @date 2021/11/26
 */
@Java110Listener("countTotalDiningListener")
public class CountTotalDiningListener extends AbstractServiceApiListener {

    @Autowired
    private IDiningRecordInnerServiceSMO diningRecordInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        DiningRecordDto diningRecordDto = BeanConvertUtil.covertBean(reqJson, DiningRecordDto.class);

        // 获取就餐统计总数
        ApiCountTotalDiningRecordVo countVo = diningRecordInnerServiceSMOImpl.queryTotalDiningRecordStatisticsCount(diningRecordDto);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(countVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeDiningRecordConstant.COUNT_TOTAL_DINING_RECORD;
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
