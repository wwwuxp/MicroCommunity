package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IListAttendanceExceptionSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询考勤异常列表
 * @author hh
 * @date 2021/10/8
 */
@Component("listAttendanceException")
public class ListAttendanceExceptionComponent {

    @Autowired
    private IListAttendanceExceptionSMO listAttendanceExceptionSMOImpl;

    public ResponseEntity<String> list(IPageData pd) {
        return listAttendanceExceptionSMOImpl.listAttendanceException(pd);
    }

}
