package com.java110.api.listener.inspectionPoint;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspection.IInspectionBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.inspectionPlan.InspectionDto;
import com.java110.intf.community.IInspectionInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeInspectionPointConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;
import java.util.List;

/**
 * 巡检点定位
 * @author hh
 * create date 2021/9/14
 */
@Java110Listener("locateInspectionPointListener")
public class LocateInspectionPointListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IInspectionBMO inspectionBMOImpl;

    @Autowired
    private IInspectionInnerServiceSMO inspectionInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "inspectionId", "巡检点ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "location", "位置坐标信息不能为空");
        if (!reqJson.getString("location").matches(ConstantUtil.REGEX_LOCATION)){
            throw new IllegalArgumentException("非法位置坐标格式");
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        InspectionDto inspectionDto = new InspectionDto();
        inspectionDto.setInspectionId(reqJson.getString("inspectionId"));
        List<InspectionDto> inspectionDtos = inspectionInnerServiceSMOImpl.queryInspections(inspectionDto);

        Assert.listOnlyOne(inspectionDtos, "根据巡检点id查询到多条记录，请检查数据");

        JSONObject businessInspectionPoint = new JSONObject();
        businessInspectionPoint.putAll(BeanConvertUtil.beanCovertMap(inspectionDtos.get(0)));
        businessInspectionPoint.put("location", reqJson.getString("location"));

        inspectionBMOImpl.updateInspectionPoint(businessInspectionPoint, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionPointConstant.LOCATE_INSPECTIONPOINT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
