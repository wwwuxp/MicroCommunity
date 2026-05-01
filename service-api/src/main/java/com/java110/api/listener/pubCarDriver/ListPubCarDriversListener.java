package com.java110.api.listener.pubCarDriver;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarDriverInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarDriverConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.pubCarDriver.PubCarDriverDto;
import com.java110.utils.util.ConstantUtil;
import com.java110.vo.api.pubCarTripDriver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询司机侦听类
 */
@Java110Listener("listPubCarDriversListener")
public class ListPubCarDriversListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarDriverInnerServiceSMO pubCarDriverInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarDriverConstant.LIST_PUBCAR_DRIVER;
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
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        // 只获取司机名字
        if (reqJson.containsKey(ConstantUtil.SIMPLE_FLAG) && ConstantUtil.SIMPLE_FLAG_YES.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))){
            onlyGetDriverNames(context);
            return;
        }

        // 获取司机的信息,这里把《司机管理》，《司机用车统计》中查询司机信息的查询统一到一个查询语句上，用simple_flag作为标记
        PubCarDriverDto pubCarDriverDto = BeanConvertUtil.covertBean(reqJson, PubCarDriverDto.class);

        if (reqJson.containsKey(ConstantUtil.SIMPLE_FLAG) && ConstantUtil.SIMPLE_FLAG_STATICS.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))){
            pubCarDriverDto.setSimpleFlag(reqJson.getString(ConstantUtil.SIMPLE_FLAG));
        }

        int count = pubCarDriverInnerServiceSMOImpl.queryPubCarDriversCount(pubCarDriverDto);

        List<ApiPubCarDriverDataVo> pubCarDrivers;

        if (count > 0) {
            pubCarDrivers = pubCarDriverInnerServiceSMOImpl.queryPubCarDrivers(pubCarDriverDto);
        } else {
            pubCarDrivers = Collections.emptyList();
        }

        ApiPubCarDriverVo apiPubCarDriverVo = new ApiPubCarDriverVo();

        apiPubCarDriverVo.setTotal(count);
        apiPubCarDriverVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarDriverVo.setData(pubCarDrivers);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPubCarDriverVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    /**
     * 获取司机名字
     * @param context
     */
    private void onlyGetDriverNames(DataFlowContext context) {
        List<ApiPubCarSimpleDriverDataVo> pubCarDriverNames = pubCarDriverInnerServiceSMOImpl.queryPubCarDriverNames();

        ApiPubCarSimpleDriverVo apiPubCarDriverNameVo = new ApiPubCarSimpleDriverVo();

        apiPubCarDriverNameVo.setTotal(pubCarDriverNames.size());
        apiPubCarDriverNameVo.setRecords(0);
        apiPubCarDriverNameVo.setData(pubCarDriverNames);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPubCarDriverNameVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

}
