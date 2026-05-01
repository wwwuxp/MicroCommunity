package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2022/1/26
 */
public interface IExportPassRecordSMO {

    /**
     * 导出通行记录数据
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    ResponseEntity<Object> exportPassRecord(IPageData pd);

}
