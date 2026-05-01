package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IImportAttendanceFileSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hh
 * create date 2021/9/24
 */
@Component("attendanceFileImport")
public class ImportAttendanceFileComponent {

    @Autowired
    private IImportAttendanceFileSMO importAttendanceFileSMOImpl;

    public ResponseEntity<String> importData(IPageData pd, MultipartFile uploadFile) throws Exception {
        return importAttendanceFileSMOImpl.importExcelData(pd,uploadFile);
    }

}
