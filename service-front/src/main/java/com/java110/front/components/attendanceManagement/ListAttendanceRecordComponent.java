package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IListAttendanceRecordSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询考勤记录列表
 * @author hh
 * create date 2021/9/23
 */
@Component("listAttendanceRecord")
public class ListAttendanceRecordComponent {

    @Autowired
    private IListAttendanceRecordSMO listAttendanceRecordSMOImpl;

    /**
     * 查询考勤记录
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> list(IPageData pd){
        return listAttendanceRecordSMOImpl.listAttendanceRecord(pd);
    }

}
