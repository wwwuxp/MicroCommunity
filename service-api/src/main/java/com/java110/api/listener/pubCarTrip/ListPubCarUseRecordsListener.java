package com.java110.api.listener.pubCarTrip;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarTrip.PubCarTripDto;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarTripConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarTrip.ApiCarUseRecordDataVo;
import com.java110.vo.api.pubCarTrip.ApiCarUseRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * APP端用户用车记录
 */
@Java110Listener("listPubCarUseRecordsListener")
public class ListPubCarUseRecordsListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarTripInnerServiceSMO pubCarTripInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        PubCarTripDto pubCarTripDto = BeanConvertUtil.covertBean(reqJson, PubCarTripDto.class);
        // 查找指定用户的行程记录
        pubCarTripDto.setUserId(context.getUserId());

        int count = pubCarTripInnerServiceSMOImpl.queryPubCarUseRecordsCount(pubCarTripDto);

        List<ApiCarUseRecordDataVo> trips;

        if (count > 0) {
            trips = pubCarTripInnerServiceSMOImpl.queryPubCarUseRecords(pubCarTripDto);
        } else {
            trips = Collections.emptyList();
        }

        ApiCarUseRecordVo apiCarUseRecordVo= new ApiCarUseRecordVo();

        apiCarUseRecordVo.setTotal(count);
        apiCarUseRecordVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCarUseRecordVo.setData(trips);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiCarUseRecordVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripConstant.RECORD_PUBCARTRIP;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

}
