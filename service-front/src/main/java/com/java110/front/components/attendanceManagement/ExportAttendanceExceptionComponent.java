package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IExportAttendanceExceptionSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * 导出考勤异常数据
 * @author hh
 * @date 2021/11/26
 */
@Component("exportAttendanceException")
public class ExportAttendanceExceptionComponent {

    @Autowired
    private IExportAttendanceExceptionSMO exportAttendanceExceptionSMOImpl;

    /**
     * 导出考勤异常数据
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<Object> export(IPageData pd) throws UnsupportedEncodingException {
        return exportAttendanceExceptionSMOImpl.exportAttendanceException(pd);
    }

}
