package com.java110.api.listener.pubCarViolation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarViolation.IPubCarViolationBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodePubCarViolationConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ConstantUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * 移动端用车违章提交申请
 * @author hh
 * @date 2021/10/13
 */
@Java110Listener("submitPubCarViolationListener")
public class SubmitPubCarViolationListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarTripInnerServiceSMO pubCarTripInnerServiceSMOImpl;

    @Autowired
    private IPubCarViolationBMO pubCarViolationBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "carId", "车辆id不能为空");
        Assert.hasKeyAndValue(reqJson, "violationTime", "违章时间不能为空");
        Assert.hasKeyAndValue(reqJson, "address", "违章地点不能为空");
        Assert.hasKeyAndValue(reqJson, "content", "违法具体内容不能为空");

        if (StringUtils.isNotBlank(reqJson.getString("deduction"))) {
            String deduction = reqJson.getString("deduction");
            if (!deduction.matches(ConstantUtil.REGEX_NUMBER)){
                throw new IllegalArgumentException("输入的扣分数据应为数字");
            }
        }else {
            reqJson.put("deduction", 0);
        }

        if (StringUtils.isNotBlank(reqJson.getString("fine"))){
            String fine = reqJson.getString("fine");
            if (!fine.matches(ConstantUtil.REGEX_DECIMAL)){
                throw new IllegalArgumentException("输入的罚款金额格式不正确");
            }
        }else {
            reqJson.put("fine", new BigDecimal("0.00"));
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        String carId = reqJson.getString("carId");
        String violationTime = reqJson.getString("violationTime");
        String communityId = reqJson.getString("communityId");
        List<String> driverIds = pubCarTripInnerServiceSMOImpl.getDriverIdByCarIdAndViolationTimeAndCommunityId(carId, violationTime, communityId);

        if (CollectionUtils.isEmpty(driverIds)){
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "该车辆在该时间段内未查询到对应的司机");
        }

        if (driverIds.size() > 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "该车辆在该时间段内查询到多名司机");
        }
        reqJson.put("driverId", driverIds.get(0));
        reqJson.put("date", violationTime);
        reqJson.put("handleStatus", 1);
        pubCarViolationBMOImpl.addCarViolation(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarViolationConstant.SUBMIT_PUBCARVIOLATION;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
