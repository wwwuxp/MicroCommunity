package com.java110.api.listener.attendance;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.attendance.PassRecordDto;
import com.java110.intf.community.IPassRecordInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAttendanceConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.passRecord.ApiPassRecordDataVo;
import com.java110.vo.api.passRecord.ApiPassRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询通行记录列表服务侦听
 * @author hh
 * @date 2022/1/26
 */
@Java110Listener("listPassRecordListener")
public class ListPassRecordListener extends AbstractServiceApiListener {

    @Autowired
    private IPassRecordInnerServiceSMO passRecordInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        PassRecordDto passRecordDto = BeanConvertUtil.covertBean(reqJson, PassRecordDto.class);

        int count = passRecordInnerServiceSMOImpl.queryPassRecordsCount(passRecordDto);

        List<ApiPassRecordDataVo> apiPassRecordDataVos;

        if (count > 0) {
            apiPassRecordDataVos = passRecordInnerServiceSMOImpl.queryPassRecords(passRecordDto);
        } else {
            apiPassRecordDataVos = Collections.emptyList();
        }

        ApiPassRecordVo apiPassRecordVo = new ApiPassRecordVo();

        apiPassRecordVo.setTotal(count);
        apiPassRecordVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPassRecordVo.setData(apiPassRecordDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPassRecordVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceConstant.LIST_PASS_RECORD;
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
