package com.java110.api.listener.pubCarAlarm;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarAlarmInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarAlarmConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.pubCarAlarm.PubCarAlarmDto;
import com.java110.vo.api.pubCarAlarm.ApiPubCarAlarmDataVo;
import com.java110.vo.api.pubCarAlarm.ApiPubCarAlarmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询报警信息侦听类
 */
@Java110Listener("listPubCarAlarmsListener")
public class ListPubCarAlarmsListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarAlarmInnerServiceSMO pubCarAlarmInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarAlarmConstant.LIST_PUBCARALARMS;
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
        Assert.hasKeyAndValue(reqJson, "communityId", "小区id不能为空");
        validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        PubCarAlarmDto pubCarAlarmDto = BeanConvertUtil.covertBean(reqJson, PubCarAlarmDto.class);

        int count = pubCarAlarmInnerServiceSMOImpl.queryPubCarAlarmsCount(pubCarAlarmDto);

        List<ApiPubCarAlarmDataVo> pubCarAlarms;

        if (count > 0) {
            pubCarAlarms = pubCarAlarmInnerServiceSMOImpl.queryPubCarAlarms(pubCarAlarmDto);
        } else {
            pubCarAlarms = Collections.emptyList();
        }

        ApiPubCarAlarmVo apiPubCarAlarmVo = new ApiPubCarAlarmVo();

        apiPubCarAlarmVo.setTotal(count);
        apiPubCarAlarmVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarAlarmVo.setData(pubCarAlarms);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPubCarAlarmVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
