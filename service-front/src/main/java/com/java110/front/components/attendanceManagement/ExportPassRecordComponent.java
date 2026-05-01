package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IExportPassRecordSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 导出通行记录数据
 * @author hh
 * @date 2022/1/26
 */
@Component("exportPassRecord")
public class ExportPassRecordComponent {

    @Autowired
    private IExportPassRecordSMO exportPassRecordSMOImpl;

    /**
     * 导出通行记录数据
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    public ResponseEntity<Object> export(IPageData pd) {
        return exportPassRecordSMOImpl.exportPassRecord(pd);
    }

}
