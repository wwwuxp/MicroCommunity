package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;

/**
 * @author hh
 * @date 2021/11/24
 */
public interface IExportAttendanceRecordSMO {

    /**
     * 导出考勤记录数据
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<Object> exportAttendanceRecord(IPageData pd) throws UnsupportedEncodingException;

}
