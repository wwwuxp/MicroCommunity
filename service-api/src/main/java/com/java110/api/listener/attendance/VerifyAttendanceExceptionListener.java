package com.java110.api.listener.attendance;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.community.IAttendanceExceptionInnerServiceSMO;
import com.java110.po.attendanceException.AttendanceExceptionPo;
import com.java110.utils.constant.ServiceCodeAttendanceConstant;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 核实考勤异常
 * @author hh
 * @date 2021/10/8
 */
@Java110Listener("verifyAttendanceExceptionListener")
public class VerifyAttendanceExceptionListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IAttendanceExceptionInnerServiceSMO attendanceExceptionInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        Integer id = reqJson.getInteger("id");
        boolean verify = attendanceExceptionInnerServiceSMOImpl.verifyAttendanceException(id, AttendanceExceptionPo.VERIFY_TRUE);
        if (verify){
            context.setResponseEntity(ResultVo.success());
        }else {
            context.setResponseEntity(ResultVo.error("核实失败"));
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceConstant.VERIFY_ATTENDANCE_EXCEPTION;
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
