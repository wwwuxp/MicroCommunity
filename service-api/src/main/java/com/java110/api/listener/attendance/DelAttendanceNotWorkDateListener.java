package com.java110.api.listener.attendance;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.community.IAttendanceNotWorkDateSMO;
import com.java110.utils.constant.ServiceCodeAttendanceConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;

/**
 * 非工作日、新增or修改
 * @author lmf
 * @date 2022/06/30
 */
@Java110Listener("delAttendanceNotWorkDateListener")
public class DelAttendanceNotWorkDateListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IAttendanceNotWorkDateSMO attendanceNotWorkDateSMO;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "ids", "必填，请填写ids");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        String ids = reqJson.getString("ids");
        attendanceNotWorkDateSMO.attendanceNotWorkDateDel(ids);
        context.setResponseEntity(ResultVo.success());
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceConstant.DELETE_ATTENDANCE_NOT_WORK_DATE;
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
