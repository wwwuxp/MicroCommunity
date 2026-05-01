package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/12/17
 */
public interface IListAttendanceNoonRecordSMO {

    /**
     * 中午考勤记录查询
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> listAttendanceNoonRecord(IPageData pd);

}
