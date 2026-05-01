package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/10/8
 */
public interface IVerifyAttendanceExceptionSMO {

    /**
     * 考勤异常核实
     * @param pd 请求数据封装
     * @return responseEntity
     */
    ResponseEntity<String> verifyAttendanceException(IPageData pd);
}
