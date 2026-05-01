package com.java110.api.listener.attendance;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.community.IAttendanceExceptionInnerServiceSMO;
import com.java110.intf.community.IAttendanceNotWorkDateSMO;
import com.java110.po.attendance.AttendanceNotWorkDatePo;
import com.java110.po.attendanceException.AttendanceExceptionDataPo;
import com.java110.utils.constant.ServiceCodeAttendanceConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 非工作日、新增or修改
 * @author lmf
 * @date 2022/06/30
 */
@Java110Listener("modifyAttendanceNotWorkDateListener")
public class ModifyAttendanceNotWorkDateListener extends AbstractServiceApiPlusListener {

    private static final Logger logger = LoggerFactory.getLogger(ModifyAttendanceNotWorkDateListener.class);

    @Autowired
    private IAttendanceNotWorkDateSMO attendanceNotWorkDateSMO;

    @Autowired
    IAttendanceExceptionInnerServiceSMO attendanceExceptionInnerServiceSMO;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "year", "必填，请填写年份");
        Assert.hasKeyAndValue(reqJson, "content", "必填，请填写非工作日");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        AttendanceNotWorkDatePo po = reqJson.toJavaObject(AttendanceNotWorkDatePo.class);
        //如果type为空，默认设置为1
        if (po.getType() == null){
            po.setType(1);
        }
        attendanceNotWorkDateSMO.attendanceNotWorkDateSaveAndEdit(po);
        try {
            List<AttendanceExceptionDataPo> exceptionDataPoList = attendanceExceptionInnerServiceSMO.getAttendanceExceptionInfoByDateStr(po.getContent());
            List<String> collect = exceptionDataPoList.stream().map(r -> r.getId()).collect(Collectors.toList());
            attendanceExceptionInnerServiceSMO.deleteAttendanceExceptionByIds(collect);
        }catch (Exception e){
            logger.error("新增时删除考勤异常数据失败 ：{}" , e);
        }
        context.setResponseEntity(ResultVo.success());
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceConstant.SAVE_OR_EDIT_ATTENDANCE_NOT_WORK_DATE;
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
