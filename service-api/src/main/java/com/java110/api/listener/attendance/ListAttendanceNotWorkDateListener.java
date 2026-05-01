package com.java110.api.listener.attendance;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.attendance.AttendanceNotWorkDateDto;
import com.java110.intf.community.IAttendanceNotWorkDateSMO;
import com.java110.utils.constant.ServiceCodeAttendanceConstant;
import com.java110.vo.api.attendance.AttendanceNotWorkDateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * @author lmf
 * @date 2022/6/29
 */
@Java110Listener("listAttendanceNotWorkDateListener")
public class ListAttendanceNotWorkDateListener extends AbstractServiceApiListener {

    @Autowired
    private IAttendanceNotWorkDateSMO attendanceNotWorkDateSMO;

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceConstant.LIST_ATTENDANCE_NOT_WORK_DATE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        AttendanceNotWorkDateDto attendanceNotWorkDateDto = reqJson.toJavaObject(AttendanceNotWorkDateDto.class);
        int total = attendanceNotWorkDateSMO.attendanceNotWorkDateCount(attendanceNotWorkDateDto);
        List<AttendanceNotWorkDateVo> attendanceNotWorkDateVos;
        if (total > 0) {
            attendanceNotWorkDateVos = attendanceNotWorkDateSMO.attendanceNotWorkDatePageList(attendanceNotWorkDateDto);
        } else {
            attendanceNotWorkDateVos = Collections.emptyList();
        }
        JSONObject result = new JSONObject();
        result.put("records", (int) Math.ceil((double) total / (double) reqJson.getInteger("row")));
        result.put("total", total);
        result.put("data", attendanceNotWorkDateVos);
        result.put("page", reqJson.getInteger("page"));
        result.put("rows", reqJson.getInteger("rows"));

        ResponseEntity<String> responseEntity = new ResponseEntity<>(result.toJSONString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
