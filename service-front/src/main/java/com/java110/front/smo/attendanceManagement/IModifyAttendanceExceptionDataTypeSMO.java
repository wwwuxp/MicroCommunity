package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/10/8
 */
public interface IModifyAttendanceExceptionDataTypeSMO {

    /**
     * 核实考勤异常
     * @param pd 请求数据封装
     * @return responseEntity
     */
    ResponseEntity<String> modifyAttendanceExceptionDataType(IPageData pd);

}
