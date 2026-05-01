package com.java110.api.listener.attendance;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.attendance.AttendanceNoonRecordDto;
import com.java110.intf.community.IAttendanceNoonRecordInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAttendanceConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.attendanceNoonRecord.ApiAttendanceNoonRecordDataVo;
import com.java110.vo.api.attendanceNoonRecord.ApiAttendanceNoonRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询中午考勤记录列表
 * @author hh
 * @date 2021/12/17
 */
@Java110Listener("listAttendanceNoonRecordListener")
public class ListAttendanceNoonRecordListener extends AbstractServiceApiListener {

    @Autowired
    private IAttendanceNoonRecordInnerServiceSMO attendanceNoonRecordInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        AttendanceNoonRecordDto attendanceNoonRecordDto = BeanConvertUtil.covertBean(reqJson, AttendanceNoonRecordDto.class);

        int count = attendanceNoonRecordInnerServiceSMOImpl.queryAttendanceNoonRecordsCount(attendanceNoonRecordDto);

        List<ApiAttendanceNoonRecordDataVo> apiAttendanceNoonRecordDataVos;

        if (count > 0) {
            apiAttendanceNoonRecordDataVos = attendanceNoonRecordInnerServiceSMOImpl.queryAttendanceNoonRecords(attendanceNoonRecordDto);
        } else {
            apiAttendanceNoonRecordDataVos = Collections.emptyList();
        }

        ApiAttendanceNoonRecordVo apiAttendanceNoonRecordVo = new ApiAttendanceNoonRecordVo();

        apiAttendanceNoonRecordVo.setTotal(count);
        apiAttendanceNoonRecordVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiAttendanceNoonRecordVo.setData(apiAttendanceNoonRecordDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiAttendanceNoonRecordVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceConstant.LIST_ATTENDANCE_NOON_RECORD;
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
