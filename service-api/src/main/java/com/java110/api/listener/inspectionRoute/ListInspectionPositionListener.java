package com.java110.api.listener.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.inspectionPlan.InspectionPositionDto;
import com.java110.intf.community.IInspectionRouteInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeInspectionRouteConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionRoute.ApiInspectionPositionDataVo;
import com.java110.vo.api.inspectionRoute.ApiInspectionPositionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询巡检定位（移动端巡检定位列表查询）
 * @author hh
 * create date 2021/9/18
 */
@Java110Listener("listInspectionPositionListener")
public class ListInspectionPositionListener extends AbstractServiceApiListener {

    @Autowired
    private IInspectionRouteInnerServiceSMO inspectionRouteInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
        validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        InspectionPositionDto inspectionPositionDto = BeanConvertUtil.covertBean(reqJson, InspectionPositionDto.class);

        int count = inspectionRouteInnerServiceSMOImpl.queryInspectionPositionsCount(inspectionPositionDto);

        List<ApiInspectionPositionDataVo> inspectionPositionDatas;

        if (count > 0) {
            inspectionPositionDatas = inspectionRouteInnerServiceSMOImpl.queryInspectionPositions(inspectionPositionDto);

        } else {
            inspectionPositionDatas = Collections.emptyList();
        }

        ApiInspectionPositionVo apiInspectionPositionVo = new ApiInspectionPositionVo();

        apiInspectionPositionVo.setTotal(count);
        apiInspectionPositionVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionPositionVo.setData(inspectionPositionDatas);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiInspectionPositionVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionRouteConstant.LIST_INSPECTION_LOCATIONS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

}
