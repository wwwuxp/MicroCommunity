package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IListAttendanceNoonRecordSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 中午考勤记录查询
 * @author hh
 * @date 2021/12/17
 */
@Component("listAttendanceNoonRecord")
public class ListAttendanceNoonRecordComponent {

    @Autowired
    private IListAttendanceNoonRecordSMO listAttendanceNoonRecordSMOImpl;

    /**
     * 中午考勤记录查询
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> list(IPageData pd){
        return listAttendanceNoonRecordSMOImpl.listAttendanceNoonRecord(pd);
    }

}
