package com.java110.front.smo.assetExport;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @datetime 2021/6/24 10:35
 */
public interface IExportStaffFeeSMO {

    /**
     * 导出员工费用
     * @param pd 前台数据封装
     * @return ResponseEntity
     */
    ResponseEntity<Object> exportExcelData(IPageData pd);

}
