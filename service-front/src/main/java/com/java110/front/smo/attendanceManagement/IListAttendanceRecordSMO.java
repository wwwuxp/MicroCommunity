package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * create date 2021/9/23
 */
public interface IListAttendanceRecordSMO {

    /**
     * 查询考勤记录
     * @param pd 封装数据
     * @return ResponseEntity
     */
    ResponseEntity<String> listAttendanceRecord(IPageData pd);

}
