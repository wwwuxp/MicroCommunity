package com.java110.api.listener.outGoing;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.intf.pubcar.IOutGoingInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeOutGoingConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.outGoing.ApiOutGoingStatisticsDataVo;
import com.java110.vo.api.outGoing.ApiOutGoingStatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询外出统计列表
 * @author hh
 * @date 2021/12/20
 */
@Java110Listener("listOutGoingStatisticsListener")
public class ListOutGoingStatisticsListener extends AbstractServiceApiListener {

    @Autowired
    private IOutGoingInnerServiceSMO outGoingInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        OutGoingDto outGoingDto = BeanConvertUtil.covertBean(reqJson, OutGoingDto.class);

        int count = outGoingInnerServiceSMOImpl.queryOutGoingStatisticsCount(outGoingDto);

        List<ApiOutGoingStatisticsDataVo> apiOutGoingStatisticsDataVos;

        if (count > 0) {
            apiOutGoingStatisticsDataVos = outGoingInnerServiceSMOImpl.queryOutGoingStatistics(outGoingDto);
        } else {
            apiOutGoingStatisticsDataVos = Collections.emptyList();
        }

        ApiOutGoingStatisticsVo apiOutGoingStatisticsVo = new ApiOutGoingStatisticsVo();

        apiOutGoingStatisticsVo.setTotal(count);
        apiOutGoingStatisticsVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiOutGoingStatisticsVo.setData(apiOutGoingStatisticsDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiOutGoingStatisticsVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOutGoingConstant.LIST_OUTGOING_STATISTICS;
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
