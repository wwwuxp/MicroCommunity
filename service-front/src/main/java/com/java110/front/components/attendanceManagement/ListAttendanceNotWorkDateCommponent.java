package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IListAttendanceNotWorkDateSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author lmf
 * @date 2022/6/29
 */
@Component("listAttendanceNotWorkDate")
public class ListAttendanceNotWorkDateCommponent {

    @Autowired
    private IListAttendanceNotWorkDateSMO listAttendanceNotWorkDateSMO;

    /**
     * 中午考勤记录查询
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> list(IPageData pd){
        return listAttendanceNotWorkDateSMO.listAttendanceNotWorkDate(pd);
    }


}
