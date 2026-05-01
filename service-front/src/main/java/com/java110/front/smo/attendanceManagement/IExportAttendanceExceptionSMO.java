package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;

/**
 * @author hh
 * @date 2021/11/26
 */
public interface IExportAttendanceExceptionSMO {

    /**
     * 导出考勤异常数据
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<Object> exportAttendanceException(IPageData pd) throws UnsupportedEncodingException;

}
