package com.java110.front.smo.assetExport;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @datetime 2021/6/25 14:14
 */
public interface IExportTempCarInoutFeeSMO {

    /**
     * 导出临时车缴费清单
     * @param pd 前台数据封装
     * @return ResponseEntity
     */
    ResponseEntity<Object> exportExcelData(IPageData pd);

}
