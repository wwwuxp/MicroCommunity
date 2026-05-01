package com.java110.api.listener.attendance;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.intf.community.IAttendanceRecordInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAttendanceConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.attendanceRecord.ApiAttendanceRecordDataVo;
import com.java110.vo.api.attendanceRecord.ApiAttendanceRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询考勤记录列表 服务侦听
 * @author hh
 * create date 2021/9/23
 */
@Java110Listener("listAttendanceRecordListener")
public class ListAttendanceRecordListener extends AbstractServiceApiListener {

    @Autowired
    private IAttendanceRecordInnerServiceSMO attendanceRecordInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        AttendanceRecordDto attendanceRecordDto = BeanConvertUtil.covertBean(reqJson, AttendanceRecordDto.class);

        int count = attendanceRecordInnerServiceSMOImpl.queryAttendanceRecordsCount(attendanceRecordDto);

        List<ApiAttendanceRecordDataVo> apiAttendanceRecordDataVos;

        if (count > 0) {
            apiAttendanceRecordDataVos = attendanceRecordInnerServiceSMOImpl.queryAttendanceRecords(attendanceRecordDto);
        } else {
            apiAttendanceRecordDataVos = Collections.emptyList();
        }

        ApiAttendanceRecordVo apiAttendanceRecordVo = new ApiAttendanceRecordVo();

        apiAttendanceRecordVo.setTotal(count);
        apiAttendanceRecordVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiAttendanceRecordVo.setData(apiAttendanceRecordDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiAttendanceRecordVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceConstant.LIST_ATTENDANCE_RECORD;
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
