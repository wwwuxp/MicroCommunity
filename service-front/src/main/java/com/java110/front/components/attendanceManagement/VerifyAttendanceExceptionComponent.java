package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IVerifyAttendanceExceptionSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2021/10/8
 */
@Component("verifyAttendanceException")
public class VerifyAttendanceExceptionComponent {

    @Autowired
    private IVerifyAttendanceExceptionSMO verifyAttendanceExceptionSMOImpl;

    public ResponseEntity<String> verify(IPageData pd) {
        return verifyAttendanceExceptionSMOImpl.verifyAttendanceException(pd);
    }
}
