package com.java110.front.components.assetImport;

import com.java110.core.context.IPageData;
import com.java110.front.smo.assetExport.IExportStaffFeeSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @datetime 2021/6/24 14:13
 */
@Component("exportStaffFee")
public class ExportStaffFeeComponent {

    @Autowired
    private IExportStaffFeeSMO exportStaffFeeSMOImpl;

    public ResponseEntity<Object> exportData(IPageData pd) {
        return exportStaffFeeSMOImpl.exportExcelData(pd);
    }

}
