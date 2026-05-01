package com.java110.front.components.assetImport;

import com.java110.core.context.IPageData;
import com.java110.front.smo.assetExport.IExportTempCarInoutFeeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @datetime 2021/6/25 14:12
 */
@Component("exportTempCarInoutFee")
public class ExportTempCarInoutFeeComponent {

    @Autowired
    private IExportTempCarInoutFeeSMO exportTempCarInoutFeeSMO;

    public ResponseEntity<Object> exportData(IPageData pd) {
        return exportTempCarInoutFeeSMO.exportExcelData(pd);
    }

}
