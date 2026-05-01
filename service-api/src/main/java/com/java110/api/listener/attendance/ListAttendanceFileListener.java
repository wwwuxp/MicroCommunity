package com.java110.api.listener.attendance;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.attendance.AttendanceFileDto;
import com.java110.intf.community.IAttendanceFileInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAttendanceConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.attendanceFile.ApiAttendanceFileDataVo;
import com.java110.vo.api.attendanceFile.ApiAttendanceFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询考勤管理文件导入文件信息
 * @author hh
 * @date 2021/9/27
 */
@Java110Listener("listAttendanceFileListener")
public class ListAttendanceFileListener extends AbstractServiceApiListener {

    @Autowired
    private IAttendanceFileInnerServiceSMO attendanceFileInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        AttendanceFileDto attendanceFileDto = BeanConvertUtil.covertBean(reqJson, AttendanceFileDto.class);

        int count = attendanceFileInnerServiceSMOImpl.queryAttendanceFilesCount(attendanceFileDto);

        List<ApiAttendanceFileDataVo> apiAttendanceFileDataVos;

        if (count > 0) {
            apiAttendanceFileDataVos = attendanceFileInnerServiceSMOImpl.queryAttendanceFiles(attendanceFileDto);
        } else {
            apiAttendanceFileDataVos = Collections.emptyList();
        }

        ApiAttendanceFileVo apiAttendanceFileVo = new ApiAttendanceFileVo();

        apiAttendanceFileVo.setTotal(count);
        apiAttendanceFileVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiAttendanceFileVo.setData(apiAttendanceFileDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiAttendanceFileVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceConstant.LIST_ATTENDANCE_FILE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}
