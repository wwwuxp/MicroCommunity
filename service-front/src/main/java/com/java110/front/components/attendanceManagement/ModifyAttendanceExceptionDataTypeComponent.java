package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IModifyAttendanceExceptionDataTypeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 考勤异常 - 修改类型
 * @author hh
 * @date 2021/10/8
 */
@Component("modifyAttendanceExceptionDataType")
public class ModifyAttendanceExceptionDataTypeComponent {

    @Autowired
    private IModifyAttendanceExceptionDataTypeSMO modifyAttendanceExceptionDataTypeSMOImpl;

    public ResponseEntity<String> modify(IPageData pd) {
        return modifyAttendanceExceptionDataTypeSMOImpl.modifyAttendanceExceptionDataType(pd);
    }

}
