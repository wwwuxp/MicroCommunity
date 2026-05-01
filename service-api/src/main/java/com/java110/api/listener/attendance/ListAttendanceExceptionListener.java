package com.java110.api.listener.attendance;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.attendance.AttendanceExceptionDto;
import com.java110.intf.community.IAttendanceExceptionInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAttendanceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.attendanceException.ApiAttendanceExceptionDataVo;
import com.java110.vo.api.attendanceException.ApiAttendanceExceptionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 查询考勤异常列表
 * @author hh
 * @date 2021/10/8
 */
@Java110Listener("listAttendanceExceptionListener")
public class ListAttendanceExceptionListener extends AbstractServiceApiListener {

    @Autowired
    private IAttendanceExceptionInnerServiceSMO attendanceExceptionInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        AttendanceExceptionDto attendanceExceptionDto = BeanConvertUtil.covertBean(reqJson, AttendanceExceptionDto.class);

        // 若考勤月份为空，自动补为当月
        if (StringUtils.isBlank(attendanceExceptionDto.getAttendanceMonth())){
            LocalDate localDate = LocalDate.now();
            attendanceExceptionDto.setAttendanceMonth(localDate.toString());
        }

        String attendanceMonth = attendanceExceptionDto.getAttendanceMonth();
        String[] attendanceMonthSplit = attendanceMonth.split("-");
        String monthValue = attendanceMonthSplit[1];
        if (monthValue.length() == 1){
            monthValue = "0" + monthValue;
        }
        attendanceExceptionDto.setAttendanceMonth(attendanceMonthSplit[0] + "-" + monthValue + "-" + attendanceMonthSplit[2]);

        Set<String> attendanceMonthSet = new HashSet<>();
        attendanceMonthSet.add(attendanceExceptionDto.getAttendanceMonth());
        String attendanceEndMonth = attendanceExceptionDto.getAttendanceEndMonth();
        if (StringUtils.isNotBlank(attendanceEndMonth)){
            String[] attendanceEndMonthSplit = attendanceEndMonth.split("-");
            String endMonthValue = attendanceEndMonthSplit[1];
            if (endMonthValue.length() == 1){
                endMonthValue = "0" + endMonthValue;
            }
            attendanceEndMonth = attendanceEndMonthSplit[0] + "-" + endMonthValue + "-01";
            // 判断考勤结束月份是否小于开始月份
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);
            LocalDate startDate = LocalDate.parse(attendanceExceptionDto.getAttendanceMonth(), dateFormatter);
            LocalDate endDate = LocalDate.parse(attendanceEndMonth, dateFormatter);
            if (endDate.isBefore(startDate)){
                throw new IllegalArgumentException("结束日期不能在开始日期之前");
            }

            LocalDate tempDate = startDate;
            while (tempDate.isBefore(endDate)){
                tempDate = tempDate.plusMonths(1);
                attendanceMonthSet.add(tempDate.toString());
            }
        }
        attendanceExceptionDto.setRange(attendanceMonthSet);

        int count = attendanceExceptionInnerServiceSMOImpl.queryAttendanceExceptionsCount(attendanceExceptionDto);

        List<ApiAttendanceExceptionDataVo> apiAttendanceExceptionDataVos;

        if (count > 0) {
            apiAttendanceExceptionDataVos = attendanceExceptionInnerServiceSMOImpl.queryAttendanceExceptions(attendanceExceptionDto);
        } else {
            apiAttendanceExceptionDataVos = Collections.emptyList();
        }

        for (ApiAttendanceExceptionDataVo apiAttendanceExceptionDataVo : apiAttendanceExceptionDataVos) {
            apiAttendanceExceptionDataVo.setExceptionCount(apiAttendanceExceptionDataVo.getExceptionData().size());
        }

        ApiAttendanceExceptionVo apiAttendanceExceptionVo = new ApiAttendanceExceptionVo();

        apiAttendanceExceptionVo.setTotal(count);
        apiAttendanceExceptionVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiAttendanceExceptionVo.setData(apiAttendanceExceptionDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiAttendanceExceptionVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceConstant.LIST_ATTENDANCE_EXCEPTION;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

}
